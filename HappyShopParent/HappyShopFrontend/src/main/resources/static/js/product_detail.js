
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
	
	
	
	
	
	$(".btn_like_review").on("click", function(e) {
		e.preventDefault();
		reviewId=$(this).attr("reviewId");
		likeStatus = $(this).attr("likeStatus");
		if(likeStatus == "0"){
			likeReview(reviewId, $(this));
		}
		else{
			unlikeReview(reviewId, $(this));			
		}
				
	});
	
	$(".btn_like_review").each(function(){
		reviewId=$(this).attr("reviewId");
		checkCustomerLikeReview(reviewId, $(this));		
	});
	
});

function checkCustomerLikeReview(reviewId, btn_like_review){
	$.ajax({
		url: contextPath + 'review/like_check',
		type: 'POST',
		data: {
			reviewId: reviewId,
			_csrf: csrfValue
		},
		success: function(response) {
			if(response == "liked"){
				$("#like_icon_review" + reviewId).removeClass("fa-regular");
				$("#like_icon_review" + reviewId).addClass("fa-solid");
				btn_like_review.attr("likeStatus", "1");
				$("#like_text_review" + reviewId).addClass("text-success");
			}
		},
		error: function(xhr, status, error) {
			alert("fail");
		}
	});
}

function likeReview(reviewId, btn_like_review) {
	var currentUrl = window.location.href;
	//redirect if user is not authenticated
	redirectUrl = contextPath + 'login?redirect=' + currentUrl;
	$.ajax({
		url: contextPath + 'review/like',
		type: 'POST',
		data: {
			reviewId: reviewId,
			_csrf: csrfValue
		},
		success: function(response) {
			if(response != 'ok' && response != 'the review is not existed!'){
				location.replace(redirectUrl);
			}
			else if(response == 'ok'){
				$("#like_icon_review" + reviewId).removeClass("fa-regular");
				$("#like_icon_review" + reviewId).addClass("fa-solid");	
				btn_like_review.attr("likeStatus", "1");
				$("#like_text_review" + reviewId).addClass("text-success");
				increaseLikeCount(reviewId);
			}
		},
		error: function(xhr, status, error) {
			alert("fail");
		}
	});
}


function unlikeReview(reviewId, btn_like_review){
	$.ajax({
		url: contextPath + 'review/unlike',
		type: 'POST',
		data: {
			reviewId: reviewId,
			_csrf: csrfValue
		},
		success: function(response) {
			if(response == 'ok'){
				$("#like_icon_review" + reviewId).removeClass("fa-solid");
				$("#like_icon_review" + reviewId).addClass("fa-regular");
				btn_like_review.attr("likeStatus", "0");
				$("#like_text_review" + reviewId).removeClass("text-success");
				decreaseLikeCount(reviewId);				
			}
		},
		error: function(xhr, status, error) {
			alert("fail");
		}
	});
}

function increaseLikeCount(reviewId){
	likeCount = parseInt( $("#like_count_review" + reviewId).text() );
	likeCount += 1;
	$("#like_count_review" + reviewId).text(likeCount);
}

function decreaseLikeCount(reviewId){
	likeCount = parseInt( $("#like_count_review" + reviewId).text() );
	likeCount -= 1;
	$("#like_count_review" + reviewId).text(likeCount);
}


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