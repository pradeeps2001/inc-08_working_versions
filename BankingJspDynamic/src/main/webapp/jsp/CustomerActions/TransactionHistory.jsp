<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Account Info</title>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/jsp/css/frameProps.css" />
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap">
</head>
<body>
	<div>
		<h4>Transaction History</h4>
		<section>
			<div>
				<form action="<%=request.getContextPath()%>/myServlet"
					target="content">
					<div>
						<label for="accountDrop" id="smallLabel">Account : </label> <select
							name="accountDrop" required>
							<c:choose>
								<c:when test="${accountDrop == null }">
									<option value="" selected disabled hidden="hidden">Choose here</option>
								</c:when>
								<c:otherwise>
									<option value="${accountDrop}" selected hidden="hidden" >${accountDrop}</option>
								</c:otherwise>
							</c:choose>
							<c:forEach var="accounts" items="${accountSet}">
								<option value="${accounts}">${accounts}</option>
							</c:forEach>
						</select>
						<button class="btn btn-outline-success btn-sm ms-3" type="submit"
							id="trans-btn" value="Confirm" name="data">Show</button>
					</div>
				</form>
				<br>
				<div class="table table-responsive col-lg-6">
					<table
						class="table table-striped table-light table-hover table-borderless table-fixed">
						<thead>
							<tr class="${hide}">
								<th>Account No</th>
								<th>Transaction ID</th>
								<th>Time</th>
								<th>Mode</th>
								<th>Type</th>
								<th>Amount</th>
								<th>Received From</th>
								<th>Sent To</th>
								<th>Closing Balance</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="map" items="${transMap}">
								<tr>
									<td data-label="Account No">${map.value.getAccountNo()}</td>
									<td data-label="Transaction ID">${map.value.getTransferId()}</td>
									<td data-label="Time">${map.value.getTime()}</td>
									<td data-label="Mode">${map.value.getMode()}</td>
									<td data-label="Type">${map.value.getType()}</td>
									<td data-label="Amount">${map.value.getAmount()}</td>
									<td data-label="Received From">${map.value.getReceivedFrom()}</td>
									<td data-label="Sent To">${map.value.getSendTo()}</td>
									<td data-label="Closing Balance">Rs.
										${map.value.getBalance()}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</section>
	</div>
</body>
</html>