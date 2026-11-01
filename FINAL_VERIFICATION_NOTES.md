# G42 fixes and verification

## Changes
- Booking IDs now use a persistent high-water sequence file (`data/bookings.txt.sequence`), preventing ID reuse when the last booking is deleted or the app restarts. This sequence logic is implemented in the generic file repository, so all modules benefit.
- Vendor search is visible to customers as well as admins. Existing bubble-sort-by-price code remains intact.
- Booking delete checks and Customer Confirmation both use `app.data.dir` for the confirmation file, avoiding mismatches with customized data locations.
- Added `PersistenceAndBookingIdTests.java` for ID uniqueness and five-module save/reload verification.
- Retained existing customer-to-admin record-update fix, delete safety and five-module service tests.

## Verified here
- Compiled repository/model/util Java classes with Java `javac` and lightweight annotation stubs: PASS.
- Ran standalone Java repository test using a new temporary data directory: PASS. Booking IDs BKG0001, BKG0002, BKG0003 remained distinct after deleting BKG0002 and reconstructing repository; records for Vendor, Wedding, Booking, Payment and Guest survived reconstruction and linked IDs matched.
- Vendor JavaScript parsed successfully with `node --check`.

## Not verified here
- Maven/Spring Boot application build and JUnit execution (Maven unavailable in this environment).
- Real browser clicks, login/session behavior and full customer end-to-end workflow (no running browser/server available).

## Run locally
1. Back up your original `data` directory and copy it into this project if necessary.
2. Open project in IntelliJ, use JDK 17.
3. Run `mvn clean test` then `mvn spring-boot:run`.
4. Log in as a customer and test Wedding > Vendor > Booking > Payment > Guest > Confirmation. Confirm payment booking ID matches the chosen booking.
5. Delete the latest unused booking, create another and verify the new ID differs. Restart and repeat.
6. Vendor search should be available to customers; sort price ascending and confirm row order.

Note: `.sequence` files must be retained with your data folder to preserve IDs across restarts/deletions. Existing data files are included unchanged.
