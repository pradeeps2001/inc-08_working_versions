<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Login Page</title>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/jsp/css/login.css" />
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap">
<link rel="icon"
	href="<%=request.getContextPath()%>/jsp/images/banklogo.png"
	type="image/x-icon">
</head>
<body>
	<div class="parent">
		<header>
			<div class="title">
				<img id="logo" alt="bank logo" src="jsp/images/banklogo.png">
				<h1 id="bankname">Lucas Federal Bank</h1>
				<p id="line">__________________________________________</p>
				<h3 id="motto">Banking beyond ordinary.</h3>
			</div>
		</header>
		<div class="container">
			<div class="card border-0 shadow-lg rounded">
				<div class="card-body">
					<div class="float-start">
						<span id="loginIcon" class="material-symbols-outlined">person_filled</span>
					</div>
					<div class="mt-2 ms-2 float-start">User Login</div>
					<form class="needs-validation"
						action="<%=request.getContextPath()%>/myServlet" method="post">
						<div id="userId" class="form-group was-validated">
							<input type="number" class="form-control mt-5 my-3 py-2"
								id="user" name="user" placeholder="Enter your User ID" required>
						</div>
						<div id="password" class="form-group was-validated">
							<input type="password" class="form-control my-3 py-2"
								id="password" name="pass" placeholder="Enter your Password"
								required>
						</div>
						<div>
							<span style="color: red;">${error}</span>
						</div>
						<div class="text-centre mt-3">
							<button id="loginbtn" class="btn btn-primary" type="submit"
								value="Login" name="data">Login</button>
						</div>
						<div id="forgotLink">
							<a href="#">Forgot password?</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<footer id="footer">
		<span id="copyright">Copyright © 2022, Pradeep's Project</span>
	</footer>
</body>
</html>