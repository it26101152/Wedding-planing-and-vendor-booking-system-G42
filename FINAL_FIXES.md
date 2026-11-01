# Dashboard and CRUD fixes

- Removed redundant Admin dashboard and Customer dashboard links from embedded CRUD pages.
- Search records is only visible for admin in Vendors, Bookings, Payments, Guests and Wedding Plans.
- Corrected edit-row field indexes for Vendors, Bookings, Payments, and Guests.
- Existing empty-ID handling in save controllers retained.
- Admin and Customer dashboards remember the last selected module/step within a browser tab.
- Existing ownership checks for customer-owned records remain in the controllers.

## Manual testing required

1. Stop the old application and open this ZIP as a new project.
2. Start Spring Boot and sign in as admin. Check all six tabs, add/edit/delete and search.
3. Sign in as customer. Check six steps, add/edit/delete own records, and confirm a booking.
4. Confirm a customer cannot edit or delete another customer's records.
5. Restart and confirm records persist in data/*.txt.

This package has not been verified through a full Maven build or browser test in this environment.
