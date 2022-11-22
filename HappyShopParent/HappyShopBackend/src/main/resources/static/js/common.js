
//logout
$(document).ready(function() {
	$("#logoutLink").on("click", function(e) {
		e.preventDefault();
		document.logoutForm.submit();
	});
	customizeDropDownMenu();
});

function customizeDropDownMenu(){
  $(".navbar .dropdown").mouseenter(function(){
    $(this).find(".dropdown-menu").show();
  });
  $(".navbar .dropdown").mouseleave(function(){
    $(this).find(".dropdown-menu").hide();
  });
};

