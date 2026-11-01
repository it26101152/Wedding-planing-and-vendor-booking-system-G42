# Admin New Customer + Wedding Plan

On Admin > Wedding Plans, choose `+ New Customer (no account/password)` in the existing customer selector. Enter name, email and phone, plus wedding details, then Save. The application creates a `CUST####` reference in `data/pending_customers.txt` and a `WED####` record in `data/weddings.txt`. The customer has **no login account** and no password is created.

Subsequent Admin > Bookings, Payments and Guest List & RSVP forms include these pending `CUST####` references in their existing customer selectors. Select the same reference to link records. Payments must reference a booking belonging to the same customer.

Registered `USR####` customers and the existing Customer Dashboard workflow are unchanged. This version **does not automatically transfer pending CUST records to a newly registered USR account**. That requires verified email ownership and an explicit safe account-claim process; matching an unverified email string alone is unsafe. Pending records remain admin-managed until such a workflow is implemented.

Test in IntelliJ: Maven Lifecycle > clean, test, package. Then run the application and test a new pending customer with wedding plan, booking, payment and guest. Back up the existing `data/` directory before replacing the project.
