# Wedding Planner G42 — Finalization Guide

## Project overview
Spring Boot 3 / Java 17 / JSP wedding planning application using text-file persistence. Six modules: users, vendors, weddings, bookings, payments and guest RSVP. The permanent Main Admin can promote registered customers to Admin; other admins cannot manage admin roles.

## What this finalization pass changed
- Improved customer/admin table legibility, long-email wrapping and horizontal overflow.
- Added visible keyboard focus indicators and responsive form sizing on JSP pages.
- Added server-side name/email checks at registration and a consistent 12-character minimum password requirement.
- Added friendly messages for missing/invalid form parameters and unexpected errors (without showing stack traces in the UI).
- Kept the live customer search, suggestions, customer selection, separate admin list, role changes, session repair and existing six modules.

## Setup
1. Open the folder containing `pom.xml` in IntelliJ IDEA.
2. Use JDK 17 and import the Maven project.
3. Set environment variables `WEDDING_ADMIN_EMAIL` and `WEDDING_ADMIN_PASSWORD` for the initial administrator (see ROLE_LOGIN_SETUP.md).
4. Run `WeddingPlannerApplication` and open `http://localhost:8080`.
5. Back up the `data/` directory before importing an older dataset or testing deletes.

## Acceptance test checklist (manual, not executed here)
- [ ] Register a customer with a valid email and password >=12 characters.
- [ ] Invalid email, blank name, duplicate email and short password are rejected.
- [ ] Customer dashboard shows six steps; booking uses a valid vendor and payment a valid booking.
- [ ] Main Admin sees all customers without typing; typing partial name/email narrows matches and offers suggestions.
- [ ] Long email addresses remain readable; selecting a customer displays the correct identity.
- [ ] Main Admin promotes a customer; after fresh login they reach Admin Dashboard.
- [ ] Other admins cannot promote/demote; Main Admin cannot be deleted or demoted.
- [ ] Main Admin demotes another admin; after fresh login they reach Customer Dashboard.
- [ ] CRUD operations for vendors, wedding plans, bookings, payments and guests work with valid data.
- [ ] Invalid IDs, missing inputs and expired sessions show a friendly response or login redirect.
- [ ] Check mobile width and keyboard navigation for each screen.

## Important limits
This ZIP has been checked for archive integrity and source structure, but Maven compilation, browser workflows and all acceptance tests have **not** been verified in this environment. Do not treat the checklist as passed. This is a course/demo application; review production security requirements (CSRF protection, authorization, storage security and password-reset flow) before any public deployment.
