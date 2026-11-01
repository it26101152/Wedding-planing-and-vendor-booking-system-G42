# Session restart and Super Admin update

- `/admin` and `/customer` now redirect to `/login?expired=1` when the login session is missing.
- `IllegalArgumentException` messages are handled in a readable error page rather than a generic server error.
- The User Management JSP no longer depends on JSTL functions for escaping values; it uses JSTL core `c:out`.
- The existing main-admin permission checks remain in `UserManagementController`.
- Main Admin cannot be deleted.

## Test
1. Back up your working project and its `data/` folder.
2. Run the project, log in as Main Admin, open User Management and add a test Admin (password 12+ characters).
3. Log in with the new Admin in a separate Chrome profile. Confirm they cannot create/edit/delete other Admins.
4. Stop and restart Spring Boot, then reload `/admin` or `/customer`. Expect a login page with a session-expired notice.
5. Log in again and confirm records remain in the `data/` files.

## Verification limitation
No Maven executable/dependency cache is installed in the authoring environment. Compile and browser tests are still required in IntelliJ. If the User Management screen still errors, send the full IntelliJ exception including `Caused by:`.
