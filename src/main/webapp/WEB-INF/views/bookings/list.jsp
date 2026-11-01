<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bookings | Wedding Planner G42</title>
    <style>
/* Finalization: keyboard navigation, responsive forms and readable long values */
:focus-visible{outline:3px solid #963b73;outline-offset:2px}input,select,textarea{max-width:100%;box-sizing:border-box}td,th{overflow-wrap:anywhere}button{cursor:pointer}@media(max-width:640px){body{padding:12px!important}main,.wrap,.panel{max-width:100%;box-sizing:border-box}table{display:block;overflow-x:auto;max-width:100%}}

        body { margin:0; background:#faf7f8; font:16px Arial,sans-serif; color:#302b34; }
        .wrap { max-width:1150px; margin:36px auto; padding:18px; }
        .panel { background:#fff; padding:26px; border-radius:16px; box-shadow:0 5px 20px #0001; }
        h1 { color:#8b3560; }
        .btn,button { display:inline-block; border:0; border-radius:8px; background:#963b73; color:white;
                       padding:11px 15px; cursor:pointer; text-decoration:none; font-size:14px; }
        .secondary { background:#6d6470; }
        .danger { background:#a32940; }
        .toolbar { display:flex; gap:12px; align-items:center; flex-wrap:wrap; margin:18px 0; }
        form.grid { display:grid; grid-template-columns:repeat(auto-fit,minmax(180px,1fr)); gap:12px; margin:20px 0; }
        label { display:flex; flex-direction:column; gap:7px; font-weight:600; }
        input,select { box-sizing:border-box; width:100%; padding:11px; border:1px solid #ddd; border-radius:8px; font:inherit; }
        .table-wrap { overflow-x:auto; }
        table { width:100%; border-collapse:collapse; margin-top:12px; }
        th,td { white-space:nowrap; overflow-wrap:normal; padding:12px; border-bottom:1px solid #eee; text-align:left; }
        .actions { white-space:nowrap; }
        .actions form { display:inline; }
        .actions button { padding:8px 10px; margin:2px; }
        .hint { color:#665c66; }
    </style>
</head>
<body>
<div class="wrap"><div class="panel">
    <h1>Bookings</h1>
    <div class="toolbar">
        
        <c:if test="${admin}"><input style="max-width:290px" id="search" type="search" placeholder="Search records..." oninput="filterRecords()"></c:if>
        
    </div>
    
    <h2 id="formHeading">Create Bookings</h2>
    <form class="grid" id="recordForm" method="post" action="/bookings/save">
        <input type="hidden" name="id" id="f_id">
        <c:if test="${admin}"><label>Select Customer (ID or Name)
<select name="customerId" id="f_customerId" required><option value="">Choose a registered customer</option><c:forEach var="u" items="${customers}"><option value="${u.id}"><c:out value="${u.id}"/> - <c:out value="${u.name}"/> (<c:out value="${u.email}"/>)</option></c:forEach><c:forEach var="pc" items="${pendingCustomers}"><option value="${pc.id}"><c:out value="${pc.id}"/> - <c:out value="${pc.name}"/> (<c:out value="${pc.email}"/>) [Pending]</option></c:forEach></select></label></c:if>
<c:choose><c:when test="${admin}"><label>Select Vendor<select name="vendorId" id="f_vendorId" required><option value="">Choose a vendor</option><c:forEach var="v" items="${vendors}"><option value="${v.id}"><c:out value="${v.id}"/> - <c:out value="${v.name}"/> - Rs. <c:out value="${v.price}"/></option></c:forEach></select></label></c:when>
<c:otherwise><label>Select Vendor<select name="vendorId" id="f_vendorId" required><option value="">Choose a vendor</option><c:forEach var="v" items="${vendors}"><option value="${v.id}"><c:out value="${v.name}"/> — <c:out value="${v.category}"/> — Rs. <c:out value="${v.price}"/> (<c:out value="${v.id}"/>)</option></c:forEach></select></label></c:otherwise></c:choose>
<label>eventDate<input type="date" name="eventDate" id="f_eventDate" placeholder="eventDate" required></label>
<label>status<select name="status" id="f_status" required><option value="">Select status</option><option value="Pending">Pending</option><option value="Confirmed">Confirmed</option><option value="Cancelled">Cancelled</option><option value="Completed">Completed</option></select></label>
        <div><button type="submit">Save</button> <button class="secondary" type="button" onclick="clearForm()">Clear</button></div>
    </form>
    
    <h2>Records</h2>
    <div class="table-wrap"><table id="recordsTable">
        <thead><tr><th>ID</th><th>customerId</th><th>vendorId</th><th>eventDate</th><th>status</th><th>Actions</th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${records}">
            <tr><td><c:out value="${r.id}"/></td><td><c:out value="${r.customerId}"/></td><td><c:out value="${r.vendorId}"/></td><td><c:out value="${r.eventDate}"/></td><td><c:out value="${r.status}"/></td>
                <td class="actions">
                    <c:if test="${true}">
                        <button type="button" class="secondary" onclick="editRow(this)">Edit</button>
                        <form method="post" action="/bookings/delete" onsubmit="return confirm('Delete this record?')">
                            <input type="hidden" name="id" value="${r.id}">
                            <button class="danger" type="submit">Delete</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table></div>
</div></div>
<script>
    function setValue(name, value) {
        const field = document.getElementById('f_' + name);
        if (field) field.value = value;
    }
    function editRow(button) {
        const cells = button.closest('tr').cells;
        setValue('id', cells[0].textContent.trim());
        setValue('customerId', cells[1].textContent.trim());
        setValue('vendorId', cells[2].textContent.trim());
        setValue('eventDate', cells[3].textContent.trim());
        setValue('status', cells[4].textContent.trim());
        document.querySelector('#recordForm button[type=submit]').textContent = 'Update';
        document.getElementById('formHeading').textContent = 'Edit Bookings';
        document.getElementById('recordForm').scrollIntoView({behavior:'smooth'});
    }
    function clearForm() {
        document.getElementById('recordForm').reset();
        setValue('id', '');
        document.querySelector('#recordForm button[type=submit]').textContent = 'Save';
        document.getElementById('formHeading').textContent = 'Create Bookings';
    }
    function customerSearchText(id) {
        const selector = document.getElementById('f_customerId');
        if (!selector) return '';
        const option = Array.from(selector.options).find(o => o.value === id);
        return option ? option.textContent.toLowerCase() : '';
    }
    function filterRecords() {
        const searchField = document.getElementById('search');
        if (!searchField) return;
        const q = searchField.value.toLowerCase();
        document.querySelectorAll('#recordsTable tbody tr').forEach(row => {
            row.hidden = !(row.textContent.toLowerCase() + ' ' + customerSearchText(row.cells[1].textContent.trim())).includes(q);
        });
    }
    
</script>
</body>
</html>
