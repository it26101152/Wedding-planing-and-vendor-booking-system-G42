<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="en"><head><meta charset="UTF-8"><title>Payment Invoice</title>
<style>body{font:16px Arial,sans-serif;max-width:740px;margin:40px auto;padding:20px;color:#29232a}h1{color:#8b3560}table{width:100%;border-collapse:collapse}td,th{padding:12px;border-bottom:1px solid #ddd;text-align:left}.actions{margin-top:25px}@media print{.actions{display:none}}</style></head>
<body><h1>Payment Invoice / Receipt</h1>
<p>Invoice reference: <strong><c:out value="${invoiceNumber}"/></strong></p>
<table><tr><th>Payment ID</th><td><c:out value="${payment.id}"/></td></tr>
<tr><th>Booking ID</th><td><c:out value="${payment.bookingId}"/></td></tr>
<tr><th>Customer ID</th><td><c:out value="${payment.customerId}"/></td></tr>
<tr><th>Payment amount</th><td><c:out value="${payment.amount}"/></td></tr>
<tr><th>Payment status</th><td><c:out value="${payment.status}"/></td></tr>
<tr><th>Refund amount</th><td><c:out value="${payment.refundAmount}"/></td></tr>
<tr><th>Refund date</th><td><c:out value="${payment.refundDate}"/></td></tr>
<tr><th>Booking total (saved booking price where available)</th><td><c:out value="${total}"/></td></tr>
<tr><th>Net paid</th><td><c:out value="${paid}"/></td></tr>
<tr><th>Remaining balance</th><td><c:out value="${balance}"/></td></tr></table>
<p>Refunded payments count only the remaining non-refunded amount. This is a printable record, not proof of a payment-gateway refund.</p>
<div class="actions"><button onclick="window.print()">Print / Save PDF</button> <a href="/payments">Back to payments</a></div>
</body></html>
