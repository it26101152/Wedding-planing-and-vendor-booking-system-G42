# Role-change fix and automated tests

Existing Wedding, Booking, Payment, and Guest records remain editable if their original customer is later promoted to ADMIN. The exception applies only when the record already exists and its customerId is unchanged. Newly created records still require a COUPLE customer account. This does not grant an ADMIN the customer dashboard or permission to create customer records.

Five JUnit 5/Mockito service tests are in `src/test/java/com/weddingplanner/integration/FiveModuleServiceTests.java` and cover vendor validation and delete safety, wedding updates after promotion, booking references and delete safety, payment matching and amounts, and guest RSVP validation.

## Run locally

With Java 17 and Maven installed, in the project folder run:

```
mvn clean test
mvn package
```

Then run the application and manually verify Admin/Customer login, vendor search and sorting, CRUD in all five modules, and the Review & Confirm workflow. Unit tests do not substitute for browser end-to-end tests. Back up the `data` directory before running destructive tests. Maven/browser execution was not available in the artifact creation environment.
