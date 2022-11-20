<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Customer Info</title>
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
		<h4>Profile</h4>
		<section>
			<div class="container mx-auto my-auto mt-4">
				<div class="card border-0 shadow rounded-3" id="profile-card">
					<div class="card-body">
						<form action="<%=request.getContextPath()%>/myServlet"
							method="post" id="profile-form">
							<div class="table-responsive" id="cusInfo">
								<table
									class="table table-striped table-light table-hover table-borderless">
									<tr>
										<td id="greyColumn">Customer ID</td>
										<td>${customerId}</td>
									</tr>
									<tr>
										<td id="greyColumn">Name</td>
										<td>${name}</td>
									</tr>
									<tr>
										<td id="greyColumn">DOB</td>
										<td>${dob}</td>
									</tr>
									<tr>
										<td id="greyColumn">E-MAIL</td>
										<td><input id="underline" type="email"
											value="<c:out value="${mail}"/>" name="email"></input></td>
									</tr>
									<tr>
										<td id="greyColumn">Mobile</td>
										<td><input id="underline" type="number"
											value="<c:out value="${mobile}"/>" name="mobile"></input></td>
									</tr>
								</table>
							</div>
							<div class="text-centre ms-2">
								<button class="btn btn-outline-success btn-sm" id="profileBtn"
									type="submit" value="Update Admin Details" name="data">Save</button>
								<button class="btn btn-outline-primary btn-sm ms-2" type="reset">Reset</button>
							</div>
							<br>
							<div>
								<span class="text-success">${success}</span> <span
									class="text-danger">${error}</span>
							</div>
						</form>
					</div>
				</div>
			</div>
		</section>
	</div>
</body>
</html>