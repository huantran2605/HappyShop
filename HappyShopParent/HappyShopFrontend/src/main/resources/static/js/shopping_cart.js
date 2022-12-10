decimalSeparator = decimalPointType == "COMMA" ? "," : "."
thousandSeparator = thousandPointType == "COMMA" ? "," : ".";

$(document).ready(function(){
	var e;
	$(".link_Minus").on("click", function(e){
		e.preventDefault();
		decreaseQuantity($(this));
	});
	
	$(".link_Plus").on("click", function(e){
		e.preventDefault();
		increaseQuantity($(this));
		
	});
	updateSubTotalFromInputQuantityField();
});

function increaseQuantity(link){
	productId = link.attr("pid");
	quantity = $("#p" + productId);
	quantityValue = parseInt(quantity.val()) + 1;
	maxQuantityValue =  parseInt(quantity.attr("max"));
	message = $("#m" + productId);
	if (quantityValue > maxQuantityValue) {
			message.text("There are only " + maxQuantityValue +" quantity for this item");
		}
	else {
			message.text("");
			quantity.val(quantityValue);
	}
	updateSubTotal (productId, quantityValue);
}

function decreaseQuantity(link){
	productId = link.attr("pid");
	quantity = $("#p" + productId);
	quantityValue = parseInt(quantity.val()) - 1;
	message = $("#m" + productId);
	if(quantityValue < 1){
		message.text("The min quatity is 1");
	}else{
		message.text("");
		quantity.val(quantityValue);			
	}
	updateSubTotal (productId, quantityValue);
}

function updateSubTotalFromInputQuantityField(){
	$(".quantity_Input").on("click", function(){
		productIdString = $(this).attr("id");
		productId = parseInt(productIdString.substring(1, productIdString.length));
		message = $("#m" + productId);
		message.text("");
		maxQuantityValue =  parseInt($(this).attr("max"));
		$(this).on("change", function(){
			quantityValue = parseInt($(this).val());
			if(quantityValue > maxQuantityValue){				
				$(this).val(maxQuantityValue);
				message.text("There are only " + maxQuantityValue +" quantity for this item");
			}
			else if(quantityValue < 1){
				$(this).val(1);
				message.text("The min quatity is 1");				
			}
			else if($(this).val() == ""){
				$(this).val(1);	
				quantityValue = 1;
			}
			updateSubTotal(productId, quantityValue);			
		});
				
	});	
}

function updateSubTotal(productId, quantityValue){	
	url = contextPath + "cart/update_quantity/" + productId + "/"+ quantityValue;		
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
	}).done(function(data) {
		if(data == "must login"){					
			location.replace(loginURL);
		}
		else{
			newSubTotal =  formatNumber(parseFloat(data));
			$("#subTotal" + productId).text(newSubTotal);
			updateTotal();
		}
	}).fail(function() {
		alert("fail");
	});
	
	
}

function updateTotal(){
	 var total = 0.0;
	 $(".subTotal").each(function(index, element) {
		total += parseFloat(clearFormatNumber(element.innerHTML));
	 });
	 total = formatNumber(total);
	 $("#total").text(total);	
}

function formatNumber(number){
	return $.number(number, decimalDigits, decimalSeparator, thousandSeparator);
}

function clearFormatNumber(numberString){
	result =  numberString.replace(thousandSeparator,"");
	result = result.replace(decimalSeparator,".");
	return result;
}






