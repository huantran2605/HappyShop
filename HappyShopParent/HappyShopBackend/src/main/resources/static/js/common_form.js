$(document).ready(function() {
	$("#fileImage").change(function() {
		if(!checkSizeFile (this)){
			return;
		}
		ReviewPhoto(this);	
	});
});

function checkSizeFile(fileInput){
		var size = fileInput.files[0].size;
		if (size > MAX_FILE_SIZE) {
			alert("the file is larger than " + (MAX_FILE_SIZE / 1048576) + " MB!");
			fileInput.setCustomValidity("You must choose the file is less than " + (MAX_FILE_SIZE / 1048576) + " MB!");
			fileInput.reportValidity();
			return false;
		}
		else {
			fileInput.setCustomValidity("");
			return true;
		}
}

function ReviewPhoto(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}

function showModal(title, message) {
	$("#modal-title").text(title);
	$("#modal-body").text(message);
	$("#modalDialog").modal("show");
}
function showConfirmationModal(message) {
	$("#modal-body").text(message);
	$("#confirmationModal").modal("show");
}

$(".deleteUser").click(
	function(e) {
		e.preventDefault();
		confirmToDelete("user", this);
});

$(".deleteBrand").click(
	function(e) {
		e.preventDefault();
		confirmToDelete("brand", this);
});

$(".deleteCategory").click(
	function(e) {
		e.preventDefault();
		confirmToDelete("category", this);
});

$(".deleteProduct").click(
	function(e) {
		e.preventDefault();
		confirmToDelete("product", this);
});
 


function confirmToDelete(name, selector) {	
	if (name == "user") {
		showConfirmationModal(
			"Are you sure to delete the " + name + " email: "
			+ $(selector).attr("email") + "?");
	}
	else{
		showConfirmationModal(
			"Are you sure to delete the " + name + " name: "
			+ $(selector).attr("name") + "?");
	}
	$("#deleteOption").attr("href",
		$(selector).attr("href"));
}


function checkPasswordMatch(confirmPassword) {
	if (confirmPassword.value != $("#password").val()) {
		confirmPassword.setCustomValidity("Passwords do not match!");
	} else {
		confirmPassword.setCustomValidity("");
	}
}


$(document).ready(function() {
	dropdownCategories = $("#selectedCategory");
	divChosenCategories = $("#categoriesChosen");

	dropdownCategories.change(function() {
		divChosenCategories.empty();
		showChosenCategories();
	});

	showChosenCategories();
});

function showChosenCategories() {
	dropdownCategories.children("option:selected").each(function() {
		selectedCategory = $(this);
		catId = selectedCategory.val();
		catName = selectedCategory.text().replace(/-/g, "");

		divChosenCategories.append("<span class='badge badge-secondary m-1'>" + catName + "</span>");
	});
}
