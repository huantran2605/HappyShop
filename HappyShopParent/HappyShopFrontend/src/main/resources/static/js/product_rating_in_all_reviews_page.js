$(document).ready(function(){
	
	productId = $("#hiddenInput").attr("productId");
	avr_rating =  $("#hiddenInput").attr("avr_rating");
	review_count =  $("#hiddenInput").attr("review_count");
		
	
	setStartRatingForProduct(avr_rating, review_count, productId, 'customer_reviews');		
	$("#customer_reviews" + productId + " i").addClass('fa-2x');		
	
	$(".customer_review").each(function(){
			reviewId = $(this).attr("reviewId");
			rating = $(this).attr("rating");
			setStarRatingByCustomer(rating, reviewId,'review_star_by_customer');
	});
});
