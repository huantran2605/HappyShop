
$(document).ready(function() {
		

	$('select[name="questionStatus"]').on("change", function() {
		$("#questionSearchForm").submit();
	});
	$('select[name="replyStatus"]').on("change", function() {
		$("#replySearchForm").submit();
	});

	handleShowDetailModal('link-detail-customer', 'detailModalCustomer');

	$(".deleteQuestion").click(function(e) {
		e.preventDefault();
		questionId = $(this).attr("questionId");
		showConfirmationModal("Are you sure to delete this question id: " + questionId);
		$("#deleteOption").attr("href", $(this).attr("href"));
	});

	$(document).on("click",".approveBtn", function() {
		object = $(this).attr("object");
		objectId = $(this).attr("objectId");
		showConfirmationModal("Are you sure to approve " + object +" id " + objectId); 
		objectStatus = object == 'question' ? questionStatus : replyStatus;
		url = contextPath + object	+"/approve?"+ object+"Status="+objectStatus+"&" +  object +"Id=" + objectId;
		$("#confirmationModal").find("#deleteOption").attr("href", url);
	});

	$(".checkbox").on("click", function(){
		if(checkAllCheckboxCheckedOrNot()){
			$(".selectAllDiv input").prop("checked", true);
			whenQuestionSelectAllCheckboxChecked();
		}
		else{
			$(".selectAllDiv input").prop("checked", false);
			whenQuestionSelectAllCheckboxChecked();
		}
	});


	$(".selectAllDiv input").on("click", function(){
		whenQuestionSelectAllCheckboxChecked();
		if($(this).prop("checked")){
			$(".checkbox").each(function(){
				$(this).prop("checked", true);
			});
		}
		else{
			$(".checkbox").each(function(){
				$(this).prop("checked", false);
			});
		}		
	});
	
	$(".selectAllDiv button").on("click", function(){
		object = $(this).attr("object");
		validBtn = false;
		$(".checkbox").each(function(){
				if( $(this).prop("checked") ){
					validBtn = true;
					return false;
				}
		});
		if(validBtn){
			objects = object == 'question' ? 'questions' : 'replies';
			showConfirmationModal("Are you sure to approve the selected " + objects +"!");
			objectStatus = object == 'question' ? questionStatus : replyStatus;
			url = contextPath + object +"/approve?" + object+"Status=" + objectStatus;
			$(".checkbox").each(function(){
				if($(this).prop("checked")){
					objectId = $(this).val();
					url += '&'+objects +'SelectedId=' + objectId;
				}
			});
			$("#confirmationModal").find("#deleteOption").attr("href", url);						
		}
	});
	
	
	if(replyStatus == 'ARR'){
		$(".replyApprovedBtnDiv").each(function(){
			replyId = $(this).attr("replyId");
			checkReplyApproved(replyId, $(this));		
		});		
	}
	
	$(".answerBtn").on("click", function() {
		objectId = $(this).attr("objectId");
		object = $(this).attr("object");
		url = contextPath + object +"/answer/" + objectId;
		if(object == 'reply'){
			if ($("#replyApprovedBtnDiv" + objectId).find("button").length){
				showModal('Warning', ' You need to approve the reply first!');
			}else{
				$("#answerFormModal").modal("show").find(".modal-content").load(url);				
			}
		}
		else{
			$("#answerFormModal").modal("show").find(".modal-content").load(url);			
		}	
	});
	
	
	

});

function checkReplyApproved(replyId, replyApprovedBtnDiv){
	$.ajax({
	    url: contextPath + "reply/checkApproved/" + replyId,
	    method: "GET",
	    success: function(data) {
			if(!data){
				htmlBtn = `<button class="btn btn-primary approveBtn me-2" 
									title="approve" objectId="${replyId}" object="reply">Approve</button>`;
				replyApprovedBtnDiv.append(htmlBtn);
			}
	    },
	    error: function(xhr, status, error) {
	        console.error(error);
	    }
	});
}

function whenQuestionSelectAllCheckboxChecked(){
	if($(".selectAllDiv input").prop("checked")){
		$(".selectAllDiv p").addClass("text-warning");		
	}
	else{
		$(".selectAllDiv p").removeClass("text-warning");
		
	}
}

function checkAllCheckboxCheckedOrNot(){
	var allChecked = true;
	screenWidth = $(window).width();
	if(screenWidth >= 576){
		$(".full-details .checkbox").each(function(){
			if(!$(this).prop("checked")){
				allChecked = false;			
			}		
		});
	}
	else{
		$(".less-details .checkbox").each(function(){
			if(!$(this).prop("checked")){
				allChecked = false;			
			}		
		});
	}
	return allChecked;
}


function handleShowDetailModal(link, detailModalName) {
	$("." + link).on("click", function(e) {
		e.preventDefault();
		linkDetailUrl = $(this).attr("href");
		$("#" + detailModalName).modal("show").find(".modal-content").load(linkDetailUrl);
	});
}