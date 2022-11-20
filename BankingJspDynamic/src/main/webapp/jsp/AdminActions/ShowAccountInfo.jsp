<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Show Account Info</title>
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
		<h4>Account Details</h4>
		<section>
			<div>
				<div>
					<form action="<%=request.getContextPath()%>/myServlet"
						method="post">
						<div id="inputgrp" class="mt-2">
							<c:choose>
								<c:when test="${customerId == null }">
									<input id="inputgrp-box" type="number" name="customerId"
										max="999999999999999" placeholder="Customer ID" required>
								</c:when>
								<c:otherwise>
									<input id="inputgrp-box" type="number"
										value="<c:out value="${customerId}"/>" name="customerId"
										required>
								</c:otherwise>
							</c:choose>
							<button class="btn btn-outline-success btn-sm" type="submit"
								value="Show Accounts" name="data">Submit</button>
						</div>
						<div>
							<span class="text-danger">${error}</span>
						</div>
					</form>
				</div>
				<div class="table table-responsive col-lg-6 mt-3">
					<table
						class="table table-striped table-light table-hover table-borderless">
						<thead>
							<tr class="${hide}">
								<th>Account No</th>
								<th>Account Type</th>
								<th>Branch</th>
								<th>Balance</th>
								<th>Status</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="map" items="${accounts}">
								<tr>
									<td data-label="Account No">${map.value.getAccountNo()}</td>
									<td data-label="Account Type">${map.value.getAccountType()}</td>
									<td data-label="Branch">${map.value.getBranch()}</td>
									<td data-label="Balance">${map.value.getBalance()}</td>
									<td data-label="Status">${map.value.getStatus()}</td>
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