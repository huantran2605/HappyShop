
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

	$(".approveBtn").on("click", function() {
		questionId = $(this).attr("questionId");
		showConfirmationModal("Are you sure to approve question id " + questionId);
		url = contextPath + "question/approve?questionId=" + questionId;
		$("#confirmationModal").find("#deleteOption").attr("href", url);
	});

	$(".questionCheckbox").on("click", function(){
		if(checkAllCheckboxCheckedOrNot()){
			$("#questionSelectDiv input").prop("checked", true);
			whenQuestionSelectAllCheckboxChecked();
		}
		else{
			$("#questionSelectDiv input").prop("checked", false);
			whenQuestionSelectAllCheckboxChecked();
		}
	});

	$("#questionSelectDiv input").on("click", function(){
		whenQuestionSelectAllCheckboxChecked();
		if($(this).prop("checked")){
			$(".questionCheckbox").each(function(){
				$(this).prop("checked", true);
			});
		}
		else{
			$(".questionCheckbox").each(function(){
				$(this).prop("checked", false);
			});
		}		
	});
	
	$("#questionSelectDiv button").on("click", function(){
		validBtn = false;
		$(".questionCheckbox").each(function(){
				if( $(this).prop("checked") ){
					validBtn = true;
					return false;
				}
		});
		if(validBtn){
			showConfirmationModal("Are you sure to approve the selected questions!");
			url = contextPath + "question/approve";
			count = 0;
			$(".questionCheckbox").each(function(){
				if( $(this).prop("checked") && count == 0 ){
					questionId = $(this).val();
					url += '?questionsSelectedId=' + questionId;
					count = -1;
				}
				else if($(this).prop("checked") && count == -1){
					questionId = $(this).val();
					url += '&questionsSelectedId=' + questionId;
				}
			});
			$("#confirmationModal").find("#deleteOption").attr("href", url);						
		}
	});

});

function whenQuestionSelectAllCheckboxChecked(){
	if($("#questionSelectDiv input").prop("checked")){
		$("#questionSelectDiv p").addClass("text-warning");		
	}
	else{
		$("#questionSelectDiv p").removeClass("text-warning");
		
	}
}

function checkAllCheckboxCheckedOrNot(){
	var allChecked = true;
	$(".questionCheckbox").each(function(){
		if(!$(this).prop("checked")){
			allChecked = false;			
		}		
	});
	return allChecked;
}


function handleShowDetailModal(link, detailModalName) {
	$("." + link).on("click", function(e) {
		e.preventDefault();
		linkDetailUrl = $(this).attr("href");
		$("#" + detailModalName).modal("show").find(".modal-content").load(linkDetailUrl);
	});
}