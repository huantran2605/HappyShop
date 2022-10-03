
//logout
$(document).ready(function() {
	$("#logoutLink").on("click", function(e) {
		e.preventDefault();
		document.logoutForm.submit();
	});
});

$(document).ready(function() {
	$("#inputUserPhoto").change(function() {
		var size = this.files[0].size;
		if (size > 1048576) {
			alert("the file is larger than 1MB!");
			this.setCustomValidity("You must choose the file is less than 1MB!");
			this.reportValidity();
		}
		else {
			this.setCustomValidity("");
			ReviewUserPhoto(this);
		}
	});

});
function ReviewUserPhoto(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#userPhoto").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}

function checkEmailUnique(form) {
	$("#Save").click(function() {
		url = "[[@{/users/check_email}]]";
		userEmail = $("#email").val();
		csrfValue = $("input[name='_csrf']").val();

		params = { email: userEmail, _csrf: csrfValue };
		$.post(url, params, function(response) {
			var result = response;
			if (response == "OK") {
				form.submit();
			} else if (response == "Duplicated") {
				return false;
				showWarningModal("There is another user having the email " + userEmail);
			}
			else {
				alert = "has fault";
			}
		});

		return false;
	});
	$("#Update").click(function() {
		form.submit();
	});
};



function showWarningModal(message) {
	$("#modal-title").text("Warning!");
	$("#modal-body").text(message);
	$("#ModalDialog").modal("show");
};