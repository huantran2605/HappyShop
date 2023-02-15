var orderId;

$(document).ready(function(){
	
		$(".linkReturnOrder").on("click", function(e) { 
			link = $(this);
			e.preventDefault();
			showOrderReturnModal(link);
		});
	
});

function showOrderReturnModal(link){
	$(".firstOption").show();
	$(".secondOption").text("Cancel");
	$(".divMessage").hide();
	$(".divReason").show();
	orderId = link.attr("orderId");
	$("#orderReturnModal").find(".modal-title").text("Return Order ID #" + orderId);
	$("#orderReturnModal").modal("show");
}

function submitOrderReturnForm(){
	reason = $("input[name='returnReason']:checked").val();
	notes = $("#addInfo").val();
	reason += "-"+ notes;
	
	sendReturnOrderRequest(reason, notes);
	return false;
}

function sendReturnOrderRequest(reason, note){
	requestBody = {orderId : orderId, reason: reason, note: note};
	url = contextPath + "orders/return"
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(requestBody),
		contentType: 'application/json'
	}).done(function(response) {
		message = "Return request have been sent!";
		showMessageModal(message);
		updateStatusAndHideIcon();
	}).fail(function(err) {
		showMessageModal("Error to send the request !");
	});
}

function showMessageModal(message){
	$(".firstOption").hide();
	$(".secondOption").text("Close");
	$(".divMessage").text(message);
	$(".divMessage").show();
	$(".divReason").hide();
	$("#orderReturnModal").modal("show");
}

function updateStatusAndHideIcon (){
	$("#linkUpdateStatusReturnOrder" + orderId).text("RETURN_REQUESTED");
	$("#orderReturnIcon" + orderId).hide();
}


