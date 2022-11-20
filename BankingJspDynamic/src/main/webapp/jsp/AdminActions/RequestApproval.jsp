<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Approve/Reject Request</title>
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
		<h4>Approve Request</h4>
		<br>
		<section>
			<div>
				<div class="table table-responsive col-lg-6">
					<table
						class="table table-striped table-light table-hover table-borderless">
						<thead>
							<tr>
								<th>Request ID</th>
								<th>Customer ID</th>
								<th>Account No</th>
								<th>Amount</th>
								<th>Request Time</th>
								<th>Status</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="map" items="${requestMap}">
								<tr>
									<td data-label="Request ID">${map.value.getRequestId()}</td>
									<td data-label="Customer ID">${map.value.getCustomerId()}</td>
									<td data-label="Account No">${map.value.getAccountNo()}</td>
									<td data-label="Amount">${map.value.getAmount()}</td>
									<td data-label="Request Time">${map.value.getReqTime()}</td>
									<td data-label="Status">${map.value.getReqStatus()}</td>
									<td>
										<form action="<%=request.getContextPath()%>/myServlet"
											method="post">
											<input type="hidden" name="tableCusId"
												value="${map.value.getCustomerId()}"> 
											<input type="hidden" name="tableAccNo"
												value="${map.value.getAccountNo()}">
											<input type="hidden" name="tableReqId"
												value="${map.value.getRequestId()}">
											<div class="text-centre">
												<button class="btn btn-outline-success btn-sm" type="submit" id="approval"
													value="Approve Table" name="data">Approve</button>
												<button class="btn btn-outline-danger btn-sm ms-1" type="submit" id="approval"
													value="Reject Table" name="data">Reject</button>
											</div>
										</form>
									</td>
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