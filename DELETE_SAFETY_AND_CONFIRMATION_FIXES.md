# Delete safety and confirmation fixes

- Vendors with bookings cannot be deleted. Reassign/remove dependent bookings first.
- Bookings with payments or saved confirmations cannot be deleted.
- Completed booking status is now selectable in the booking form.
- Cancelled bookings cannot be confirmed, and Review displays the reason.

## Manual tests
1. Create a vendor and booking; try deleting the vendor (must fail safely).
2. Create a payment for a booking; try deleting the booking (must fail safely).
3. Create a booking with status Completed and save it.
4. Create a Cancelled booking with linked records; Review must not allow confirmation.
5. Verify normal booking confirmation still works when linked records exist.

Full Maven/browser tests should be performed before submission.
