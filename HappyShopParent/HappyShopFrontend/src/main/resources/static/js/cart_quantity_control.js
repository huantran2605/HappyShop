$(document).ready(function(){
	var e;
	$(".link_Minus").on("click", function(e){
		e.preventDefault();
		productId = $(this).attr("pid");
		quantity = $("#p" + productId);
		e = quantity
		quantityValue = parseInt(quantity.val()) - 1;
		message = $("#m" + productId);
		if(quantityValue < 1){
			message.text("The min quatity is 1");
		}else{
			message.text("");
			quantity.val(quantityValue);			
		}
	});
	
	$(".link_Plus").on("click", function(e){
		e.preventDefault();
		productId = $(this).attr("pid");
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
	});
	checkValueQuantity();
	
});

function checkValueQuantity(){
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
		});
				
	});
	
	
	
}