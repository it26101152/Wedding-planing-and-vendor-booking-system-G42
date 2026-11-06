<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Vendors | Wedding Planner G42</title>
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
        th,td { padding:12px; border-bottom:1px solid #eee; text-align:left; }
        .actions { white-space:nowrap; }
        .actions form { display:inline; }
        .actions button { padding:8px 10px; margin:2px; }
        .hint { color:#665c66; }
    </style>
</head>
<body>
<div class="wrap"><div class="panel">
    <h1>Vendors</h1>
    <div class="toolbar">
        
        <input style="max-width:290px" id="search" type="search" placeholder="Search vendors..." oninput="filterRecords()">
        <button class="secondary" type="button" onclick="window.location.href='/vendors?sort=price'">Sort by price</button>
    </div>
    <c:if test="${admin}">
    <h2 id="formHeading">Add Vendor</h2>
    <form class="grid" id="recordForm" method="post" action="/vendors/save">
        <input type="hidden" name="id" id="f_id">
        <label>name<input type="text" name="name" id="f_name" placeholder="name" required></label>
<label>category<select name="category" id="f_category" required><option value="">Select category</option><option value="Photography">Photography</option><option value="Videography">Videography</option><option value="Catering">Catering</option><option value="Decoration">Decoration</option><option value="Wedding Planning">Wedding Planning</option><option value="Makeup & Beauty">Makeup & Beauty</option><option value="Music & DJ">Music & DJ</option><option value="Wedding Venues">Wedding Venues</option><option value="Transport">Transport</option><option value="Wedding Cakes">Wedding Cakes</option></select></label>
<label>price<input type="number" name="price" id="f_price" placeholder="price" min="0" step="0.01" required></label>
<label>description<input type="text" name="description" id="f_description" placeholder="description" required></label>
        <div><button type="submit">Save</button> <button class="secondary" type="button" onclick="clearForm()">Clear</button></div>
    </form>
    </c:if>
    <h2>Records</h2>
    <div class="table-wrap"><table id="recordsTable">
        <thead><tr><th>ID</th><th>name</th><th>category</th><th>price</th><th>description</th><th>Actions</th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${records}">
            <tr><td><c:out value="${r.id}"/></td><td><c:out value="${r.name}"/></td><td><c:out value="${r.category}"/></td><td><c:out value="${r.price}"/></td><td><c:out value="${r.description}"/></td>
                <td class="actions">
                    <c:if test="${!admin}"><button type="button" onclick="chooseVendor(this)">Select Vendor</button></c:if>
                    <c:if test="${admin}">
                        <button type="button" class="secondary" onclick="editRow(this)">Edit</button>
                        <form method="post" action="/vendors/delete" onsubmit="return confirm('Delete this record?')">
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
    function chooseVendor(button) {
        const cells=button.closest('tr').cells;
        const selected={id:cells[0].textContent.trim(),name:cells[1].textContent.trim(),price:cells[3].textContent.trim()};
        try { window.parent.postMessage({type:'g42-vendor-selected',vendor:selected},window.location.origin); } catch(e) {}
        button.textContent='Selected ✓';
    }
    function setValue(name, value) {
        const field = document.getElementById('f_' + name);
        if (field) field.value = value;
    }
    function editRow(button) {
        const cells = button.closest('tr').cells;
        setValue('id', cells[0].textContent.trim());
        setValue('name', cells[1].textContent.trim());
        setValue('category', cells[2].textContent.trim());
        setValue('price', cells[3].textContent.trim());
        setValue('description', cells[4].textContent.trim());
        document.getElementById('formHeading').textContent = 'Edit Vendors';
        document.getElementById('recordForm').scrollIntoView({behavior:'smooth'});
    }
    function clearForm() {
        document.getElementById('recordForm').reset();
        setValue('id', '');
        document.getElementById('formHeading').textContent = 'Create Vendors';
    }
    function filterRecords() {
        const searchField = document.getElementById('search');
        if (!searchField) return;
        const q = searchField.value.toLowerCase();
        document.querySelectorAll('#recordsTable tbody tr').forEach(row => {
            row.hidden = !row.textContent.toLowerCase().includes(q);
        });
    }
    function sortByPrice() { const body=document.querySelector("#recordsTable tbody"); const rows=Array.from(body.rows); for(let i=0;i<rows.length;i++){for(let k=0;k<rows.length-i-1;k++){if(Number(rows[k].cells[3].textContent)>Number(rows[k+1].cells[3].textContent)){body.insertBefore(rows[k+1],rows[k]); [rows[k],rows[k+1]]=[rows[k+1],rows[k]];}}}}
</script>
</body>
</html>
