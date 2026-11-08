<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>User Management</title>
<style>
body{font:16px Arial;background:#faf7f8;padding:24px;color:#302630}
main{background:white;padding:28px;border-radius:15px;max-width:1200px;margin:auto}
h1,h2{color:#963b73}table{width:100%;border-collapse:collapse;min-width:680px}thead{background:#f8edf4}tbody tr:hover{background:#fff7fb}
th,td{padding:12px;border-bottom:1px solid #ddd;text-align:left;vertical-align:top}
input{padding:9px;margin:4px;max-width:230px;box-sizing:border-box}
button{padding:10px 14px;background:#963b73;color:white;border:0;border-radius:7px;cursor:pointer}
button:focus-visible,input:focus-visible{outline:3px solid #602b55;outline-offset:2px}button.danger{background:#9d3445}form{display:inline-block;margin:3px}
.panel{background:#faf1f6;border:1px solid #ead5e2;border-radius:12px;padding:16px;margin:18px 0}
.search-box{width:100%;max-width:520px!important;border:1px solid #d6b7c9;border-radius:8px;font-size:16px}.section-title{margin-top:30px}.empty-message{display:none;padding:18px;color:#6d626b}.note{color:#6d626b;font-size:14px}.pill{font-size:12px;background:#eee;padding:4px 7px;border-radius:10px}
[hidden]{display:none!important}.selected-panel{margin-top:18px;background:#fff;border:1px solid #d8b3c9;padding:18px;border-radius:10px}
button.secondary{background:#716a70}.selected-actions{display:flex;flex-wrap:wrap;align-items:end;gap:10px}.selected-actions label{display:flex;flex-direction:column;gap:4px}.selected-actions input{max-width:340px;width:100%}.customer-edit{margin:8px 0}.customer-edit summary{cursor:pointer;color:#963b73}.customer-edit form{display:flex;flex-wrap:wrap;gap:5px}.stats{display:flex;gap:14px;flex-wrap:wrap}.stat{background:#faf1f6;padding:15px 22px;border-radius:10px;flex:1;min-width:160px}.stat strong{font-size:25px;color:#963b73}.suggestions{background:white;border:1px solid #dfc7d5;border-radius:9px;margin:8px 0;max-width:720px;max-height:190px;overflow-y:auto}.suggestion{display:block;width:100%;text-align:left;background:white;color:#302630;border-bottom:1px solid #eee;border-radius:0;padding:10px 12px;overflow-wrap:anywhere}.suggestion:hover,.suggestion:focus{background:#f8eaf2}.email-full{overflow-wrap:anywhere;word-break:break-word;max-width:430px}.table-scroll{overflow-x:auto;width:100%}.admin-email{overflow-wrap:anywhere;word-break:break-word;min-width:170px}.customer-row.selected{background:#faf1f6}#customerTable td{word-break:break-word}#customerTable button{min-width:90px}
@media(max-width:650px){main{padding:12px}th,td{padding:7px;font-size:13px}input{max-width:100%}}
</style></head><body><main>
<h1>User Management</h1>
<p>View all administrators and registered customers. All administrators can update or delete customers. Only the Main Admin can promote customers to Admin. Administrator accounts are read-only here.</p>
<div class="stats"><div class="stat">Total Admins<br><strong id="adminCount">0</strong></div><div class="stat">Total Customers<br><strong id="customerCount">0</strong></div></div>
<section class="panel" aria-label="Customer email search">
<h2>Customer Search</h2>
<p class="note">All registered customers are listed below. Type part of a name or email to filter the list and see suggestions.</p>
<label for="customerSearch">Customer email or name</label>
<input id="customerSearch" class="search-box" type="search" placeholder="Enter email or customer name..." autocomplete="off" oninput="filterCustomers()">
<div id="suggestions" class="suggestions" hidden aria-label="Matching customer suggestions"></div>
<p class="note" id="searchCount" aria-live="polite"></p>
<div id="searchResults">
<h2>Registered Customers &amp; Details</h2>
<div class="table-scroll">
<table id="customerTable"><thead><tr><th>Customer ID</th><th>Name</th><th>Full Email</th><th>Role</th><th>Actions</th></tr></thead><tbody>
<c:forEach var="u" items="${records}"><c:if test="${u.role eq 'COUPLE'}">
<tr class="customer-row" data-id="<c:out value="${u.id}"/>" data-name="<c:out value="${u.name}"/>" data-email="<c:out value="${u.email}"/>"><td><c:out value="${u.id}"/></td><td><c:out value="${u.name}"/></td><td class="email-full"><c:out value="${u.email}"/></td><td>COUPLE</td><td><button type="button" class="select-customer" onclick="selectCustomer(this)">View / Select</button>
</td></tr>
</c:if></c:forEach>
</tbody></table></div>
<p id="noCustomers" class="note" hidden>No matching customers found.</p>
</div>
<div id="selectedCustomer" class="selected-panel" hidden>
<h2>Selected Customer Details</h2>
<p><strong>Customer ID:</strong> <span id="selectedCustomerId"></span></p>
<p><strong>Name:</strong> <span id="selectedName"></span></p>
<p><strong>Full Email:</strong> <span id="selectedEmail" class="email-full"></span></p>
<p><strong>Role:</strong> COUPLE</p>
<h3>Update Customer</h3>
<form id="selectedUpdateForm" method="post" action="${pageContext.request.contextPath}/users/update" class="selected-actions">
<input type="hidden" id="updateCustomerId" name="id">
<label>Full Name <input id="updateCustomerName" name="name" maxlength="100" required></label>
<label>Email <input id="updateCustomerEmail" type="email" name="email" required></label>
<button type="submit">Update Customer</button>
</form>
<form method="post" action="${pageContext.request.contextPath}/users/delete" onsubmit="return confirm('Permanently delete this customer account?')">
<input type="hidden" id="deleteCustomerId" name="id"><button class="danger" type="submit">Delete Customer</button>
</form>
<c:if test="${mainAdmin}"><form method="post" action="${pageContext.request.contextPath}/users/admin/promote" onsubmit="return confirm('Promote the selected customer to Admin?')">
<input type="hidden" id="selectedId" name="id"><button type="submit">Promote to Admin</button>
</form></c:if>
<button type="button" class="secondary" onclick="clearSelection()">Cancel Selection</button>
</div>
</section>
<h2 class="section-title">Admin List</h2>
<p class="note">Main Admin is protected. Only Main Admin can remove another administrator's access.</p>
<div class="table-scroll"><table><thead><tr><th>ID</th><th>Name</th><th>Full Email</th><th>Role</th><th>Actions</th></tr></thead><tbody>
<c:forEach var="u" items="${records}"><c:if test="${u.role eq 'ADMIN'}">
<tr class="admin-row"><td><c:out value="${u.id}"/></td><td><c:out value="${u.name}"/></td><td class="admin-email"><c:out value="${u.email}"/></td><td>ADMIN <c:if test="${u.id eq mainAdminId}"><span class="pill">Main Admin</span></c:if></td><td>
<span class="note">View only — administrator accounts cannot be updated or deleted here.</span>
<c:if test="${mainAdmin and u.id ne mainAdminId}"><form method="post" action="${pageContext.request.contextPath}/users/admin/demote" onsubmit="return confirm('Remove administrator access and return this account to Customer?')"><input type="hidden" name="id" value="<c:out value="${u.id}"/>"><button type="submit">Remove Admin Role</button></form></c:if>
<c:if test="${u.id eq mainAdminId}"><p class="note">Protected: cannot delete or demote Main Admin.</p></c:if>
</td></tr></c:if></c:forEach>
</tbody></table></div>
<script>
function clearSelection(){
 document.getElementById('selectedCustomer').hidden=true;
 var selectedId=document.getElementById('selectedId');if(selectedId)selectedId.value='';
 document.querySelectorAll('.customer-row').forEach(function(r){r.classList.remove('selected');});
 document.querySelectorAll('.select-customer').forEach(function(b){b.textContent='View / Select';});
}
function selectCustomer(button){
 var row=button.closest('tr');
 if(!row || row.hidden)return;
 clearSelection();
 row.classList.add('selected');
 ['selectedId','updateCustomerId','deleteCustomerId'].forEach(function(id){var el=document.getElementById(id);if(el)el.value=row.dataset.id;});
 var editName=document.getElementById('updateCustomerName');if(editName)editName.value=row.dataset.name;
 var editEmail=document.getElementById('updateCustomerEmail');if(editEmail)editEmail.value=row.dataset.email;
 document.getElementById('selectedCustomerId').textContent=row.dataset.id;
 document.getElementById('selectedName').textContent=row.dataset.name;
 document.getElementById('selectedEmail').textContent=row.dataset.email;
 document.getElementById('selectedCustomer').hidden=false;
 button.textContent='Selected';
 document.getElementById('selectedCustomer').scrollIntoView({behavior:'smooth',block:'nearest'});
}
function selectSuggestion(id){
 var rows=Array.from(document.querySelectorAll('#customerTable .customer-row'));
 var row=rows.find(function(r){return r.dataset.id===id;});
 if(!row)return;
 document.getElementById('customerSearch').value=row.dataset.email;
 filterCustomers();
 selectCustomer(row.querySelector('button'));
 document.getElementById('suggestions').hidden=true;
}
function filterCustomers(){
 var input=document.getElementById('customerSearch');if(!input)return;
 var q=input.value.trim().toLocaleLowerCase();
 var rows=Array.from(document.querySelectorAll('#customerTable .customer-row'));
 var matches=[];
 rows.forEach(function(row){
  var found=!q || row.dataset.email.toLocaleLowerCase().includes(q) || row.dataset.name.toLocaleLowerCase().includes(q);
  row.hidden=!found;
  if(found)matches.push(row);
 });
 document.getElementById('noCustomers').hidden=matches.length>0;
 document.getElementById('searchCount').textContent=q?(matches.length+' matching customer'+(matches.length===1?'':'s')):(matches.length+' registered customer'+(matches.length===1?'':'s')+' — showing all');
 var suggestions=document.getElementById('suggestions');
 suggestions.replaceChildren();
 if(q){
  matches.slice(0,8).forEach(function(row){
   var b=document.createElement('button');b.type='button';b.className='suggestion';
   b.textContent=row.dataset.name+' — '+row.dataset.email;
   b.addEventListener('click',function(){selectSuggestion(row.dataset.id);});
   suggestions.appendChild(b);
  });
 }
 suggestions.hidden=!q || matches.length===0;
 clearSelection();
}
document.getElementById('adminCount').textContent=document.querySelectorAll('.admin-row').length;
document.getElementById('customerCount').textContent=document.querySelectorAll('.customer-row').length;
filterCustomers();
</script>
</main></body></html>
