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

$(document).ready(
				function() {
					$(".deleteUser").click(
							function(e) {
								e.preventDefault();
								emailName = $(this).attr("email");
								showModal("Confirmation",
										"Are you sure to delete the user email: "
												+ emailName + "?");
								$("#deleteOption").attr("href",
										$(this).attr("href"));
							});

				});


$(document).ready(
	function() {
		$(".deleteCategory").click(
			function(e) {
				e.preventDefault();
				catId = $(this).attr("id");
				showModal("Confirmation", "Are you sure to delete the category id: "
					+ catId + "?");
				$("#deleteOption").attr("href",
					$(this).attr("href"));
			});

	});

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
