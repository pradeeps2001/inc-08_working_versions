<!DOCTYPE html>
<html>
<head>
<title>Customer Activation</title>
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
		<h4>Customer Activation</h4>
		<section>
			<div class="container mx-auto my-auto mt-4">
				<div class="card border-0 shadow rounded-3" id="smallCard">
					<div class="card-body">
						<form action="<%=request.getContextPath()%>/myServlet"
							method="post">
							<div>
								<label>Customer ID : </label><input type="number"
									name="customerId" max="999999999999999" required></input>
							</div>
							<div class="text-centre mt-4">
								<button class="btn btn-outline-success btn-sm" type="submit" id="smCardBtn"
									value="Activate Customer" name="data">Activate</button>
								<button class="btn btn-outline-danger btn-sm ms-2" type="submit"
									value="Deactivate Customer" name="data">Deactivate</button>
							</div>
							<br>
							<div>
								<span class="text-success">${activate}</span> <span
									class="text-danger">${deactivate}</span> <span
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