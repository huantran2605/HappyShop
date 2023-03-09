
var daysOfRange;

var decimalSeparator; 
var thousandSeparator;


$(document).ready(function() {
	titleTotalItems();
	prepareFormatCurrency();	
});

function titleTotalItems (){
	$("#total_item_date").text("Total Orders");
	$("#total_item_product").text("Total Products");
	$("#total_item_category").text("Total Products");
	
}

function initButtonSaleBy(reportType, callBackFunction){
	$(".btn_sales_by" + reportType).on("click", function(){
		$("#customeTimeDiv" +reportType).addClass("d-none");
		
		$(".btn_sales_by" + reportType).each(function(e){
			$(this).removeClass("btn-primary").addClass("btn-light");
		});
		
		$(this).removeClass("btn-light").addClass("btn-primary");
		
		period = $(this).attr("period");
		callBackFunction(period);				
	});
}

function initButtonCustomTimeAndViewReportData(reportType, callBackFunction){
	
	
	$("#btnCustomTime" + reportType ).on("click", function(){
		$(this).removeClass("btn-light").addClass("btn-primary");
		$("#customeTimeDiv" +  reportType).removeClass("d-none");		
		initCustomDateRangeInput(reportType);  	
		callBackFunction("custom");
	});
	
	$("#btnViewReportDataByDateRange" + reportType).on("click", function(){
		valid = validateDateRange(reportType);
		if(valid){
			callBackFunction("custom");
		}
	})
}

function initCustomDateRangeInput(reportType){
	endTimeInput = document.getElementById("endTime" + reportType);
	startTimeInput = document.getElementById("startTime" + reportType);
	
	today = new Date();  
	endTimeInput.valueAsDate = today;
	startTime = new Date();
	startTime.setDate(today.getDate() - 30);
	startTimeInput.valueAsDate = startTime;
}

function validateDateRange(reportType){
	endTimeInput = document.getElementById("endTime" + reportType);
	startTimeInput = document.getElementById("startTime" + reportType);
	 
	startTime = startTimeInput.valueAsDate;
	endTime = endTimeInput.valueAsDate;
	
	differenceInMiliseconds = endTime - startTime;
	
	daysOfRange = differenceInMiliseconds / (24 * 60 * 60 * 1000);
	if(daysOfRange < 1 || daysOfRange > 30){
		startTimeInput.setCustomValidity("The time must be between 1 to 30 days");
		startTimeInput.reportValidity();
		return false;
	}
	return true;
	
}



function prepareFormatCurrency(){
	
	decimalSeparator = decimal_point_type == "COMMA" ? "," : "."
	thousandSeparator = thousands_point_type == "COMMA" ? "," : ".";
	if(currency_symbol_position.charAt(0) == "B"){//before price
		number_prefix = currency_symbol;
		number_suffix = "";
	}
	else{
		number_suffix = currency_symbol;
		number_prefix = "";
	}
}

function getDenominator(period){
	if(period == "last_7_days"){
		return 7;
	}
	else if(period == "last_28_days"){
		return 28;
	}
	else if(period == "last_6_months"){
		return 6;
	}
	else if(period == "last_year"){
		return 12;
	}
	else if(period == "custom"){
		return daysOfRange;
	}	
	return 7;
}

function getChartTitle(period){
	if(period == "last_7_days"){
		return "report last 7 days";
	}
	else if(period == "last_28_days"){
		return "report last 28 days";
	}
	else if(period == "last_6_months"){
		return "report last 6 months";
	}
	else if(period == "last_year"){
		return "report last year";
	}
	else if(period == "custom"){
		return "report with custom date";
	}
	
	return "report last 7 days";
}

function formatNumber(number){
	return number_prefix +  $.number(number, decimal_digits, decimalSeparator, thousandSeparator) + number_suffix;		
}

function formatChartData(column1, column2){
	var formatter = new google.visualization.NumberFormat(
		{
			decimalSymbol: decimalSeparator, fractionDigits: decimal_digits,
			groupingSymbol: thousandSeparator, prefix: number_prefix,
			suffix: number_suffix
		});

	
	formatter.format(data, column1);
	formatter.format(data, column2);
}