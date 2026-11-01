# Main Admin and Admin Management

- The oldest existing ADMIN account is the permanent Main Admin (based on creation timestamp). No existing user IDs or text-file format are changed.
- Main Admin can create new ADMIN accounts from Admin > User Management, update other admins, and delete other admins.
- Main Admin can update their own name, email, and password, but cannot be deleted.
- Other admins can update their own name, email, and password, but cannot edit/delete/create other admins.
- Both admin types retain customer-management privileges.
- Admin account passwords require 12+ characters and are BCrypt hashed.
- Register still creates COUPLE accounts only.
- Permission checks are enforced in UserManagementController, not just hidden in the JSP.
- IMPORTANT: Back up data/users.txt before testing. Keep the existing main admin record.
- Run from the project root so app.data.dir points to the expected data folder.
- Test creating a secondary admin, its login, its limited UI, and main-admin delete protection.
- This update does not introduce a new SUPER_ADMIN role string; the oldest ADMIN record is designated main admin.
