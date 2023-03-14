
$(document).ready(function() {
	$(".image-thumbnail").mouseenter(function() {
		currentIndexImage = $(this).attr("index");
		urlImageThumnail = $(this).attr("src");
		$("#mainImage").attr("src", urlImageThumnail);
		$("#mainImage").attr("index", currentIndexImage);
	});
	$("#mainImage").on("click", function() {
		$("#imageCarouselModal").modal("show");
		imageIndex = parseInt($("#mainImage").attr("index"));
		$("#imageCarouselModal").carousel(imageIndex);
	});

	$("#viewAllReviewsBtn").on("click", function(e) {
		e.preventDefault();
		$("#viewAllReviewsModal").modal("show");
	});
	

});


$(".addToCart").on("click", function() {
	productIdString = $(this).attr("addToCartId");
	productId = parseInt(productIdString.substring(1, productIdString.length));
	quantity = parseInt($("#p" + productId).val());
	url = contextPath + "cart/add_to_cart/" + productId + "/" + quantity;

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
	}).done(function(data) {
		if (data == "added the cart") {
			$("#addToCartToast").toast('show');
		}
		else {
			location.replace(loginURL);
		}
	}).fail(function() {
		alert("fail");
	});
});
