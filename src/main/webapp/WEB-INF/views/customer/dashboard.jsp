<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Customer | Wedding Planner G42</title><style>
/* Finalization: keyboard navigation, responsive forms and readable long values */
:focus-visible{outline:3px solid #963b73;outline-offset:2px}input,select,textarea{max-width:100%;box-sizing:border-box}td,th{overflow-wrap:anywhere}button{cursor:pointer}@media(max-width:640px){body{padding:12px!important}main,.wrap,.panel{max-width:100%;box-sizing:border-box}table{display:block;overflow-x:auto;max-width:100%}}
*{box-sizing:border-box}body{margin:0;background:#faf6f9;color:#38253b;font:16px Arial,sans-serif}header{background:#54294d;color:white;padding:25px max(20px,5vw)}header h1{margin:0 0 8px}main{max-width:1250px;margin:28px auto;padding:0 20px}.tabs{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:12px}.tab{border:1px solid #e5d6df;border-radius:12px;background:white;padding:18px 8px;color:#51294c;cursor:pointer;font-size:15px;font-weight:bold}.tab.active{background:#963b73;color:white}.panel{background:white;border-radius:14px;padding:20px;margin-top:20px;box-shadow:0 4px 15px #0001}.panel h2{margin-top:0}.frame{width:100%;min-height:700px;border:1px solid #eee;border-radius:10px}.actions{display:flex;justify-content:space-between;gap:12px;margin-top:15px}button,.button{background:#963b73;color:white;border:0;border-radius:8px;padding:12px 22px;cursor:pointer;text-decoration:none}button:disabled{opacity:.4;cursor:not-allowed}.secondary{background:#6e6470}.muted{color:#756a75;font-size:14px}.notice{background:#f8e8f1;border-radius:9px;padding:12px;margin-top:15px}@media(max-width:600px){.tabs{grid-template-columns:repeat(2,minmax(0,1fr))}.tab{font-size:13px;padding:13px 5px}}</style></head>
<body>
<header><h1>Wedding Planner | Customer Dashboard</h1><p>Plan your special day step by step</p></header>
<main>
<div class="tabs" role="tablist" aria-label="Wedding planning steps">
<button class="tab active" type="button" data-url="/weddings">1. Wedding Plan</button>
<button class="tab" type="button" data-url="/vendors">2. Select Vendor</button>
<button class="tab" type="button" data-url="/bookings">3. Vendor Booking</button>
<button class="tab" type="button" data-url="/guests">4. Guest List &amp; RSVP</button>
<button class="tab" type="button" data-url="/payments">5. Payments</button>
<button class="tab" type="button" data-url="">6. Review &amp; Confirm</button>
</div>
<section class="panel">
<p class="muted" id="progress">Step 1 of 6</p>
<h2 id="sectionTitle">1. Wedding Plan</h2>
<div id="modulePanel">
<iframe class="frame" id="moduleFrame0" title="Wedding Plan" src="/weddings"></iframe>
<iframe class="frame" id="moduleFrame1" title="Select Vendor" src="/vendors" hidden></iframe>
<iframe class="frame" id="moduleFrame2" title="Vendor Booking" src="/bookings" hidden></iframe>
<iframe class="frame" id="moduleFrame3" title="Guest List" src="/guests" hidden></iframe>
<iframe class="frame" id="moduleFrame4" title="Payments" src="/payments" hidden></iframe>
</div>
<div id="reviewPanel" hidden>
<p>Choose your saved booking to review. Confirmation is stored separately; it does not charge money or create new bookings.</p>
<label for="bookingChoice">Your booking</label>
<select id="bookingChoice" style="display:block;width:100%;padding:12px;margin:10px 0"></select>
<p class="muted" id="reviewDetails">Loading your records...</p>
<div id="confirmed" class="notice" hidden role="status"></div>
</div>
<div class="actions"><button type="button" class="secondary" id="back" disabled>← Back</button><button type="button" id="next">Next →</button></div>
</section>
<form method="post" action="/logout" style="margin-top:20px"><button type="submit">Logout</button></form>
</main>
<script>
const tabs=Array.from(document.querySelectorAll('.tab'));
let current=0;
function showStep(index){
 current=Math.max(0,Math.min(5,index));
 sessionStorage.setItem("g42-customer-step",String(current));
 tabs.forEach((tab,i)=>{tab.classList.toggle('active',i===current);tab.setAttribute('aria-selected',String(i===current));});
 document.getElementById('progress').textContent='Step '+(current+1)+' of 6';
 document.getElementById('sectionTitle').textContent=tabs[current].textContent;
 document.getElementById('back').disabled=current===0;document.getElementById('next').disabled=false;
 document.getElementById('next').textContent=current===5?'OK - Confirm':'Next →';
 document.getElementById('modulePanel').hidden=current===5;
 document.getElementById('reviewPanel').hidden=current!==5;
 document.querySelectorAll('#modulePanel iframe').forEach((frame,i)=>{frame.hidden=i!==current;});
 if(current===5)loadReview();
}
// Keep unsaved forms when moving between steps; draft values survive a page refresh.
const draftPrefix='g42-draft-';
function restoreDraft(frame,step){
 try {
  const doc=frame.contentDocument;
  if(!doc)return;
  const form=doc.getElementById('recordForm');
  if(!form)return;
  const draft=JSON.parse(sessionStorage.getItem(draftPrefix+step)||'{}');
  for(const field of form.querySelectorAll('input,select,textarea')){
   if(field.name && field.type!=='hidden' && field.type!=='submit' && draft[field.name]!==undefined)field.value=draft[field.name];
   field.addEventListener('input',saveDraft);
   field.addEventListener('change',saveDraft);
  }
  function saveDraft(){const values={};for(const field of form.querySelectorAll('input,select,textarea'))if(field.name && field.type!=='hidden')values[field.name]=field.value;sessionStorage.setItem(draftPrefix+step,JSON.stringify(values));}
  form.addEventListener('submit',()=>sessionStorage.removeItem(draftPrefix+step));
  if(step===2){const vendor=sessionStorage.getItem('g42-selected-vendor');if(vendor && !draft.vendorId){const select=doc.getElementById('f_vendorId');if(select)select.value=JSON.parse(vendor).id;}}
 }catch(e){console.warn('Draft restore unavailable',e);}
}
document.querySelectorAll('#modulePanel iframe').forEach((frame,step)=>frame.addEventListener('load',()=>restoreDraft(frame,step)));
window.addEventListener('message',event=>{
 if(event.origin!==window.location.origin || event.data?.type!=='g42-vendor-selected')return;
 const vendor=event.data.vendor;
 if(!vendor || !vendor.id)return;
 sessionStorage.setItem('g42-selected-vendor',JSON.stringify(vendor));
 const bookingFrame=document.getElementById('moduleFrame2');
 try{const field=bookingFrame.contentDocument.getElementById('f_vendorId');if(field){field.value=vendor.id;field.dispatchEvent(new Event('change',{bubbles:true}));}}catch(e){}
 showStep(2);
});
let reviewOptions=[];
async function loadReview(){
 const details=document.getElementById('reviewDetails');
 const select=document.getElementById('bookingChoice');
 details.textContent='Loading saved records...';
 document.getElementById('confirmed').hidden=true;
 try{
  const response=await fetch('/customer/confirmation');
  if(!response.ok)throw new Error('Could not load your records');
  reviewOptions=(await response.json()).bookings;
  select.replaceChildren();
  for(const b of reviewOptions){const opt=document.createElement('option');opt.value=b.id;opt.textContent='Booking '+b.id+' | '+b.date+(b.confirmed==='true'?' (confirmed)':'');select.append(opt);}
  updateReview();
 }catch(e){details.textContent=e.message;}
}
function updateReview(){
 const selected=reviewOptions.find(b=>b.id===document.getElementById('bookingChoice').value);
 document.getElementById('reviewDetails').textContent=selected
  ? selected.details+(selected.ready==='true'?' — Ready to confirm.':' — Please fix the missing items above, then return to Review & Confirm.')
  : 'No bookings saved yet. Create a booking in step 3 first.';
 document.getElementById('next').disabled=!selected || selected.ready!=='true' || selected.confirmed==='true';
}
document.getElementById('bookingChoice').addEventListener('change',updateReview);
async function confirmBooking(){
 const bookingId=document.getElementById('bookingChoice').value;
 if(!bookingId)return;
 const message=document.getElementById('confirmed');
 try{
  const response=await fetch('/customer/confirmation',{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded'},body:new URLSearchParams({bookingId})});
  const data=await response.json();
  message.textContent=data.message||data.error||'Could not confirm booking.';
  message.hidden=false;
  if(response.ok)await loadReview();
  if(response.ok){message.textContent=data.message;message.hidden=false;}
 }catch(e){message.textContent='Connection error. Please retry.';message.hidden=false;}
}
tabs.forEach((tab,i)=>tab.addEventListener('click',()=>showStep(i)));
document.getElementById('back').addEventListener('click',()=>showStep(current-1));
document.getElementById('next').addEventListener('click',()=>{
 if(current===5){confirmBooking();return;}
 showStep(current+1);
});
showStep(Number(sessionStorage.getItem("g42-customer-step") || 0));
</script></body></html>
