# Customer/Admin Login Setup (G42)

One shared login screen: `/login`. Customer registration creates role `COUPLE` only.
Successful login redirects `COUPLE` to `/customer` and `ADMIN` to `/admin`. The admin
dashboard is protected by a server-side role check. Existing modules use role checks;
review authorization in each module before production use.

## First admin account
Set environment variables **before the first run** (do not commit credentials):

Windows PowerShell:
```
$env:WEDDING_ADMIN_EMAIL="admin@example.com"
$env:WEDDING_ADMIN_PASSWORD="choose-a-strong-password"
mvn spring-boot:run
```

The startup helper creates an ADMIN record in `data/users.txt` only if no ADMIN already
exists. It hashes the password using BCrypt. It does not overwrite an existing admin. Do
not share the resulting users.txt file with real account data.

## Customer account
Go to `/register` and register normally. Then log in at `/login` with either account.
Logout uses POST `/logout`.

## IntelliJ
Open the directory containing `pom.xml`, use JDK 17, and run
`WeddingPlannerApplication`. For first admin setup, add both environment variables to
the IntelliJ run configuration.

## Notes
This update keeps the previous integrated starter project structure and adds admin
bootstrap and distinct dashboard JSP pages. It is not a fully tested production system.
Do not push generated files over teammates' branch work without reviewing differences.
Maven build was not verified in this environment.
