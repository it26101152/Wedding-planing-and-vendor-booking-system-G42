# Step 1–5 validation improvements

The five modules now validate submitted values on the server, not only through HTML inputs.

1. Vendors: required name/category/description and nonnegative numeric price (two decimals maximum).
2. Wedding Plans: valid existing customer account, required title/venue and ISO date.
3. Bookings: valid customer and vendor, ISO date and known booking status.
4. Payments: valid customer and booking, matching booking ownership, positive amount and known payment status.
5. Guests: valid customer, guest name/email and known RSVP status.

**Important:** Existing text-file records were not migrated. Historical data with invalid references may require manual cleanup. Deleting linked records can still leave orphaned records; this version does not implement cascade deletion. Back up the `data/` directory before testing.

## Suggested manual checks

- Try blank fields, invalid dates, invalid customer/vendor/booking IDs and negative amounts.
- Try a payment linked to another customer's booking: the server must reject it.
- Check Admin and Customer CRUD, dashboard navigation and persisted text files.
- Re-test editing existing records and the final confirmation workflow.

Build and end-to-end browser execution are required before claiming a fully verified release.
