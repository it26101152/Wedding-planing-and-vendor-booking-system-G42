# Customer vendor selection and draft update

- Customers can select a vendor from the Vendors table; selection opens Booking step and preselects vendor ID.
- Booking form lists existing vendors; Payment form lists the signed-in customer's bookings.
- Unsaved forms remain mounted in the wizard while navigating, with sessionStorage drafts for refresh.
- Drafts are browser-session data, not saved records; Save submits to the existing text-file repositories.
- Backend validates that booking vendor IDs exist and that customers only pay for their own booking IDs.
- Admin can still create/edit vendors; customer cannot.
- Test all CRUD and flows locally before deployment.
