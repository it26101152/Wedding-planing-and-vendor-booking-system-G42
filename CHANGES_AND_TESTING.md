# Wedding Planner G42 — CRUD update

## Roles
- Admin: User Management, Vendors, Wedding Plans, Bookings, Payments, Guest Lists.
- Customer: view vendors; create, view, update, and delete own wedding plans, bookings, payments, and guests.
- Vendor create/update/delete requires ADMIN on server.
- Customer ownership is checked on server for record update and delete.
- User creation is through `/register`; admin can read/update/delete users at `/users`.

## UI additions
- Edit/Delete actions in module tables.
- Search filter; Vendor category dropdown; booking/wedding date picker; RSVP/payment status dropdown.
- Vendor price sorting button (browser-side bubble sort). This does not prove the repository uses the project's linked-list implementation.

## Run
Open pom.xml in IntelliJ; use Java 17; configure admin environment variables; start WeddingPlannerApplication; use port from logs (usually 8080).

## Important limitations
- This is a code update, not a completed browser regression test.
- Payment refund/invoice generation and full linked-list requirement need separate functional verification.
- Existing text data files are preserved. Back up data before testing deletes.
- This starter application does not include comprehensive CSRF protection or full Spring Security authentication. Do not deploy publicly without a security review.
