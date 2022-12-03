var buttonLoadForState;
var dropDownCountryForState;
var dropDownState;

var addStateButton;
var	updateStateButton;
var	deleteStateButton;

var labelStateName;
var fieldStateName;

$(document).ready(function(){
	buttonLoadForState = $("#buttonLoadForState");
	dropDownCountryForState = $("#dropDownCountryForState");
	dropDownState = $("#dropDownState");
	addStateButton =  $("#addStateButton") ;
	updateStateButton =  $("#updateStateButton");
	deleteStateButton = $("#deleteStateButton");
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");
	
	buttonLoadForState.on("click", function(){
		loadCountriesForState();
	})
	dropDownCountryForState.on("change", function(){
		loadStates();	
	})
	dropDownState.on("change", function(){
		changeFormStateToSlectedState();
	})
	addStateButton.on("click", function(){
		if(addStateButton.val() == "Add"){			
				addState();					
		}
		else{
			changeFormToNew();			
		}
	})
	updateStateButton.on("click", function(){
		updateState();		
	})
	deleteStateButton.on("click", function(){
		deleteState();
		
	})
})

function deleteState(){
	stateId = $("#dropDownState option:selected").val();
	url = contextPath + "states/delete/" + stateId;
	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(){
		$("#dropDownState option:selected").remove();
		changeFormToNew();
		showToastMessageForState("Deleted state successfully!");
	}).fail(function(){
		showToastMessageForState("Error: Could not connect to server or server encounted an error!");
	})
	
}

function updateState(){
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	selectedCountryId = $("#dropDownCountryForState option:selected").val();
	selectedCountryName = $("#dropDownCountryForState option:selected").text();
	stateId = $("#dropDownState option:selected").val();
	jsonData = {id: stateId, name: stateName, country: {id: selectedCountryId, name: selectedCountryName}};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		$("#dropDownState option:selected").text(stateName);
		$("#dropDownState option:selected").val(stateId);
		showToastMessageForState("Update State successfully!");
		changeFormToNew();
	}).fail(function() {
		showToastMessageForState("ERROR: Could not connect to server or server encountered an error");
	});		
}

function changeFormToNew() {
	addStateButton.val("Add");
	labelStateName.text("State/Province Name:");
	
	updateStateButton.prop("disabled", true);
	deleteStateButton.prop("disabled", true);
	
	fieldStateName.val("").focus();	
}

function addState(){
	stateForm = document.getElementById("stateForm");
	if(!stateForm.checkValidity()){
		stateForm.reportValidity();
		return;
	}
	
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	selectedCountryName = $("#dropDownCountryForState option:selected").text();
	selectedCountryId = $("#dropDownCountryForState option:selected").val();
	jsonData = {name: stateName,country: {id: selectedCountryId, name: selectedCountryName}};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		AfterAddedNewState(stateId, stateName);
		showToastMessageForState("The new state has been added");
	}).fail(function() {
		showToastMessageForState("ERROR: Could not connect to server or server encountered an error");
	});
}

function AfterAddedNewState(stateId, stateName){
	$("<option>").val(stateId).text(stateName).appendTo(dropDownState);
	$("#dropDownState option[value = '" +stateId + "']").prop("selected", true);
	
	fieldStateName.val("").focus();
}

function changeFormStateToSlectedState(){
	addStateButton.prop("value", "New");
	updateStateButton.prop("disabled", false);
	deleteStateButton.prop("disabled", false);
	labelStateName.text("Selected State/Province");
	selectedStateName = $("#dropDownState option:selected").text();
	fieldStateName.val(selectedStateName);
}

function loadStates(){
	selectedCountryId = $("#dropDownCountryForState option:selected").val();
	selectedCountryName = $("#dropDownCountryForState option:selected").text();

	
	url = contextPath +"states/list_by_country/" + selectedCountryId;
	$.get(url, function(data){	
		dropDownState.empty();
		$.each(data, function(index, state){
			optionValue = state.id;
			$("<option>").val(optionValue).text(state.name).appendTo(dropDownState);
		});
	});
}

function loadCountriesForState(){
	url = contextPath +"countries/listAll";
	$.get(url, function(data){	
		dropDownCountryForState.empty();
		$.each(data, function(index, country){
			optionValue = country.id;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountryForState);
		});
	}).done(function(){
		buttonLoadForState.val("Refresh Country List");
		showToastMessageForState("Loaded country list successfully!");
	}).fail(function(){
		showToastMessageForState("Error: Could not connect to server or server encounted an error!");
	})
	
};

function showToastMessageForState(message){
	$("#toastMessageForState").text(message);
	$("#toastForState").toast('show');
}



