<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Wedding Planner G42</title>
        <style>
/* Finalization: keyboard navigation, responsive forms and readable long values */
:focus-visible{outline:3px solid #963b73;outline-offset:2px}input,select,textarea{max-width:100%;box-sizing:border-box}td,th{overflow-wrap:anywhere}button{cursor:pointer}@media(max-width:640px){body{padding:12px!important}main,.wrap,.panel{max-width:100%;box-sizing:border-box}table{display:block;overflow-x:auto;max-width:100%}}

            body {
                font-family:Arial,sans-serif;
                background:#faf7f8;
                color:#302b34;
                margin:0
            }
            .wrap {
                max-width:1050px;
                margin:45px auto;
                padding:25px
            }
            h1 {
                color:#8b3560
            }
            .panel {
                background:white;
                border-radius:16px;
                padding:25px;
                box-shadow:0 5px 20px #0001
            }
            a,button {
                background:#9c3e70;
                color:white;
                padding:12px 18px;
                border:0;
                border-radius:8px;
                text-decoration:none;
                display:inline-block;
                margin:6px;
                cursor:pointer
            }
            input,select {
                padding:12px;
                border:1px solid #ddd;
                border-radius:8px;
                margin:6px
            }
            table {
                width:100%;
                border-collapse:collapse
            }
            td,th {
                padding:12px;
                border-bottom:1px solid #eee;
                text-align:left
            }
            </style>
        </head>
        <body>
            <div class="wrap">
                <div class="panel">
                    <h1>Wedding Planner | Login
                </h1>
                <% if (request.getParameter("expired") != null) { %>
                <div role="alert" style="color:#713b12;background:#fff4dc;padding:12px;border-radius:8px;margin:12px 0;">
                    Your session has expired. Please log in again.
                </div>
                <% } %>
                <% if (request.getParameter("denied") != null) { %>
                <div role="alert" style="color:#713b12;background:#fff4dc;padding:12px;border-radius:8px;margin:12px 0;">
                    Admin access is required. Please log in with an administrator account.
                </div>
                <% } %>
                <% if (request.getAttribute("loginError") != null) { %>
                <div role="alert" style="color:#a61b32;background:#fff0f1;padding:12px;border-radius:8px;margin:12px 0;">
                    <%= request.getAttribute("loginError") %>
                </div>
                <% } %>
                <form method="post" action="/login">
                    <input name="email" type="email" placeholder="Email" required>
                    <input name="password" type="password" placeholder="Password" required>
                    <button>Login
                </button>
            </form>
            <p>New customer?
            <a href="/register">Register
        </a>
    </p>
</div>
</div>
</body>
</html>
