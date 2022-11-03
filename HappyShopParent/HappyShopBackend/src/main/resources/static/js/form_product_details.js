var index = 1;

$(document).ready(function(){
	$("i[name='removeDetailDiv']").each(function(index) {
		$(this).click(function() {
			removeDetailDiv(index);
		});
	});

});  


function addNextDivDetail(){
	htmlDivDetail = `
		<div class="form-inline p-3" id="divDetail${index}">
			<input type="hidden" value="0" name="detailIDs"/>
			<label class ="me-3">Name</label>	
			<input type="text" name="productNameDetails" class="w-25 me-3" maxlength="255"/>
			<label class ="me-3">Value</label>	
			<input type="text" name="productValueDetails" class="w-25" maxlength="255"/>
			<i class="fa-sharp fa-solid fa-circle-xmark fa-2x ms-2"
				onclick="removeDetailDiv(${index})" 
			title="Remove this image"></i>
		</div>
	`;

	$("#divProductDetails").append(htmlDivDetail);
	$("input[name='productNameDetails']").last().focus();
	index++;
}

function removeDetailDiv(index){
	$("#divDetail" + index).remove();
}

