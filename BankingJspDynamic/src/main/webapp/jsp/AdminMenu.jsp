<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Menu</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/jsp/css/menu.css"/>
<link rel="stylesheet" 
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" >
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
	    $("#start").trigger('click');
	    $("button").click(function(){
			if(this.id == "start"){
				removeColor();
				$("#start").css('background-color','#65bbb1');
			} else if(this.id == "customers"){
				removeColor();
				$("#customers").css('background-color','#65bbb1');
			} else if(this.id == "accounts"){
				removeColor();
				$("#accounts").css('background-color','#65bbb1');
			} else if(this.id == "transactions"){
				removeColor();
				$("#transactions").css('background-color','#65bbb1');
			} else if(this.id == "approval"){
				removeColor();
				$("#approval").css('background-color','#65bbb1');
			} else if(this.id == "createCus"){
				removeColor();
				$("#createCus").css('background-color','#65bbb1');
			} else if(this.id == "createAcc"){
				removeColor();
				$("#createAcc").css('background-color','#65bbb1');
			} else if(this.id == "activateCus"){
				removeColor();
				$("#activateCus").css('background-color','#65bbb1');
			} else if(this.id == "activateAcc"){
				removeColor();
				$("#activateAcc").css('background-color','#65bbb1');
			}
			
		});
	});
	function removeColor(){
		$(".menu").css('background-color','#004EAE');
		$(".dropdownMenu").css('background-color','#17378B');
	}
</script>
</head>
<body>
	<div class="menu-container">
		<h6>Welcome ${userName}</h6>
	<form action="<%=request.getContextPath()%>/myServlet" target="content">
		<button type="submit" class="menu" id="start" class="btn btn-secondary btn-sm" value="Home Page" name="data">
		<span id="icons" class="material-symbols-outlined">home</span>Home
		</button>
		<button type="submit" id="customers" class="menu" class="btn btn-secondary btn-sm" value="Show Customer Info" name="data">
		<span id="icons" class="material-symbols-outlined">group</span>Customers
		</button>
		<button type="submit" id="accounts" class="menu" class="btn btn-secondary btn-sm" value="Show Account Info" name="data">
		<span id="icons" class="material-symbols-outlined">account_balance</span>Accounts
		</button>
		<button type="submit" id="transactions" class="menu" class="btn btn-secondary btn-sm" value="Show Transaction Info" name="data">
		<span id="icons" class="material-symbols-outlined">currency_exchange</span>Transactions
		</button>
		<button type="submit" id="approval" class="menu" class="btn btn-secondary btn-sm" value="Request Approval" name="data">
		<span id="icons" class="material-symbols-outlined">rule</span>Request Approval
		</button>
		<div class="right-menu">
					<div>
						<button class="menu" id="menuicon" class="btn btn-dark btn-sm mt-1" type="button" value="drop">
						<span id="icons" class="material-symbols-outlined">add</span><p id="create">Create</p>
						</button>
					</div>
					<div class="dropdown-menu">
					<div class="text-centre">
						<button type="submit" id="createCus" class="dropdownMenu" class="btn btn-secondary btn-sm" value="Create Customer" name="data">
						<span id="icons" class="material-symbols-outlined">person_add</span>Customer
						</button>
					</div>
					<div class="text-centre mt-1">
						<button type="submit" id="createAcc" class="dropdownMenu" class="btn btn-secondary btn-sm" value="Create Account" name="data">
						<span id="icons" class="material-symbols-outlined">account_balance_wallet</span>Account
						</button>
					</div>
			</div>
		</div>
		<div class="right-menu">
					<div>
						<button class="menu" id="menuicon" class="btn btn-dark btn-sm mt-1" type="button" value="drop">
						<span id="icons" class="material-symbols-outlined">verified_user</span><p id="activate">Activate</p>
						</button>
					</div>
					<div class="dropdown-menu">
					<div class="text-centre">
						<button type="submit" id="activateCus" class="dropdownMenu" class="btn btn-secondary btn-sm" value="Customer Activation" name="data">
						<span id="icons" class="material-symbols-outlined">person_add</span>Customer
						</button>
					</div>
					<div class="text-centre mt-1">
						<button type="submit" id="activateAcc" class="dropdownMenu" class="btn btn-secondary btn-sm" value="Account Activation" name="data">
						<span id="icons" class="material-symbols-outlined">account_balance_wallet</span>Account
						</button>
					</div>
			</div>
		</div>
	</form>
	</div>
</body>
</html>