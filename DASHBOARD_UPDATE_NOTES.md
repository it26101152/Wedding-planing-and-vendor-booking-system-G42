# G42 Dashboard Update

Admin `/admin`: six clickable module tabs; the existing CRUD pages load inside the dashboard using an iframe.

Customer `/customer`: six clickable steps with Back/Next and a review acknowledgment button. Existing saved records are managed in the embedded module pages. The final button does not submit all six modules in one transaction, and it does not verify that every field is complete.

All original Java controllers, repositories, JSP CRUD pages, and data text files are preserved. Existing data is included in this ZIP: protect it if it contains personal information.

Start with JDK 17 and the existing README and ROLE_LOGIN_SETUP instructions. If you have previously started the application on port 8081, check application.properties for the configured port.

Note: embedded CRUD pages may navigate within their iframe; the dashboard remains on the same page.
