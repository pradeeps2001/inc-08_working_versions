<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Password Change</title>
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
		<h4>Password Change</h4>
		<section>
			<div class="container mx-auto my-auto mt-4">
				<div class="card border-0 shadow rounded-3" id="smallCard">
					<div class="card-body">
						<form action="<%=request.getContextPath()%>/myServlet"
							target="content" method="post">
							<div>
								<label id="mediumLabel">Old Password : </label> <input
									id="underline" type="text" name="oldPass"
									placeholder="Old Password" required></input>
							</div>
							<div>
								<label id="mediumLabel">New Password : </label> <input
									id="underline" type="text" name="newPass"
									placeholder="New Password" required></input>
							</div>
							<div class="text-centre mt-3">
								<button id="mdLabelBtn" class="btn btn-outline-success btn-sm"
									type="submit" value="Change Password" name="data">Change</button>
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