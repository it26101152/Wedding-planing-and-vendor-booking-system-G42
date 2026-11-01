# Latest fixes and verification notes

- Fixed payment amount validation: up to 12 digits before decimal, up to two meaningful decimal digits; standard amounts like 500000.00 accepted.
- Improved Payments booking picker to disable bookings of other customers in browsers that do not honor hidden options.
- Aligned Payments status options with backend (Pending, Paid, Refunded, Unpaid, Partial).
- New pending-customer wedding form validates title, venue and date before creating pending customer reference.
- Fixed email duplicate check normalization for registration.
- Added ValidationRegressionTests for amount/date/RSVP validation.
- Existing Vendor/Booking delete dependency checks retained.

## Verification limits

Standalone Java validation smoke checks passed. Full Maven test/package and browser end-to-end tests could not be executed in this environment because Maven is not installed. Run IntelliJ Maven Lifecycle > test and package.

## Important remaining limitation

Pending CUST records are not automatically linked to subsequently registered USR accounts. Do not link solely by an unverified email address. An email verification or secure claim flow is required before exposing pending customer records to an account.
