# Customer confirmation update

The final Customer Dashboard step now loads the logged-in customer's existing bookings and validates linked records on the server. Selecting OK - Confirm persists a confirmation to `data/confirmations.txt` (customer ID, booking ID, timestamp). Duplicate confirmations are prevented for the same customer and booking.

Validation requires: a wedding plan with the same event date, a vendor referenced by the booking, a payment record linked to that booking, and at least one guest. Missing requirements are shown in Review & Confirm. Admin accounts cannot use this endpoint.

Important: This does **not** implement a single atomic six-step transaction, process real payments, verify payment status, or reserve vendor inventory. The existing module forms save their own records independently. Confirmation is a separate acknowledgement of those saved records. Deleting or changing source records after confirmation does not revoke it.

Test with a registered customer: save a wedding plan, select an existing vendor ID, save a booking with matching event date, save a payment referencing the booking ID, add a guest, then confirm from step 6. Restart the application and check `data/confirmations.txt`. Test a missing payment and another customer's booking ID as negative cases.
