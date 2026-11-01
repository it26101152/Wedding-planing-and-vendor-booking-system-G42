# Admin role management update

- Main Admin only: Promote registered COUPLE accounts to ADMIN, or remove ADMIN role from other admins.
- New admins register with their own passwords; no admin password entry or email invitations.
- Main Admin account cannot be deleted or demoted.
- Other admins can update their own profile, but cannot change others' roles.
- Session roles are synchronized with saved user records on each HTTP request. A newly promoted user should log out and log back in to reach the admin dashboard.
- Existing users.txt data is retained. No data migration needed.
- **Test:** Register a test customer, promote as Main Admin, log in as promoted admin; check no role controls. Demote and verify admin URL access is denied. Test Main Admin protection.
