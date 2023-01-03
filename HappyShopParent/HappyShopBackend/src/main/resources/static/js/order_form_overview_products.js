var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;

$(document).ready(function() {
	
	fieldProductCost = $("#productCost");
	fieldSubtotal = $("#subtotal");
	fieldShippingCost = $("#shippingCost");
	fieldTax = $("#tax");
	fieldTotal = $("#orderTotal");
	
	$("#productList").on("change", ".quantity-input", function(e) {
		updateSubtotalWhenQuantityChanged($(this));
		updateOrderAmounts();
	});
	
	$("#productList").on("change", ".price-input", function(e) {
		updateSubtotalWhenPriceChanged($(this));
		updateOrderAmounts();
	});	
	
	$("#productList").on("change", ".cost-input", function(e) {
		updateOrderAmounts();
	});
	
	$("#productList").on("change", ".ship-input", function(e) {
		updateOrderAmounts();
	});
	
	formatOrderAmounts();
	formatProductAmounts();
});

function updateOrderAmounts(){
	var totalCost = 0;
	var totalShip = 0;
	var totalSubtotal = 0;
	$(".cost-input").each(function(e) {
		rowNumber = $(this).attr("rowNumber");
		quantity = parseInt($("#quantity" + rowNumber).val());
		totalCost += getNumberValueRemovedThousandSeparator($(this)) * quantity;
	});
	setAndFormatNumberForField("productCost", totalCost);
	
	$(".ship-input").each(function(e) {
		rowNumber = $(this).attr("rowNumber");
		totalShip += getNumberValueRemovedThousandSeparator($(this));
	});
	setAndFormatNumberForField("shippingCost", totalShip);
	
	$(".subtotal-output").each(function(e) {
		rowNumber = $(this).attr("rowNumber");
		totalSubtotal += getNumberValueRemovedThousandSeparator($(this));
	});
	setAndFormatNumberForField("subtotal", totalSubtotal);
	tax = getNumberValueRemovedThousandSeparator($("#tax"));
	orderTotal = tax + totalSubtotal + totalShip;
	setAndFormatNumberForField("orderTotal", orderTotal);
}

function getNumberValueRemovedThousandSeparator(fieldRef) {
	fieldValue = fieldRef.val().replace(",", "");
	return parseFloat(fieldValue);
}

function setAndFormatNumberForField(fieldId, fieldValue) {
	formattedValue = $.number(fieldValue, 2);
	$("#" + fieldId).val(formattedValue);
}

function updateSubtotalWhenPriceChanged(input){
	priceValue = getNumberValueRemovedThousandSeparator(input);
	rowNumber = input.attr("rowNumber");
	
	quantity = parseInt($("#quantity" + rowNumber).val());
	newSubtotal = quantity * priceValue;
	
	setAndFormatNumberForField("subTotal" + rowNumber, newSubtotal);
}

function updateSubtotalWhenQuantityChanged(input){
	rowNumber = input.attr("rowNumber");
	maxQuantity =  parseInt(input.attr("maxQuantity"));
	quantity = parseInt(input.val());	
	if(quantity > maxQuantity ){
		$("#warning" + rowNumber).text("There are only " + maxQuantity+" quantity for this item");
		input.val(maxQuantity);
		quantity = maxQuantity;
	}
	else{
		$("#warning" + rowNumber).text("");
	}
	unitPrice = getNumberValueRemovedThousandSeparator($("#unitPrice" + rowNumber));
	newSubTotal = unitPrice * quantity;
	setAndFormatNumberForField("subTotal" + rowNumber, newSubTotal);
}

function formatProductAmounts() {
	$(".cost-input").each(function(e) {
		formatNumberForField($(this));
	});

	$(".price-input").each(function(e) {
		formatNumberForField($(this));
	});	
	
	$(".subtotal-output").each(function(e) {
		formatNumberForField($(this));
	});	
	
	$(".ship-input").each(function(e) {
		formatNumberForField($(this));
	});	
}

function formatOrderAmounts() {
	formatNumberForField(fieldProductCost);
	formatNumberForField(fieldSubtotal);
	formatNumberForField(fieldShippingCost);
	formatNumberForField(fieldTax);
	formatNumberForField(fieldTotal);	
}

function formatNumberForField(fieldRef) {
	fieldRef.val($.number(fieldRef.val(), 2));
}

function processFormBeforeSubmit() {
	setCountryName();
	
	removeThousandSeparatorForField(fieldProductCost);
	removeThousandSeparatorForField(fieldSubtotal);
	removeThousandSeparatorForField(fieldShippingCost);
	removeThousandSeparatorForField(fieldTax);
	removeThousandSeparatorForField(fieldTotal);
	
	$(".cost-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});
	
	$(".price-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});
	
	$(".subtotal-output").each(function(e) {
		removeThousandSeparatorForField($(this));
	});			
	
	$(".ship-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});		
	
	return true;
}

function removeThousandSeparatorForField(fieldRef) {
	fieldRef.val(fieldRef.val().replace(",", ""));
}

function setCountryName() {
	selectedCountry = $("#country option:selected");
	countryName = selectedCountry.text();
	$("#countryName").val(countryName);
}