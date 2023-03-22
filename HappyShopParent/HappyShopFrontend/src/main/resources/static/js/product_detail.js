var productId;
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
	
	
	$(".question_div").each(function(){
		questionId = $(this).attr("questionId");
		question_likes = $(this).attr("question_likes");
				
		getAllRepliesOfProduct(questionId, question_likes);
		
	});
	
	
	$("#question_input_text").on("click", function() {
		$(".question_inputField").each(function(){
			$(this).css('display','');
		});		
	});
	
		
	$("#question_form").submit(function(event) {
	    event.preventDefault();	
	    question_content = $("#question_input_text").val();
	    if( $(this).find('input[name="fullName"]').length){
		    fullName= $(this).find('input[name="fullName"]').val();		
		}
		if($(this).find('input[name="phoneNumber"]').length){
		    phoneNumber= $(this).find('input[name="phoneNumber"]').val();			
		}
		if($(this).find('input[name="email"]').length){
		    email= $(this).find('input[name="email"]').val();			
		}
		if($(this).find('input[name="fullName"]').length){
			saveQuestion(productId, question_content, fullName, phoneNumber, email);			
		}else{
			saveQuestion(productId, question_content);
		}
		
	});
	
	$(".question_reply_btn").on("click",function(e){
		e.preventDefault();
		questionId =  $(this).attr("questionId");
		
		personName = $("#question_personName" + questionId).text().trim();
		
		showReplyFormAndSaveReply(questionId, false, personName);
		

			
		
	});
	
	$(document).on("click",".reply_reply_btn",function(e){
		e.preventDefault();
		questionId =  $(this).attr("questionId");
		replyId = $(this).attr("replyId");
		personName = $("#reply_person" + replyId).text().trim();
		person = $(this).attr("person");
		adminReplyRequired = false;
		if(person == "admin"){
			adminReplyRequired = true;
		}
		
		showReplyFormAndSaveReply(questionId, customerAuthentication, personName);
				
	});
	
	
	
});

function showReplyFormAndSaveReply(questionId, customerAuthentication, personName){
	if(!$("#reply_form" + questionId).length){
			html = generateReplyForm(customerAuthentication, personName, questionId);
			$("#reply_div" + questionId).prepend(html);
			
			//prevent user delete tag at textarea
			tag = '@' + personName + ': ';
			lengthTag = tag.length;
			$("#reply_form" + questionId).find("textarea").on('keydown', function(event) {
				if (this.value.length <= lengthTag && event.key === 'Backspace') {
					event.preventDefault();
				}
			});
		}
		
		$("#reply_form" + questionId).submit(function(e) {
			e.preventDefault();
			reply_content = $(this).find("#reply_input_text").val();
			 if( $(this).find('input[name="fullName"]').length){
		    	fullName= $(this).find('input[name="fullName"]').val();		
			 }
			if ($(this).find('input[name="phoneNumber"]').length) {
				phoneNumber = $(this).find('input[name="phoneNumber"]').val();

			}
			if ($(this).find('input[name="email"]').length) {
				email = $(this).find('input[name="email"]').val();
			}
			if($(this).find('input[name="fullName"]').length){
				saveReply(questionId, reply_content, adminReplyRequired, fullName, phoneNumber, email);
			}
			else{
				saveReply(questionId, reply_content, adminReplyRequired);
			}
			 
		});
}

function saveReply(questionId, reply_content, adminReplyRequired, fullName, phoneNumber, email){
	$.ajax({
		url: contextPath + 'reply/save',
		type: 'POST',
		data: {
			questionId: questionId,
			reply_content: reply_content,
			adminReplyRequired: adminReplyRequired,
			
			fullName: fullName,
			phoneNumber: phoneNumber,
			email: email,						
			_csrf: csrfValue
		},
		success: function(response) {
			console.log(response);
		},
		error: function(xhr, status, error) {
			alert("fail to save reply of question id" + questionId);
		}
	});
}


function generateReplyForm(customerAuthentication, personName, questionId){
	reply_form = "reply_form" + questionId;
	html = ``;
	if(customerAuthentication == "true"){
		html += `
			<div id= ${reply_form} class="border border-secondary border-1 rounded  p-3">
				<form >
					<div class="form-floating mb-2">
										<textarea class="form-control" id="reply_input_text" maxlength="300" required
											style="height: 90px">@${personName}:</textarea>
										<label for="reply_input_text">Write your reply</label>
									</div>
									
					<div class="d-flex justify-content-end">
									<button class="btn btn-primary " type="submit">Send</button>
								</div>
				</form>
			</div>
		`;
	}
	else{		
		html += `
			<div id= ${reply_form} class="border border-secondary border-1 rounded  p-3">
				<form >
					<div class="form-floating mb-2">
										<textarea class="form-control" id="reply_input_text" maxlength="300" required
											style="height: 90px" >@${personName}: </textarea>
										<label for="reply_input_text">Write your reply</label>
									</div>
									
					<div class="row reply_inputField">
										<div class="col-sm mb-2">
											<div class="form-floating">
												<input type="text" class="form-control" id="fullName"
													name="fullName" placeholder="Full Name" maxlength="30"
													required> <label for="fullName">Full Name* </label>
											</div>
										</div>
										<div class="col-sm mb-2">
											<div class="form-floating">
												<input type="tel" class="form-control" id="phoneNumber"
													name="phoneNumber" placeholder="Phone Number"> <label
													for="phoneNumber">Phone Number </label>
											</div>
										</div>
									</div>
									
						<div class="row reply_inputField" >
										<div class="col-sm">
											<div class="form-floating">
												<input type="email" class="form-control" id="email"
													name="email" placeholder="email"> <label for="email">Email
												</label>
											</div>
										</div>
										<div class="col-sm d-grid ">
											<button class="btn btn-primary " type="submit">Send</button>
										</div>
									</div>					
					</form>
				</div>
		`;
	}
	
	return html;
	
}

function saveQuestion(productId, question_content, fullName, phoneNumber, email){
	$.ajax({
		url: contextPath + 'question/save',
		type: 'POST',
		data: {
			productId: productId,
			question_content: question_content,
			
			fullName: fullName,
			phoneNumber: phoneNumber,
			email: email,						
			_csrf: csrfValue
		},
		success: function(response) {
			//hide form add question
			$('#question_form').hide();
			//add auto to 'most recent questions' wait to approve
			askerName = fullName;
			if(askerName == null && customerFullName != null){
				askerName = customerFullName;
			}			
			html = generateNewQuestionDiv(askerName, question_content);
			$("#new_quesion_div").prepend(html);
		},
		error: function(xhr, status, error) {
			alert("fail to save question");
		}
	});
}

function getAllRepliesOfProduct(questionId, question_likes){
	requestUrl = contextPath + 'reply/all_replies/' +  questionId;
	
	$.ajax({
		url: requestUrl,
		type: 'GET',
		success: function(response) {
			response.forEach(function(r) {
				replier = "";
				person = "";
				if(r.customerName == null){
					replier = r.adminName;
					person = "admin";
				}
				else{
					replier = r.customerName;
					person = "customer";
				}
				html = generateNewReplyDiv(replier, r.reply_content, r.replyTime,questionId, r.id, question_likes, person);
				$("#reply_div" + questionId).append(html);
				
				
			});
			
		},
		error: function(xhr, status, error) {
			alert("fail to load all replies of question id " + questionId);
		}
	});
}

function generateNewReplyDiv(replier, reply_content, replyTime, questionId, replyId, question_likes, person){
	html = ``;
	const date = replyTime.slice(0, 10);
	person_reply = "reply_person" + replyId;
	if(person == "customer"){
		html += `
			<div class="mt-3">
				<strong id = ${person_reply} >${replier}</strong>
				<p class="mt-2">${reply_content}</p>
				<div class="d-flex justify-content-start align-items-center" style="font-size: 14px;">
					<a href="#" class="reply_reply_btn" questionId=${questionId} replyId = ${replyId}>Reply</a>					
					<small class="ms-3">${date}</small>
				</div>			
			</div>
		`;
	}
	else{
		html += `
			<div class="mt-3">
				<div class="d-flex justify-content-start">
					<strong class="me-3" id = ${person_reply} >${replier}</strong>
					<i style="font-size: 13px;" class="fa-sharp fa-solid fa-circle-check d-flex align-items-center"></i>
					<span class="d-flex align-items-start ms-2" style="font-size: 13px;">admin</span>
				</div>
				<p class="mt-2">${reply_content}</p>
				<div class="d-flex justify-content-start align-items-center" style="font-size: 14px;">
					<a href="#" class="reply_reply_btn" questionId=${questionId} replyId = ${replyId}
						person = "admin">Reply</a>
					
					<a href="#" class="btn_like_question" questionId=${questionId}
						likeStatus="0"> <i
						class="fa-sharp fa-regular fa-thumbs-up me-2 ms-3"
						id="like_icon_question + ${questionId}"></i>
					</a>
					<small id="like_text_question + ${questionId}">
						Like (<span id="like_count_question + ${questionId}">${question_likes}</span>)
					</small>
					
					<small class="ms-3">${date}</small>
				</div>			
			</div>
		`;			
	}
	
		
	return html;
}

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

function generateNewQuestionDiv(fullName, question_content){
	myDate = new Date(); // current date and time
	date = myDate.toISOString().substr(0, 10);
	html = `
		<div class="row mt-2">
						<p style="font-size: 13px;" class="text-success">your new question</p>
						<p style="font-weight: bold;">${fullName}</p>						
						<p class="mt-2">${question_content}</p>													
						<small>${date}</small>
						
						<p class="bg-warning" style="max-width: 400px;">Thanks for leaving your comment/question, <br /> 
							that will be approve and show on the page soon!</p>						
					</div>
	`;
	return html;
}