<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
                    <h1>Customer Registration
                </h1>
                <c:if test="${not empty registerError}"><p role="alert" style="color:#9d3445;background:#fff1f2;padding:12px;border-radius:8px"><c:out value="${registerError}"/></p></c:if>
                <p>Password must contain at least 12 characters.</p>
                <form method="post" action="${pageContext.request.contextPath}/register">
                    <input name="name" placeholder="Name" maxlength="100" value="<c:out value="${enteredName}"/>" required>
                    <input name="email" type="email" placeholder="Email" value="<c:out value="${enteredEmail}"/>" required>
                    <input name="password" type="password" minlength="12" placeholder="Password (at least 12 characters)" required>
                    <button>Register
                </button>
            </form>
            <a href="${pageContext.request.contextPath}/login">Back to login
        </a>
    </div>
</div>
</body>
</html>
