# Invalid Login Fix (G42)

Changed files:
- src/main/java/com/weddingplanner/user/controller/AuthController.java
- src/main/webapp/WEB-INF/views/auth/login.jsp

When AuthService.login throws IllegalArgumentException for an invalid email/password,
the controller returns the login view with a generic error message instead of an
exception page. Valid ADMIN and COUPLE redirects remain unchanged.

Open the project root containing pom.xml in IntelliJ, restart the Spring Boot app, and
try a wrong password at http://localhost:8081/login (if using port 8081).

Do not replace your own data/users.txt with the one from this archive if it contains
your registered users.
