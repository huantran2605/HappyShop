$(document).ready(function(){
	
	
	$(".product_rating").each(function(){
		productId = $(this).attr("productId");
		avr_rating = $("#avr_rating" + productId).val();
		review_count = $("#review_count" + productId).val();
		if(avr_rating != 0){
			setStartRatingForProduct(avr_rating, review_count, productId);			
		}
		else{
			setStarRating0(productId);
		}
		
		content = $("#star_rating" + productId).html();
		$("#customer_review" + productId).html(content);
	});
		
});

function setStartRatingForProduct(avr_rating, review_count, productId){
	avr_r = avr_rating;
	count = 0;
	star_rating_div = $("#star_rating" + productId);
	while(avr_rating >= 0){		
		avr_rating--;
		star_rating_div.append('<i class="fa-sharp fa-solid fa-star fa-yellow"></i>');	
		
		count++;
		if(avr_rating < 1){
			break;			
		}
	}
	
	if(avr_rating > 0){
		star_rating_div.append('<i class="fa-sharp fa-solid fa-star-half fa-yellow"></i>');
		count++;
	}
	while(count < 5){
		star_rating_div.append('<i class="fa-sharp fa-regular fa-star "></i>');
		count++;
	}
	
	star_rating_div.append('<small class="ms-1">' + avr_r +'</small>');
	star_rating_div.append('<small class="ms-1"> (' + review_count +') </small>');
}

function setStarRating0(productId){
	star_rating_div = $("#star_rating" + productId);
	for(let i = 0; i< 5;i++){
		star_rating_div.append('<i class="fa-sharp fa-regular fa-star "></i>');
	}
	star_rating_div.append('<small class="ms-1">' + 0.0 +'</small>');
	star_rating_div.append('<small class="ms-1"> (' + 0 +') </small>');
}