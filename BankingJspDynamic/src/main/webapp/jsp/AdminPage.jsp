<!DOCTYPE html>
<html>
<head>
<title>Admin Page</title>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css">
<link rel="stylesheet" 
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" >
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/jsp/css/homepage.css" />
<link rel = "icon" href = "<%=request.getContextPath()%>/jsp/images/banklogo.png" type = "image/x-icon">
</head>
<body>
	<div id="header" class="border-bottom border-secondary">
		<header>
			<div id="badge">
				<img id="logo" alt="bank logo" src="jsp/images/banklogo.png">
				<h2 id="bankname">Lucas Federal Bank</h2>
				<div class="right-menu">
					<div>
						<button id="menuicon" class="btn btn-dark btn-sm" type="button"
							value="drop" name="data">
							<img id="manicon" alt="menu" src="<%=request.getContextPath()%>/jsp/images/user1.png">
						</button>
					</div>
					<div class="dropdown-menu">
						<form action="<%=request.getContextPath()%>/myServlet"
							target="content" method="post">
							<div class="text-centre mt-1">
								<button class="btn btn-dark btn-sm" id="menubtn" type="submit" value="Edit Admin Details" name="data">
								<span id="icons" class="material-symbols-outlined">settings</span><span id="dropProfile">Profile</span>
								</button>
							</div>
							<div class="text-centre mt-1">
								<button class="btn btn-dark btn-sm" id="menubtn" type="submit" value="Password Change" name="data">
								<span id="icons" class="material-symbols-outlined">key</span><span id="dropPass">Edit Password</span>
								</button>
							</div>
						</form>
						<form action="<%=request.getContextPath()%>/myServlet"
							target="_parent" method="post">
							<div class="text-centre mt-1">
								<button class="btn btn-dark btn-sm" id="logout" type="submit" value="LOGOUT" name="data">
								<span id="icons" class="material-symbols-outlined">logout</span><span id="dropLogout">Logout</span>
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</header>
	</div>
	<div align="left">
		<iframe name="AdminMenu" id="menuframe" src="jsp/AdminMenu.jsp">
		</iframe>
	</div>
	<div align="right">
		<iframe name="content" id="content"
			src="jsp/AdminInitialContent.jsp"> </iframe>
	</div>
	<footer id="footer">
	<div class="social-links">
			<ul>
				<li class="social-items"><a href="#">Follow us</a></li>
				<li class="social-items"><a href="https://www.facebook.com/" target="_blank"><ion-icon name="logo-facebook"></ion-icon></a></li>
				<li class="social-items"><a href="https://twitter.com/i/flow/login" target="_blank"><ion-icon name="logo-twitter"></ion-icon></a></li>
				<li class="social-items"><a href="https://www.linkedin.com/login"  target="_blank"><ion-icon name="logo-linkedin"></ion-icon></a></li>
				<li class="social-items"><a href="https://www.instagram.com/accounts/login/" target="_blank"><ion-icon name="logo-instagram"></ion-icon></a></li>
			</ul>
		</div>
		<div class="copyright">Copyright © 2022, Pradeep's Project</div>
		<div class="quick-links">
			<ul>
				<li class="quick-items"><a href="#"><span id="footer-icon" class="material-symbols-outlined">design_services</span>Services</a></li>
				<li class="quick-items"><a href="#"><span id="footer-icon" class="material-symbols-outlined">info</span>About Us</a></li>
				<li class="quick-items"><a href="#"><span id="footer-icon" class="material-symbols-outlined">forum</span>Feedback</a></li>
			</ul>
		</div>
	</footer>
<script type="module" src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.esm.js"></script>
<script nomodule src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.js"></script>
</body>
</html>