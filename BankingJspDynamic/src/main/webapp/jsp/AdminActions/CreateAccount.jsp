<!DOCTYPE html>
<html>
<head>
<title>Create Account</title>
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
		<h4>Create Account</h4>
		<section>
			<div class="container mx-auto my-auto mt-4">
				<div class="card border-0 shadow rounded-3" id="smallCard">
					<div class="card-body">
						<form action="<%=request.getContextPath()%>/myServlet"
							method="post">
							<div>
								<label>Customer ID : </label><input id="underline" type="text"
									max="999999999999999" name="customerId" required></input>
							</div>
							<div>
								<label for="type">Account Type: </label> <select name="type"
									id="hugeLabel" required>
									<option value="" selected disabled hidden="hidden">Choose
										here</option>
									<option value="Savings">SAVINGS</option>
									<option value="Fixed Deposit">FIXED DEPOSIT</option>
									<option value="Salary">SALARY</option>
								</select>
							</div>
							<div>
								<label>Branch : </label><input id="underline" type="text" name="branch"
									maxlength="30"></input>
							</div>
							<div>
								<label>Initial Balance : </label><input id="underline" type="number"
									max="20000" name="balance" required></input>
							</div>
							<div class="text-centre mt-3">
								<button class="btn btn-outline-success btn-sm" type="submit" id="smCardBtn"
									value="Add Account" name="data">Add Account</button>
								<button class="btn btn-outline-primary btn-sm ms-2" type="reset">Reset</button>
							</div>
							<div>
								<span style="color: red; font-weight: bold;">${error}</span>
							</div>
						</form>
					</div>
				</div>
			</div>
		</section>
	</div>
</body>
</html>