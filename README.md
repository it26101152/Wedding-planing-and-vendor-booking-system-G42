# Wedding Planner G42 — GitHub-compatible starter integration

Original uploaded `main/java`, `main/resources`, and `main/webapp` were mapped to
`src/main/`. Existing common utilities and data repository are retained. Java 17 /
Spring Boot 3 / JSP / text files.

## Run
Install Java 17+ and Maven. From project root run `mvn spring-boot:run` and open
http://localhost:8080.

## Roles
Customers can self-register (COUPLE). ADMIN login requires a provisioned admin record in
users.txt; do not publicly enable admin registration. User records are encoded using
existing FileUtil serialization and passwords must be BCrypt hashes.

## Limitations before submission
This is an integrated **development scaffold**, not a production-ready application. It
has basic CRUD, but needs authorization checks for customer ownership on
READ/UPDATE/DELETE, field validation, CSRF protection, booking conflicts, payment
validation, and integration testing. Avoid real customer data. Admin account bootstrap
is not included. Maven build could not be run in this environment.
