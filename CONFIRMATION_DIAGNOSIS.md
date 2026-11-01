# Confirmation diagnosis (uploaded project data)

- Existing booking: BKG0002, vendor VEN0001, event date 2026-10-30, customer USR0002.
- Vendor VEN0001 exists. Wedding plan WED0002 has matching date 2026-10-30. A guest exists.
- Payments PAY0001 and PAY0002 reference BKG001 and BKG0001, respectively, NOT BKG0002.
- To confirm BKG0002, use Step 5 Payments to create a payment linked to BKG0002, or edit an existing payment to select BKG0002 **only if it truly belongs to that booking**.
- No payment data has been automatically reassigned.
- Confirmation messages now specify which record is missing and the required booking ID.
- Build and browser testing were not available in this environment.
