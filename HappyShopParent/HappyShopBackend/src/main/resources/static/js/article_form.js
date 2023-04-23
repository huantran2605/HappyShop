var topicId;
var articleId;
var selectedMedia;
var selectedMediaContainer;
var listFile = [];
$(document).ready(function() {
	//init tinymce (rich text)
	tinymce.init({
		selector: '#content',
		plugins: 'anchor autolink charmap codesample emoticons image link lists media searchreplace table visualblocks wordcount checklist mediaembed casechange export formatpainter pageembed linkchecker a11ychecker tinymcespellchecker permanentpen powerpaste advtable advcode editimage tinycomments tableofcontents footnotes mergetags autocorrect typography inlinecss',
		toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | addcomment showcomments | spellcheckdialog a11ycheck typography | align lineheight | checklist numlist bullist indent outdent | emoticons charmap | removeformat',
		tinycomments_mode: 'embedded',
		tinycomments_author: 'Author name',
		mergetags_list: [
			{ value: 'First.Name', title: 'First Name' },
			{ value: 'Email', title: 'Email' },
		]
	});

	
	$('#image-input').change(function() {
		let file = this.files[0];
		listFile.push(file);
		if (file) {
			let reader = new FileReader();
			reader.onloadend = function() {
				html = `
							  <div class="image-container col m-2 position-relative">
								<img
									src="${reader.result}"
									alt="" style="height: auto; width: 150px;" />
									<i class="position-absolute delete-icon fa-sharp fa-solid fa-x" title="remove"></i>
							</div>
						  `;
				$('#image-preview').append(html);
			}
			reader.readAsDataURL(file);			
		}

	});

	$('#video-input').change(function() {
		// Lấy đường dẫn tập tin video đã chọn
		const file = $(this).get(0).files[0];
		listFile.push(file);
		const fileURL = URL.createObjectURL(file);
		html = `
						  <div class="video-container position-relative col m-2">
					    	<video src = "${fileURL}" id="video-player" style="height: auto; width: 350px;" controls></video>
					    	<i class="position-absolute delete-icon fa-sharp fa-solid fa-x" title="remove"></i> 
						 </div>
			  	`;
		// Hiển thị video
		$('#video-preview').append(html);
	});
	
	$('#form-upload-media').submit(function(e) {
		e.preventDefault();
		//create formData		
		let formData = new FormData();	
		for(let i = 0; i<listFile.length ; i++){ 
			formData.append("files", listFile[i]);
		}				
		if (!formData.has('files')) {
			return alert("Please choose at least 1 file.");
		}
		topicId = $("input[name='topicId']").val();
		if (!$("input[name='topicId']").length) {
			topicId = $("#form-upload-media select[name='topicId']").val();
		}
		formData.append('topicId', topicId);
		
		articleId = $("input[name='articleId']").val();
		if(articleId != null){
			formData.append('articleId', articleId);
		}		
		
		//show loading...		
		$('#upload-modal').modal("show");		
		uploadMedia(formData);
		
	});

	$(document).on('click', '.image-container .delete-icon', function() {	
		selectedMedia = $(this);
		selectedMediaContainer = selectedMedia.closest('.image-container');
		if(selectedMedia.closest('#image-uploaded').length){
			$("#confirmationModal").find('.modal-body').text("Are you sure to remove this media");
			$("#confirmationModal").modal('show');								
		}else{			
			let index = selectedMedia.closest('.image-container').index();
			listFile.splice(index, 1);			
			selectedMediaContainer.remove();						
		}			
	});
	
	$(document).on('click', '.video-container .delete-icon', function() {
		selectedMedia = $(this);
		selectedMediaContainer = selectedMedia.closest('.video-container');
		if(selectedMedia.closest('#video-uploaded').length){
			$("#confirmationModal").find('.modal-body').text("Are you sure to remove this media");
			$("#confirmationModal").modal('show');		
		}else{
			let index = selectedMedia.closest('.video-container').index();
			listFile.splice(index, 1);
			selectedMediaContainer.remove();
		}
	});
	
	//delete option event 
	$("#confirmationModal #deleteOption").on("click", function(){
			selectedMediaContainer.remove();	
			$("#confirmationModal").modal('hide');
			let mediaUrl = selectedMedia.prev().attr('src');
			deleteUploadedMedia(mediaUrl);
	})	
		
	//copy link
	$(document).on("click", ".copy-link", function(){
		let link = $(this).find("span").attr("link");
		copyLink(link);
	});
	

});



function deleteUploadedMedia(mediaUrl){
	let requestUrl = contextPath + "article/delete-uploaded-media";
	let data = {mediaUrl: mediaUrl};
	$.ajax({
		type: 'POST',
		url: requestUrl,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: data,
	}).done(function(response) {
		alert(response);
	}).fail(function(xhr, status, error) {
		alert("fail to delete media url: " + mediaUrl);
	});
}


function afterUploadSuccess(urls){
	
	//set select topic readonly	
	if(!$("input[name='topicId']").length){
		$("#form-upload-media select[name='topicId']").hide();
		$("#mainForm select[name='topicId']").prop('disabled', true).find('option[value=' +topicId + ']').prop('selected', true);
	}
	//delete all image/video preview
	$('#image-preview').empty();
	$('#video-preview').empty();
	//show images and videos uploaded
	urls.forEach(function(url) {
		let html=``;
		if(getFileType(url) == 'image'){			
			html = `
					  <div class="image-container col m-2 position-relative">
						<img
							src="${url}"
							alt="" style="height: auto; width: 150px;" />
							<i class="position-absolute delete-icon fa-sharp fa-solid fa-x" title="remove"></i>
						<div class="text-center copy-link" style="cursor: pointer;">
								<i class="fa-sharp fa-solid fa-copy"  title="Copy link"></i>
								<span link = "${url}">Copy link</span>
						</div>
					</div>
				  `;
			$("#image-uploaded").append(html);  	
		}
		else if(getFileType(url) == 'video'){
			html = `
						  <div class="video-container position-relative col m-2">
					    	<video src = "${url}" id="video-player" style="height: auto; width: 350px;" controls></video>
					    	<i class="position-absolute delete-icon fa-sharp fa-solid fa-x" title="remove"></i> 
					    	<div class="text-center copy-link" style="cursor: pointer;">
								<i class="fa-sharp fa-solid fa-copy" title="Copy link"></i>
								<span link = "${url}">Copy link</span>
							</div>
						 </div>
			  	`;
			$("#video-uploaded ").append(html);  	
		}

	});
	
}



function getFileType(url) {
  var extension = url.split('.').pop().toLowerCase();

  if(extension === 'mp4'){
	  return 'video';
  }
  return 'image';
}

function copyLink(link) {

      // Tạo một thẻ input ẩn để chứa liên kết
      var input = document.createElement('input');
      input.setAttribute('value', link);
      document.body.appendChild(input);

      // Chọn toàn bộ nội dung của thẻ input
      input.select();

      // Sao chép nội dung đã chọn vào clipboard
      document.execCommand('copy');

      // Xóa thẻ input khỏi trang web
      document.body.removeChild(input);

      // Thông báo cho người dùng biết rằng liên kết đã được sao chép
      alert('Link copied to clipboard!');
}

function uploadMedia(formData) {
	let url = contextPath + "article/upload-media";
	//upload media
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: formData,
		processData: false,
		contentType: false,
		dataType: "json",
	}).done(function(data) {
		afterUploadSuccess(data.urls);
		$("input[name='articleId']").val(data.articleId);
		//show alert success
		$('#upload-modal').modal("hide").off('hidden.bs.modal').on('hidden.bs.modal', function() {
			alert("Upload media to server successfully!");
		});
		//clear listFile
		listFile.splice(0, listFile.length);
	}).fail(function(xhr, status, error) {
		$('#upload-modal').modal("hide").on('hidden.bs.modal', function() {
			alert("fail to upload media!");
		});
	});
}

