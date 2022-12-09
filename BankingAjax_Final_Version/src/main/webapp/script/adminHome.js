window.onload = function(){
	$("#start").click();
};

$(document).ready(function(){
	 $("button").click(function(){
			if(this.id == "start"){
				removeColor();
				$("#start").css('background-color','rgb(33 155 197)');
			} else if(this.id == "customers"){
				removeColor();
				$("#customers").css('background-color','rgb(33 155 197)');
			} else if(this.id == "accounts"){
				removeColor();
				$("#accounts").css('background-color','rgb(33 155 197)');
			} else if(this.id == "transactions"){
				removeColor();
				$("#transactions").css('background-color','rgb(33 155 197)');
			} else if(this.id == "reqApproval"){
				removeColor();
				$("#reqApproval").css('background-color','rgb(33 155 197)');
			} else if(this.id == "createCus"){
				removeColor();
				$("#createCus").css('background-color','rgb(33 155 197)');
			} else if(this.id == "createAcc"){
				removeColor();
				$("#createAcc").css('background-color','rgb(33 155 197)');
			} else if(this.id == "activateCus"){
				removeColor();
				$("#activateCus").css('background-color','rgb(33 155 197)');
			} else if(this.id == "activateAcc"){
				removeColor();
				$("#activateAcc").css('background-color','rgb(33 155 197)');
			}
	});
	
	$("#start").click(function (){
			$("#content").load("AdminInitialContent.html");
	});	
	
	$("#customers").click(function (){
			$("#content").load("AdminActions/ShowCustomerInfo.html");
	});
	
	$("#accounts").click(function (){
			$("#content").load("AdminActions/ShowAccountInfo.html");
	});
	
	$("#transactions").click(function (){
			$("#content").load("AdminActions/ShowTransactionInfo.html");
	});
	
	$("#reqApproval").click(function (){
			$("#content").load("AdminActions/RequestApproval.html");
	});
	
	$("#createCus").click(function (){
			$("#content").load("AdminActions/CreateCustomer.html");
	});
	
	$("#createAcc").click(function (){
			$("#content").load("AdminActions/CreateAccount.html");
	});
	
	$("#activateCus").click(function (){
			$("#content").load("AdminActions/CustomerActivation.html");
	});
	
	$("#activateAcc").click(function (){
			$("#content").load("AdminActions/AccountActivation.html");
	});
	
	$("[name='profile']").click(function (){
			removeColor();
			$("#content").load("AdminActions/AdminProfile.html");
	});
		
	$("[name='editPass']").click(function (){
		removeColor();
		$("#content").load("PasswordChange.html");
	});
		
	$("#logout").click(function (){
		var func = "LOGOUT";
		var obj = {
			"name":func
			};
		var json = JSON.stringify(obj);
		$.ajax ({
			url : "../myServlet",
			type:"POST",
			asynch:"true",
			data: json,
			success: function(){
				location.href = "../";
			},
			error: function(){
				
			}
		});
		window.onload = disableBack();
		window.onpageshow = function(e){
			if (e.persisted) {
				disableBack();
			}
		}
	});
});
	
	function disableBack() {
		window.history.forward()
	}
	
	function removeColor(){
			$(".menu").css('background-color','#004EAE');
			$(".dropdownMenu").css('background-color','#17378B');
	}