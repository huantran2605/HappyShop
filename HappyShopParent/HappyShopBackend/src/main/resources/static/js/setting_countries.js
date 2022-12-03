var buttonLoad;
var dropDownCountry;
var addCountryButton;
var updateCountryButton;
var deleteCountryButton;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function(){
	buttonLoad = $("#buttonLoad");
	dropDownCountry = $("#dropDownCountry");
	addCountryButton = $("#addCountryButton");
	updateCountryButton = $("#updateCountryButton");
	deleteCountryButton = $("#deleteCountryButton");
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");
	
	
	buttonLoad.on("click", function(){
		loadCountries();
	});
	dropDownCountry.on("change", function(){
		changeFormStateToSlectedCountry();		
	})
	addCountryButton.on("click", function(e) {
		if (addCountryButton.val() == "Add") {
			addCountry();
		}
		else {
			changeFormStateToNew();
		}
	})
	updateCountryButton.on("click", function(){
		updateCountry();
		
	})
	deleteCountryButton.on("click", function(){
		deleteCountry();
		
	})
});

function deleteCountry(){
	countryId = $("#dropDownCountry option:selected").val().split("-")[0];
	url = contextPath + "countries/delete/" + countryId;
	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(){
		$("#dropDownCountry option:selected").remove();
		changeFormStateToNew();
		showToastMessage("Deleted country successfully!");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encounted an error!");
	})
	
}

function updateCountry(){
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	countryId = $("#dropDownCountry option:selected").val().split("-")[0];
	jsonData = {id: countryId, name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		$("#dropDownCountry option:selected").text(countryName);
		$("#dropDownCountry option:selected").val(countryId + "-" + countryCode );
		showToastMessage("Update Country successfully!");
		changeFormStateToNew();
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});		
}

function changeFormStateToNew() {
	addCountryButton.val("Add");
	labelCountryName.text("Country Name:");
	
	updateCountryButton.prop("disabled", true);
	deleteCountryButton.prop("disabled", true);
	
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();	
}


function changeFormStateToSlectedCountry(){
	addCountryButton.prop("value", "New");
	updateCountryButton.prop("disabled", false);
	deleteCountryButton.prop("disabled", false);
	
	selectedCountryValue = $("#dropDownCountry option:selected").val();
	selectedCountryName = $("#dropDownCountry option:selected").text();
	labelCountryName.text("Selected Country");
	selectedCountryCode = selectedCountryValue.split("-")[1];
	fieldCountryName.val(selectedCountryName);
	fieldCountryCode.val(selectedCountryCode);
}

function addCountry() {
	countryForm = document.getElementById("countryForm");
	if(!countryForm.checkValidity()){
		countryForm.reportValidity();
		return;
	}
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = {name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		AfterAddedNewCountry(countryId, countryName, countryCode);
		showToastMessage("The new country has been added");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
		
}

function AfterAddedNewCountry(countryId, countryName, countryCode){
	countryValue = countryId +"-"+ countryCode;
	$("<option>").val(countryValue).text(countryName).appendTo(dropDownCountry);
	$("#dropDownCountry option[value = '" +countryValue + "']").prop("selected", true);
	
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
}

function loadCountries(){
	url = contextPath +"countries/listAll";
	$.get(url, function(data){	
		dropDownCountry.empty();
		$.each(data, function(index, country){
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
	}).done(function(){
		buttonLoad.val("Refresh Country List");
		showToastMessage("Loaded country list successfully!");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server or server encounted an error!");
	})
};

function showToastMessage(message){
	$("#toastMessage").text(message);
	$("#toastCountryList").toast('show');
}