
var extraImagesCount = 0;

var exsitingExtraImagesCount  = $("input[name='exsitingExtraImage']").length;
extraImagesCount = exsitingExtraImagesCount;
$(document).ready(function(){
	$("input[name='exsitingExtraImage']").each(function(index){
		$(this).change(function(){
			if (!checkSizeFile(this)) {
				return;
			}
			showExtraImageThumbnail(this, index);		
		});
	});
	
	$("input[name='extraImage']").each(function(index) {
		$(this).change(function() {
			if (!checkSizeFile(this)) {
				return;
			}
			showExtraImageThumbnail(this, index + exsitingExtraImagesCount);
		});
	});

	$("i[name='linkRemoveExtraImage']").each(function(index) {
		$(this).click(function() {
			removeExtraImage(index);
		});
	});

});  


function addNextExtraImageSection(index){
	htmlExtraImage = `
			<div class="col-4 border" id="divExtraImage${index}">
				<div class="m-3">
					<div id="extraImageHeader${index}"><label for="">Extra Image #${index + 1}:</label></div>
					<div class="mt-3">
						<input type="file" name="extraImage" 
							onchange="showExtraImageThumbnail(this, ${index})"
							accept="image/png, image/jpeg, image/jpg" />
					</div>
				</div>
				<div class="m-3">
					<div class="">
						<img
							src="${defaultImageThumbnailSrc}" id="extraThumbnail${index}"
							alt="Extra Image Product"  class="img-thumbnail" width="304" height="236">
					</div>
				</div>
			</div>
			`;
	htmlLinkRemove = `
		<i class="fa-sharp fa-solid fa-circle-xmark fa-2x float-right"
			onclick ="removeExtraImage(${index - 1})" 
			title="Remove this image"></i>
	`;
	$("#divProductImages").append(htmlExtraImage);		
	$("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
	
	extraImagesCount++;
}
function showExtraImageThumbnail (fileInput, index){
	if (!checkSizeFile(fileInput)) {
		return;
	}
	var file = fileInput.files[0];
	fileName = file.name;
	imageNameHiddenField = $("#imageName" + index);
	if(imageNameHiddenField.length){
		imageNameHiddenField.val(fileName);
	}
	
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#extraThumbnail" + index).attr("src", e.target.result);
	};
	reader.readAsDataURL(file);

	if (index == extraImagesCount ) {
		addNextExtraImageSection(index + 1);		
	}		
}


function removeExtraImage(index) {
	$("#divExtraImage" + index).remove();  
}

