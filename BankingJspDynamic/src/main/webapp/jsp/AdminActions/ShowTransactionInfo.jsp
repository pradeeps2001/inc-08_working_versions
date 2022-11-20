<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Transaction Info</title>
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
		<br>
		<section>
			<div>
				<form action="<%=request.getContextPath()%>/myServlet" method="post">
					<div class="form-inline">
						<c:choose>
							<c:when test="${customerId == null }">
								<input type="number" name="customerId" id="group-1"
									placeholder="Customer ID" required>
							</c:when>
							<c:otherwise>
								<input type="number" name="customerId" id="group-1"
									value="<c:out value="${customerId}"/>" required>
							</c:otherwise>
						</c:choose>
						<select name="time" id="group-2" required>
							<c:choose>
								<c:when test="${time == null }">
									<option value="" selected disabled hidden="hidden">Time
										Period</option>
								</c:when>
								<c:otherwise>
									<option value="${time}">${time}</option>
								</c:otherwise>
							</c:choose>
							<option value="1 Week">One Week</option>
							<option value="1 Month">One Month</option>
							<option value="6 Month">Six Months</option>
						</select>
						<button class="btn btn-outline-primary btn-sm" type="submit"
							id="shwTransBtn" value="Show Statement" name="data">Show</button>
					</div>
					<div>
						<span class="text-danger">${error}</span>
					</div>
				</form>
				<br>
				<div class="table table-responsive col-lg-6">
					<table
						class="table table-striped table-light table-hover table-borderless">
						<thead>
							<tr class="${hide}">
								<th>Account No</th>
								<th>ID</th>
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
							<c:forEach var="entry" items="${transMap}">
								<c:forEach var="map" items="${entry.value }">
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
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</section>
	</div>
</body>
</html>