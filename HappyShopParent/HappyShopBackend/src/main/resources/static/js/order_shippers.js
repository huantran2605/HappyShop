var iconNames = {
	'PICKED' : 'fa-people-carry',
	'SHIPPING' : 'fa-truck-fast',
	'DELIVERED' : 'fa-box-open',
	'RETURNED' : 'fa-arrow-rotate-left'
};

$(document).ready(function() {
	$(".linkUpdateStatus").on("click", function(e){
		e.preventDefault();
		link = $(this);
		showUpdateConfirmationModal(link);
	});
});	

function showUpdateConfirmationModal (link){
	orderId = link.attr("orderId");
	status = link.attr("status");
	
	message = "Are you sure to update the status of the order ID #" + orderId + " to " + status + "?";
	showConfirmationModal(message);
	
	yesButton = $("#deleteOption");
	yesButton.show();
	yesButton.on("click", function(e){
		e.preventDefault();
		handleMethodYesButton(link);
	});
}

function handleMethodYesButton(link){
	requestUrl = link.attr("href");
	$.ajax({
		url: requestUrl,
		type: 'POST',
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
		}).done(function(response) {
			yesButton.hide();
			showMessageModal( "Update status successfully!");
			updateColorIcon(response.id, response.status);
		}).fail(function(err){
			showMessageModal("Error to update status order!");
		}).always(function(){
		});
}

function showMessageModal (message){
	$("#modal-body").text(message);
	$("#confirmationModal").modal("show");
}

function updateColorIcon(orderId, status){
	link = $("#link" + status + orderId);
	link.replaceWith("<i class='fa-sharp fa-solid " + iconNames[status] + " fa-green fa-2x'></i>");
}

