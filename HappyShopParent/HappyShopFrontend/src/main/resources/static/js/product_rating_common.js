

function setStartRatingForProduct(avr_rating, review_count, productId, showDiv){
	avr_r = avr_rating;
	count = 0;
	star_rating_div = $("#" + showDiv + productId);
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

function setStarRating0(productId, showDiv){
	star_rating_div = $("#" + showDiv + productId);
	for(let i = 0; i< 5;i++){
		star_rating_div.append('<i class="fa-sharp fa-regular fa-star "></i>');
	}
	star_rating_div.append('<small class="ms-1">' + 0.0 +'</small>');
	star_rating_div.append('<small class="ms-1"> (' + 0 +') </small>');
}

function setStarRatingByCustomer(rating, reviewId, showDiv){
	r = rating;
	review_star_by_customer_div = $("#" + showDiv + reviewId);
	count = 0;
	while(rating > 0){
		review_star_by_customer_div.append('<i class="fa-sharp fa-solid fa-star fa-yellow"></i>');		
		rating--;
		count++;
	}
	while(count < 5){
		review_star_by_customer_div.append('<i class="fa-sharp fa-regular fa-star "></i>');
		count++;
	}
	
	review_star_by_customer_div.append('<small class="ms-1">' + r +'</small>');
}