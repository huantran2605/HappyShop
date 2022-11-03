
function IsNameUnique(form) {
	url = moduleURL + "/check_name";
	productName = $("#name").val();
	productId = $("#id").val();

	let csrfValue = document.forms["save_form"]["_csrf"].value;

	params = {
		id: productId,
		name: productName,
		_csrf: csrfValue
	};
	$.post(
		url,
		params,
		function(response) {
			if (response == "ok") {
				form.submit();
			}
			else if (response == "duplicated") {
				showModal("Warning!",
					"There is another product having the name: " + productName);
			}
			else {
				showModal("Error", "Unknown response from server");
			}
		}).fail(function() {
			showModal("Error", "Could not connect to the server");
		});
	return false;
};


dropdownBrands = $("#brand");
$(document).ready(function() {
	$("#shortDescription").richText();
	$("#fullDescription").richText();
	dropdownBrands.change(function() {
		$("#category").empty();
		getCategories();
	});
	
	getCategoriesForNewForm();
});

function getCategoriesForNewForm(){
	categoryField = $("#categoryId");
	editMode = false;
	if(categoryField.length){
		editMode = true;
	}
	if(!editMode){
		getCategories();
	}
}


function getCategories() {
	brandId = dropdownBrands.val();
	url = brandModuleURL + "/" + brandId + "/categories";

	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo("#category");
		});
	});
}

