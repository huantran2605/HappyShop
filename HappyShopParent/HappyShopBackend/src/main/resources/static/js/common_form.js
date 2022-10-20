$(document).ready(function() {
	$("#inputPhoto").change(function() {
		var size = this.files[0].size;
		if (size > 1048576) {
			alert("the file is larger than 1MB!");
			this.setCustomValidity("You must choose the file is less than 1MB!");
			this.reportValidity();
		}
		else {
			this.setCustomValidity("");
			ReviewPhoto(this);
		}
	});

});
function ReviewPhoto(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#Photo").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}

function showModal(title, message) {
	$("#modal-title").text(title);
	$("#modal-body").text(message);
	$("#modalDialog").modal("show");
}


$(".deleteUser").click(
	function(e) {
		e.preventDefault();
		showModalConfirmDeletion("user", this);
});

$(".deleteBrand").click(
	function(e) {
		e.preventDefault();
		showModalConfirmDeletion("brand", this);
});

$(".deleteCategory").click(
	function(e) {
		e.preventDefault();
		showModalConfirmDeletion("category", this);
});



function showModalConfirmDeletion(name, selector) {	
	if (name == "user") {
		showModal("Confirmation",
			"Are you sure to delete the " + name + " email: "
			+ $(selector).attr("email") + "?");
	}
	else{
		showModal("Confirmation",
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
