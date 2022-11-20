<!DOCTYPE html>
<html>
<head>
<title>Create Customer</title>
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
		<h4>Create Customer</h4>
		<div class="container mx-auto my-auto mt-4">
			<div class="card border-0 shadow rounded-3" id="smallCard">
				<div class="card-body">
					<form action="<%=request.getContextPath()%>/myServlet"
						method="post">
						<div>
							<label>Name : </label><input id="underline" type="text" name="name" required></input>
						</div>
						<div>
							<label>DOB : </label><input id="underline" type="date" name="dob"
								min="1912-01-01" max="2004-01-01" required></input>
						</div>
						<div>
							<label>E-mail : </label><input id="underline" type="email" name="email" required></input>
						</div>
						<div>
							<label>Mobile Number : </label><input id="underline" type="number" name="mobile"
								required></input>
						</div>
						<div>
							<label>Aadhaar Number : </label><input id="underline" type="number" max="999999999999999"
								name="aadhar" required></input>
						</div>
						<div>
							<label>PAN Number : </label><input id="underline" type="text" name="pan"
								required></input>
						</div>
						<div>
							<label>Password : </label><input id="underline" type="password" name="password"
								required></input>
						</div>
						<div class="text-centre mt-3">
							<button class="btn btn-outline-success btn-sm" type="submit" id="smCardBtn"
								value="Add Customer" name="data">Add Customer</button>
							<button class="btn btn-outline-primary btn-sm ms-2" type="reset">Reset</button>
						</div>
						<div>
							<span class="text-success">${success}</span> <span
								class="text-danger">${error}</span>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>