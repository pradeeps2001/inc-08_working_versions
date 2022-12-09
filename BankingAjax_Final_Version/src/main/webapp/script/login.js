$(document).ready(function(){
	$("#loginbtn").click(function (){
		console.log("Entered");
		var id = $("#user").val();
		var password = $("#password").val();
		var func = "Login";
		var obj = {
			"custId":id,
			"pass":password,
			"name":func
			};
		var Json = JSON.stringify(obj);
		$.ajax ({
			url : "myServlet",
			type:"POST",
			asynch:"true",
			data: Json,
			success: function(responce){
				console.log(responce);
				var json = JSON.parse(responce);
				console.log(json);
				if(json.error != undefined){
					$("#error").text(json.error);
				} else {
					window.location.href = json["url"];
				}
			},
			error: function(){
				
			}
		});
	});
});