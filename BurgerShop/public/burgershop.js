var crust = {
	thin : 10,
	hard : 10,
	soft : 12
};

var filling = {
	chicken : 120,
	paneer : 100,
	turkey : 130
};

var topping = {
	cucumber : 25,
	tomato : 20,
	cabbage : 20,
	meat : 45
};

var crustSelect = "";
var fillSelect = "";
var topSelect = [];
var total;

$(document).ready(function() {
	$(".type-crust").hide();
	$(".filling").hide();
	$(".toppings").hide();
	$(".invoice").hide();

	$("#makeown").click(function(){
		$(".type-crust").show();
		$(".content").hide();
		$(".filling").hide();
		$(".toppings").hide();
		$(".invoice").hide();
	});
	
	$("#typeBack").click(function(){
		$(".content").show();
		$(".type-crust").hide();
		$(".crust").hide();
		$(".filling").hide();
		$(".toppings").hide();
		$(".invoice").hide();
		$(".message").text("");
		$(".message").hide();
		$('input[type=radio]:checked').prop('checked', false);
		crustSelect = "";
	});
	
	$(".menu.crustBtn").click(function(){
		if($("#veg").prop("checked")){
			console.log("veg");
			if(this.value == "thin"){
				crustSelect = "thin";
			} else if (this.value == "hard"){
				crustSelect = "hard";
			} else if (this.value == "soft"){
				crustSelect = "soft";
			}	
			console.log(crustSelect + " crust selected.");
			$(".filling").show();
			$("#chikTik").hide();
			$("#turkMeat").hide();
			$(".content").hide();
			$(".type-crust").hide();
			$(".toppings").hide();
			$(".invoice").hide();	
			$(".message").hide();	
		} else if($("#non-veg").prop("checked")) {
			console.log("non-veg");
			if(this.value == "thin"){
				crustSelect = "thin";
			} else if (this.value == "hard"){
				crustSelect = "hard";
			} else if (this.value == "soft"){
				crustSelect = "soft";
			}	
			console.log(crustSelect + " crust selected.");
			$(".filling").show();
			$("#chikTik").show();
			$("#turkMeat").show();
			$(".content").hide();
			$(".type-crust").hide();
			$(".toppings").hide();
			$(".invoice").hide();
			$(".message").hide();
		} else {
			$(".message").text("Type not selected");
			$(".message").show();
		}
	});
	
	$("#fillBack").click(function(){
		$(".type-crust").show();
		$(".content").hide();
		$(".crust").hide();
		$(".filling").hide();
		$(".toppings").hide();
		$(".invoice").hide();
		crustSelect = "";
		fillSelect = "";
	});
	
	$(".menu.fillBtn").click(function(){
		console.log($("#veg").prop("checked"));
		if($("#veg").prop("checked")){
			if(this.value == "paneer"){
				fillSelect = "paneer";
			}
			console.log(fillSelect + " filling selected.");
			$(".filling").hide();
			$(".content").hide();
			$(".type-crust").hide();
			$(".toppings").show();
			$("#strip").hide();
			$(".invoice").hide();
			$(".message").text("");
			$(".message").hide();
		} else if($("#non-veg").prop("checked")) {
			if(this.value == "chicken"){
				fillSelect = "chicken";
			} else if (this.value == "paneer"){
				fillSelect = "paneer";
			} else if (this.value == "turkey"){
				fillSelect = "turkey";
			}
			console.log(fillSelect + " filling selected.");
			$(".filling").hide();
			$(".content").hide();
			$(".type-crust").hide();
			$(".toppings").show();
			$("#strip").show();
			$(".invoice").hide();
			$(".message").text("");
			$(".message").hide();
			var max = 3;
			$('input[type=checkbox]').click(function ()
			{
			    var n = $('input[type=checkbox]:checked').length;
			    if (n > max) {
			        $(this).prop('checked', false);
			        alert('Not more than 3 toppings can be selected.');
			    }
			});
		}
	});
	
	$("#topBack").click(function(){
		if($("#veg").prop("checked")){
			$(".filling").show();
			$("#chikTik").hide();
			$("#turkMeat").hide();
			$(".content").hide();
			$(".type-crust").hide();
			$(".toppings").hide();
			$(".invoice").hide();
		} else if($("#non-veg").prop("checked")) {
			$(".filling").show();
			$("#chikTik").show();
			$("#turkMeat").show();
			$(".content").hide();
			$(".type-crust").hide();
			$(".toppings").hide();
			$(".invoice").hide();
		}
	});
	
	$(".menu.confirm").click(function(){
		var total = 0;
		if($("#veg").prop("checked")){
			$('input[type=checkbox]:checked').each(function(i){
          		topSelect[i] = $(this).val();
				console.log(topSelect[i]);
        	});
        	if(topSelect.length === 3){
				for(let i=0; i<(topSelect.length-1); i++){
					console.log(topSelect[i]);
					if(topping[topSelect[i]]<=topping[topSelect[i+1]]){
						topping[topSelect[i]]=0;
						break;
					}
				}
			}
			console.log(topSelect.length + " topping selected.");
			$(".filling").hide();
			$(".content").hide();
			$(".type-crust").hide();
			$(".toppings").hide();
			$(".invoice").show();
			total += Number(crust[crustSelect]);
			total += Number(filling[fillSelect]);
			$("#tblBody").append(
				"<tr class=\"created\">"
				+"<td>Crust</td>"
				+"<td>"+crustSelect+"</td>"
				+"<td>"+crust[crustSelect]+"</td>"
				+"</tr>"
				+"<tr class=\"created\">"
				+"<td>Filling</td>"
				+"<td>"+fillSelect+"</td>"
				+"<td>"+filling[fillSelect]+"</td>"
				+"</tr>"
			);
			for(var i=0; i<topSelect.length; i++){
				total += Number(topping[topSelect[i]]);
				if(topping[topSelect[i]]===0){
					$("#tblBody").append(
					"<tr class=\"created\">"
					+"<td>Topping</td>"
					+"<td>"+topSelect[i]+" (Free)</td>"
					+"<td>"+topping[topSelect[i]]+"</td>"
					+"</tr>"
				);
				} else {
					$("#tblBody").append(
					"<tr class=\"created\">"
					+"<td>Topping</td>"
					+"<td>"+topSelect[i]+"</td>"
					+"<td>"+topping[topSelect[i]]+"</td>"
					+"</tr>"
					);
				}
			}
			$("#tblBody").append(
				"<tr class=\"created\">"
				+"<td></td>"
				+"<td>Amount</td>"
				+"<td>"+total+"</td>"
				+"</tr>"
				);
		} else if($("#non-veg").prop("checked")) {
			$('input[type=checkbox]:checked').each(function(i){
          		topSelect[i] = $(this).val();
          		console.log(topSelect[i]);
        	});
        	if(topSelect.length === 3){
				for(let i=0; i<(topSelect.length-1); i++){
					if(topping[topSelect[i]]<=topping[topSelect[i+1]]){
						topping[topSelect[i]]=0;
						break;
					}
				}
			}
			console.log(topSelect.length + " topping selected.");
			$(".filling").hide();
			$(".content").hide();
			$(".type-crust").hide();
			$(".toppings").hide();
			$(".invoice").show();
			total += Number(crust[crustSelect]);
			total += Number(filling[fillSelect]);
			$("#tblBody").append(
				"<tr class=\"created\">"
				+"<td>Crust</td>"
				+"<td>"+crustSelect+"</td>"
				+"<td>"+crust[crustSelect]+"</td>"
				+"</tr>"
				+"<tr class=\"created\">"
				+"<td>Filling</td>"
				+"<td>"+fillSelect+"</td>"
				+"<td>"+filling[fillSelect]+"</td>"
				+"</tr>"
			);
			for(var i=0; i<topSelect.length; i++){
				total += Number(topping[topSelect[i]]);
				if(topping[topSelect[i]]===0){
					$("#tblBody").append(
					"<tr class=\"created\">"
					+"<td>Topping</td>"
					+"<td>"+topSelect[i]+" (Free)</td>"
					+"<td>"+topping[topSelect[i]]+"</td>"
					+"</tr>"
				);
				} else {
					$("#tblBody").append(
					"<tr class=\"created\">"
					+"<td>Topping</td>"
					+"<td>"+topSelect[i]+"</td>"
					+"<td>"+topping[topSelect[i]]+"</td>"
					+"</tr>"
					);
				}
			}
			$("#tblBody").append(
				"<tr class=\"created\">"
				+"<td></td>"
				+"<td>Amount</td>"
				+"<td>"+total+"</td>"
				+"</tr>"
				);
		}
	});
	
	$("#invoiceBack").click(function(){
		if($("#veg").prop("checked")){
			$(".filling").hide();
			$(".content").hide();
			$(".type-crust").hide();
			$(".toppings").show();
			$("#strip").hide();
			$(".invoice").hide();
			$(".message").hide();
			$(".created").remove();
			$('input[type=checkbox]:checked').prop('checked', false);
			topSelect = [];
		} else if($("#non-veg").prop("checked")) {
			$(".filling").hide();
			$(".content").hide();
			$(".type-crust").hide();
			$(".toppings").show();
			$("#strip").show();
			$(".invoice").hide();
			$(".message").hide();
			$(".created").remove();
			$('input[type=checkbox]:checked').prop('checked', false);
			topSelect = [];
		} else {
			$(".content").show();
			$(".type-crust").hide();
			$(".filling").hide();
			$(".toppings").hide();
			$(".invoice").hide();
			$(".message").hide();
			$(".created").remove();
			crustSelect = "";
			fillSelect = "";
			topSelect = [];
		}
	});
	
	$("#dayburger").click(function(){
		$(".type-crust").hide();
		$(".content").hide();
		$(".filling").hide();
		$(".toppings").hide();
		$(".invoice").show();
		
		let randCrust = Math.floor((Math.random() * 3) + 1);
		if(randCrust === 1){
			crustSelect = "thin";
		} else if(randCrust === 2){
			crustSelect = "hard";
		} else if(randCrust === 3){
			crustSelect = "soft";
		}
		
		let randFill = Math.floor((Math.random() * 3) + 1);
		if(randFill === 1){
			fillSelect = "chicken";
		} else if(randFill === 2){
			fillSelect = "paneer";
		} else if(randFill === 3){
			fillSelect = "turkey";
		}
		
		let randTop1 = Math.floor((Math.random() * 4) + 1);
		let randTop2 = Math.floor((Math.random() * 4) + 1);
		
		while(randTop1 == randTop2){
			randTop2 = Math.floor((Math.random() * 4) + 1);
		}
		
		if(randTop1 == 1 || randTop2 == 1){
			topSelect.push("cucumber");
		} if(randTop1 == 2 || randTop2 == 2){
			topSelect.push("tomato");
		} if(randTop1 == 3 || randTop2 == 3){
			topSelect.push("cabbage");
		} if(randTop1 == 4 || randTop2 == 4){
			topSelect.push("meat");
		}
		
		console.log("Burger of the day :\n\n"+"Crust : "+crustSelect+"\nFilling : "+fillSelect+"\nToppings : "+topSelect[0]+", "+topSelect[1]);
		
		total = 0;
			total += Number(crust[crustSelect]);
			total += Number(filling[fillSelect]);
			$("#tblBody").append(
				"<tr class=\"created\">"
				+"<td>Crust</td>"
				+"<td>"+crustSelect+"</td>"
				+"<td>"+crust[crustSelect]+"</td>"
				+"</tr>"
				+"<tr class=\"created\">"
				+"<td>Filling</td>"
				+"<td>"+fillSelect+"</td>"
				+"<td>"+filling[fillSelect]+"</td>"
				+"</tr>"
			);
			for(var i=0; i<topSelect.length; i++){
				total += Number(topping[topSelect[i]]);
				if(topping[topSelect[i]]===0){
					$("#tblBody").append(
					"<tr class=\"created\">"
					+"<td>Topping</td>"
					+"<td>"+topSelect[i]+" (Free)</td>"
					+"<td>"+topping[topSelect[i]]+"</td>"
					+"</tr>"
				);
				} else {
					$("#tblBody").append(
					"<tr class=\"created\">"
					+"<td>Topping</td>"
					+"<td>"+topSelect[i]+"</td>"
					+"<td>"+topping[topSelect[i]]+"</td>"
					+"</tr>"
					);
				}
			}
			$("#tblBody").append(
				"<tr class=\"created\">"
				+"<td></td>"
				+"<td>Amount</td>"
				+"<td>"+total+"</td>"
				+"</tr>"
				);
	});
	
	$(".menu.placeorder").click(function (){
		$(".filling").hide();
		$(".content").hide();
		$(".type-crust").hide();
		$(".toppings").hide();
		$(".invoice").show();
		$(".message").text("Your Order is placed for Rs." + total);
		$(".message").show();
	});
	
});