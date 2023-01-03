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