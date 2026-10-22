package com.weddingplanner.common.datastructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Hand-written singly linked list (not java.util.LinkedList) used to hold vendor
 * details dynamically. Owner: Vendor Management member.
 */
public class CustomLinkedList<T> implements Iterable<T> {

    public static class Node<T> {
        private T data;
        private Node<T> next;

        Node(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void addFirst(T data) {
        Node<T> node = new Node<>(data);
        node.next = head;
        head = node;
        if (tail == null) {
            tail = node;
        }
        size++;
    }

    public void addLast(T data) {
        Node<T> node = new Node<>(data);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public T get(int index) {
        return nodeAt(index).data;
    }

    public void set(int index, T data) {
        nodeAt(index).data = data;
    }

    public T removeAt(int index) {
        checkIndex(index);
        T removed;
        if (index == 0) {
            removed = head.data;
            head = head.next;
            if (head == null) {
                tail = null;
            }
        } else {
            Node<T> prev = nodeAt(index - 1);
            Node<T> target = prev.next;
            removed = target.data;
            prev.next = target.next;
            if (target == tail) {
                tail = prev;
            }
        }
        size--;
        return removed;
    }

    /** Removes the first element equal to the given one. */
    public boolean remove(T data) {
        int i = indexOf(data);
        if (i < 0) {
            return false;
        }
        removeAt(i);
        return true;
    }

    public int indexOf(T data) {
        int i = 0;
        for (Node<T> cur = head; cur != null; cur = cur.next, i++) {
            if (cur.data == null ? data == null : cur.data.equals(data)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(T data) {
        return indexOf(data) >= 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    /** Needed by BubbleSort to walk the nodes. */
    public Node<T> getHead() {
        return head;
    }

    public List<T> toList() {
        List<T> list = new ArrayList<>();
        for (T item : this) {
            list.add(item);
        }
        return list;
    }

    public static <T> CustomLinkedList<T> fromList(List<T> items) {
        CustomLinkedList<T> list = new CustomLinkedList<>();
        for (T item : items) {
            list.addLast(item);
        }
        return list;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> cur = head;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public T next() {
                if (cur == null) {
                    throw new NoSuchElementException();
                }
                T d = cur.data;
                cur = cur.next;
                return d;
            }
        };
    }

    private Node<T> nodeAt(int index) {
        checkIndex(index);
        Node<T> cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + ", size " + size);
        }
    }
}
