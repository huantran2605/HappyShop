function getStateListByCountry() {
	selectedCountryId = $("#country option:selected").val();
	url = contextPath + "states/list_by_country/" + selectedCountryId;
	$.get(url, function(data) {
		stateList.empty();
		$.each(data, function(index, state) {
			$("<option>").val(state.name).text(state.name).appendTo(stateList);
		});
	})
}


function checkEmailUnique(form) {
	url = contextPath + "customers/check_unique_email";
	customerEmail = $("#email").val();
	let csrfValue = document.forms["customer_registration_form"]["_csrf"].value;

	params = { email: customerEmail, _csrf: csrfValue };
	$.post(url, params, function(response) {
		if (response == "Ok") {
			form.submit();
		} else if (response == "Duplicated") {
			showModal("Warning!", "There is another customer having the email " + customerEmail);
		}
		else {
			showModal("Error", "Unknown response from server");
		}
	}).fail(function() {
		showModal("Error", "Could not connect to the server");
	});
	return false;
};

var dropDownCountry;
var stateList;
var stateField;
$(document).ready(function() {
	dropDownCountry = $("#country");
	stateList = $("#stateList");
	stateField = $("#stateField");
	dropDownCountry.on("change", function() {
		stateField.val("").focus();
		getStateListByCountry();
	})
})

function showModal(title, message) {
	$("#modal-title").text(title);
	$("#modal-body").text(message);
	$("#modalDialog").modal("show");
}

function checkPasswordMatch(confirmPassword) {

	if (confirmPassword.value != $("#customerPassword").val()) {
		confirmPassword.setCustomValidity("Passwords do not match!");
	} else {
		confirmPassword.setCustomValidity("");
	}
}