
//logout
$(document).ready(function() {
	$("#logoutLink").on("click", function(e) {
		e.preventDefault();
		document.logoutForm.submit();
	});
	customizeDropDownMenu();
	customizeTab();
});

function customizeDropDownMenu(){
  $(".navbar .dropdown").mouseenter(function(){
    $(this).find(".dropdown-menu").show();
  });
  $(".navbar .dropdown").mouseleave(function(){
    $(this).find(".dropdown-menu").hide();
  });
};

function customizeTab(){
	url = document.location.toString();
	des = "#" + url.split("#")[1];
	if(url.match("#")){
		$('.nav-tabs button[data-bs-target = "' + des + '"]').tab('show');
	}
	
	$('.nav-tabs button').on("")
}
