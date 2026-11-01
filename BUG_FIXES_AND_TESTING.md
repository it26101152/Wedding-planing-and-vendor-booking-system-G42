# Bug fixes and manual test checklist

- Customer View / Select now opens one selected-customer panel with Update Customer, Delete Customer and (Main Admin only) Promote to Admin actions. No inline edit/delete buttons remain in the customer list.
- View / Select no longer assumes Main Admin-only hidden fields exist; non-main Admin users can view customer details without a JavaScript null-reference error.
- Registration validation errors stay on the registration form, retaining name/email, with an explicit 12-character password hint. Password is never retained.
- Generic error page provides Go Back, Login and Home links. Existing session expiry redirects remain unchanged.

## Manual tests (not yet verified in a browser)
1. Register with a short password: inline error, not a separate error page.
2. Register with 12+ characters: customer dashboard.
3. Register duplicate email: inline error.
4. Main Admin: search customer, click View / Select, edit name/email and Update.
5. Main Admin: click View / Select, Delete, cancel confirmation, then confirm on test account.
6. Main Admin: click View / Select, Promote to Admin, verify Admin List.
7. Other Admin: click View / Select, verify customer details display but no edit/delete/promote actions.
8. Attempt direct POST to /users/update or /users/delete targeting ADMIN: backend must reject.
9. Restart application and refresh dashboard: redirect to login.
10. Test Vendor, Wedding, Booking, Payment and Guest CRUD, plus customer confirmation end-to-end.

**Note:** Browser and Maven tests have not been completed in this environment.
