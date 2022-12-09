window.onload = function(){
	$("#start").click();
};

$(document).ready(function(){
		
		$("button").click(function() {
			if(this.id == "start"){
				removeColor();
				$("#start").css('background-color','rgb(33 155 197)');
			} else if(this.id == "accBtn"){
				removeColor();
				$("#accBtn").css('background-color','rgb(33 155 197)');
			} else if(this.id == "transactionBtn"){
				removeColor();
				$("#transactionBtn").css('background-color','rgb(33 155 197)');
			}  else if(this.id == "deposit"){
				removeColor();
				$("#deposit").css('background-color','rgb(33 155 197)');
			} else if(this.id == "withdraw"){
				removeColor();
				$("#withdraw").css('background-color','rgb(33 155 197)');
			} else if(this.id == "transfer"){
				removeColor();
				$("#transfer").css('background-color','rgb(33 155 197)');
			}
			
		});
		
		$("#start").click(function (){
			$("#content").load("CustomerInitialContent.html");
		});
		
		$("#accBtn").click(function (){
			$("#content").load("CustomerActions/AccountInfo.html");
		});
		
		$("#transactionBtn").click(function (){
			$("#content").load("CustomerActions/TransactionHistory.html");
		});
		
		$("#deposit").click(function (){
			$("#content").load("CustomerActions/Deposit.html");
		});
		
		$("#withdraw").click(function (){
			$("#content").load("CustomerActions/Withdraw.html");
		});
		
		$("#transfer").click(function (){
			$("#content").load("CustomerActions/Transfer.html");
		});
		
		$("[name='profile']").click(function (){
			removeColor();
			$("#content").load("CustomerActions/CustomerProfile.html");
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