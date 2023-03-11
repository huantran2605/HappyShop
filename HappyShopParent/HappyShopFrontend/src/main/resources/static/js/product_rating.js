$(document).ready(function(){	
	$(".product_rating").each(function(){
		productId = $(this).attr("productId");
		avr_rating = $("#avr_rating" + productId).val();
		review_count = $("#review_count" + productId).val();
		if(avr_rating != 0){
			setStartRatingForProduct(avr_rating, review_count, productId, 'star_rating');			
			setStartRatingForProduct(avr_rating, review_count, productId, 'customer_reviews');		
		}
		else{
			setStarRating0(productId, 'star_rating');
			setStarRating0(productId, 'customer_reviews');
		}		
		$("#customer_reviews" + productId + " i").addClass('fa-2x');
						
		$(".customer_review").each(function(){
			reviewId = $(this).attr("reviewId");
			rating = $(this).attr("rating");
			setStarRatingByCustomer(rating, reviewId,'review_star_by_customer');
		});
		
	});
		
});
