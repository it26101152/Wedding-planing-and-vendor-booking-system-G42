<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Admin | Wedding Planner G42</title><style>
/* Finalization: keyboard navigation, responsive forms and readable long values */
:focus-visible{outline:3px solid #963b73;outline-offset:2px}input,select,textarea{max-width:100%;box-sizing:border-box}td,th{overflow-wrap:anywhere}button{cursor:pointer}@media(max-width:640px){body{padding:12px!important}main,.wrap,.panel{max-width:100%;box-sizing:border-box}table{display:block;overflow-x:auto;max-width:100%}}
*{box-sizing:border-box}body{margin:0;background:#faf6f9;color:#38253b;font:16px Arial,sans-serif}header{background:#54294d;color:white;padding:25px max(20px,5vw)}header h1{margin:0 0 8px}main{max-width:1250px;margin:28px auto;padding:0 20px}.tabs{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:12px}.tab{border:1px solid #e5d6df;border-radius:12px;background:white;padding:18px 8px;color:#51294c;cursor:pointer;font-size:15px;font-weight:bold}.tab.active{background:#963b73;color:white}.panel{background:white;border-radius:14px;padding:20px;margin-top:20px;box-shadow:0 4px 15px #0001}.panel h2{margin-top:0}.frame{width:100%;min-height:700px;border:1px solid #eee;border-radius:10px}.actions{display:flex;justify-content:space-between;gap:12px;margin-top:15px}button,.button{background:#963b73;color:white;border:0;border-radius:8px;padding:12px 22px;cursor:pointer;text-decoration:none}button:disabled{opacity:.4;cursor:not-allowed}.secondary{background:#6e6470}.muted{color:#756a75;font-size:14px}.notice{background:#f8e8f1;border-radius:9px;padding:12px;margin-top:15px}@media(max-width:600px){.tabs{grid-template-columns:repeat(2,minmax(0,1fr))}.tab{font-size:13px;padding:13px 5px}}</style></head>
<body>
<header><h1>Wedding Planner | Admin Dashboard</h1><p>Manage all six modules in one dashboard — select a box or use Next / Back</p></header>
<main>
<div class="tabs" role="tablist" aria-label="Management modules">
<button class="tab active" type="button" data-url="/users">User Management</button>
<button class="tab" type="button" data-url="/vendors">Vendor Management</button>
<button class="tab" type="button" data-url="/weddings">Wedding Plans</button>
<button class="tab" type="button" data-url="/bookings">Customer Orders</button>
<button class="tab" type="button" data-url="/payments">Payments</button>
<button class="tab" type="button" data-url="/guests">Guest Lists &amp; RSVP</button>
</div>
<section class="panel"><p class="muted" id="progress">Module 1 of 6</p><h2 id="sectionTitle">User Management</h2>
<p class="muted">Add, view, update and delete records using the selected module.</p>
<iframe class="frame" id="moduleFrame" name="moduleFrame" title="Selected management module" src="/users"></iframe>
<div class="actions"><button type="button" class="secondary" id="back" disabled>← Back</button><button type="button" id="next">Next →</button></div>
</section>
<form method="post" action="/logout" style="margin-top:20px"><button type="submit">Logout</button></form>
</main>
<script>
const tabs=Array.from(document.querySelectorAll('.tab'));
let current=0;
function selectModule(index){
  current=Math.max(0,Math.min(tabs.length-1,index));
  sessionStorage.setItem("g42-admin-module",String(current));
  index=current;
  tabs.forEach((tab,i)=>{tab.classList.toggle('active',i===index);tab.setAttribute('aria-selected',String(i===index));});
  document.getElementById('sectionTitle').textContent=tabs[index].textContent;
  document.getElementById('progress').textContent='Module '+(index+1)+' of '+tabs.length;
  document.getElementById('back').disabled=index===0;
  document.getElementById('next').disabled=index===tabs.length-1;
  document.getElementById('moduleFrame').src=tabs[index].dataset.url;
}
tabs.forEach((tab,index)=>tab.addEventListener('click',()=>selectModule(index)));
document.getElementById('back').addEventListener('click',()=>selectModule(current-1));
document.getElementById('next').addEventListener('click',()=>selectModule(current+1));
selectModule(Number(sessionStorage.getItem('g42-admin-module') || 0));
</script></body></html>
