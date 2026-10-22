package com.weddingplanner.common.repository;

import com.weddingplanner.common.exception.DataFileException;
import com.weddingplanner.common.exception.ResourceNotFoundException;
import com.weddingplanner.common.model.BaseEntity;
import com.weddingplanner.common.util.FileUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Generic text-file storage. One entity per line:
 *     id|createdAt|field1|field2|...
 * Lines that are blank or start with '#' are ignored.
 *
 * To use: extend it, pass data dir / file name / id prefix, implement fromFields().
 * You get the CRUD file operations for free:
 *     save(entity)             -> CREATE (id == null) or UPDATE (id exists)
 *     findAll(), findById(id)  -> READ
 *     deleteById(id)           -> DELETE
 */
public abstract class FileRepository<T extends BaseEntity> {

    private final Path filePath;
    private final String idPrefix;

    protected FileRepository(String dataDir, String fileName, String idPrefix) {
        this.filePath = Paths.get(dataDir, fileName);
        this.idPrefix = idPrefix;
        try {
            Files.createDirectories(filePath.toAbsolutePath().getParent());
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new DataFileException("Cannot initialise data file: " + filePath, e);
        }
    }

    /** Rebuild an entity from its OWN fields (same order as toFields()). */
    protected abstract T fromFields(String[] fields);

    // ---------------- READ ----------------

    public synchronized List<T> findAll() {
        List<T> result = new ArrayList<>();
        for (String line : readLines()) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] parts = FileUtil.split(line);
            try {
                T entity = fromFields(Arrays.copyOfRange(parts, 2, parts.length));
                entity.setId(parts[0]);
                entity.setCreatedAt(LocalDateTime.parse(parts[1]));
                result.add(entity);
            } catch (RuntimeException e) {
                throw new DataFileException("Corrupt line in " + filePath.getFileName() + ": " + line, e);
            }
        }
        return result;
    }

    public synchronized Optional<T> findById(String id) {
        for (T item : findAll()) {
            if (item.getId().equals(id)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public synchronized long count() {
        return findAll().size();
    }

    // ---------------- CREATE / UPDATE ----------------

    public synchronized T save(T entity) {
        List<T> all = findAll();
        if (entity.getId() == null) {
            entity.setId(generateId(all));
            entity.setCreatedAt(LocalDateTime.now());
            all.add(entity);
        } else {
            boolean found = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId().equals(entity.getId())) {
                    if (entity.getCreatedAt() == null) {
                        entity.setCreatedAt(all.get(i).getCreatedAt());
                    }
                    all.set(i, entity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new ResourceNotFoundException("No record with id " + entity.getId());
            }
        }
        writeAll(all);
        return entity;
    }

    // ---------------- DELETE ----------------

    public synchronized boolean deleteById(String id) {
        List<T> all = findAll();
        boolean removed = all.removeIf(item -> item.getId().equals(id));
        if (removed) {
            writeAll(all);
        }
        return removed;
    }

    // ---------------- internals ----------------

    private String generateId(List<T> existing) {
        int max = 0;
        for (T item : existing) {
            String id = item.getId();
            if (id != null && id.startsWith(idPrefix)) {
                try {
                    max = Math.max(max, Integer.parseInt(id.substring(idPrefix.length())));
                } catch (NumberFormatException ignored) {
                    // skip ids that are not numeric
                }
            }
        }
        return String.format("%s%04d", idPrefix, max + 1);
    }

    private List<String> readLines() {
        try {
            return Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataFileException("Cannot read " + filePath, e);
        }
    }

    private void writeAll(List<T> items) {
        List<String> lines = new ArrayList<>();
        for (String line : readLines()) {           // keep '#' comment/header lines
            if (line.startsWith("#")) {
                lines.add(line);
            }
        }
        for (T item : items) {
            String[] own = item.toFields();
            String[] all = new String[own.length + 2];
            all[0] = item.getId();
            all[1] = item.getCreatedAt().toString();
            System.arraycopy(own, 0, all, 2, own.length);
            lines.add(FileUtil.join(all));
        }
        try {
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataFileException("Cannot write " + filePath, e);
        }
    }
}
