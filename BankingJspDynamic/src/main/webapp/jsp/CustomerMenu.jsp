<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Frame</title>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/jsp/css/menu.css" />
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
	    $("#start").trigger('click');
		$("button").click(function(){
			if(this.id == "start"){
				removeColor();
				$("#start").css('background-color','#65bbb1');
			} else if(this.id == "accBtn"){
				removeColor();
				$("#accBtn").css('background-color','#65bbb1');
			} else if(this.id == "transactionBtn"){
				removeColor();
				$("#transactionBtn").css('background-color','#65bbb1');
			} else if(this.id == "dropdownMenu"){
				removeColor();
				$("#dropdownMenu").css('background-color','#65bbb1');
			} else if(this.id == "menuicon"){
				removeColor();
				$("#menuicon").css('background-color','#65bbb1');
			} else if(this.id == "deposit"){
				removeColor();
				$("#deposit").css('background-color','#65bbb1');
			} else if(this.id == "withdraw"){
				removeColor();
				$("#withdraw").css('background-color','#65bbb1');
			} else if(this.id == "transfer"){
				removeColor();
				$("#transfer").css('background-color','#65bbb1');
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
	<form action="<%=request.getContextPath() %>/myServlet" target="content">
		<button type="submit" class="menu" id="start" class="btn btn-dark btn-sm" value="Home" name="data" >
		<span id="icons" class="material-symbols-outlined">home</span>Home
		</button>
		<button type="submit" class="menu" id="accBtn" class="btn btn-secondary btn-sm" value="Account Info" name="data">
			<span id="icons" class="material-symbols-outlined">account_balance_wallet</span>Accounts
		</button>
		<button type="submit" class="menu" id="transactionBtn" class="btn btn-secondary btn-sm mt-2" value="Transaction History" name="data">
		<span id="icons" class="material-symbols-outlined">history</span>Transaction History
		</button>
		<div class="right-menu">
					<div>
						<button class="menu" id="menuicon" class="btn btn-dark btn-sm mt-2" type="button" value="drop">
							<span id="icons" class="material-symbols-outlined">paid</span><p id="transaction">Transaction</p>
						</button>
					</div>
					<div class="dropdown-menu">
					<div class="text-centre">
						<button class="dropdownMenu" id="deposit" class="btn btn-dark btn-sm" type="submit" value="Deposit Cash" name="data">
							<span id="icons" class="material-symbols-outlined">savings</span><span>Deposit</span>
						</button>
					</div>
					<div class="text-centre mt-1">
						<button class="dropdownMenu" id="withdraw" class="btn btn-dark btn-sm" type="submit" value="Withdraw Cash" name="data">
							<span id="icons" class="material-symbols-outlined">payments</span><span>Withdraw</span>
						</button>
					</div>
					<div class="text-centre mt-1">
						<button class="dropdownMenu" id="transfer" class="btn btn-dark btn-sm" type="submit" value="Transfer Cash" name="data">
							<span id="icons" class="material-symbols-outlined">currency_exchange</span><span>Transfer</span>
						</button>
					</div>
			</div>
		</div>
	</form>
	</div>
</body>
</html>