var chartOptions;
var chartDiv;
var data;

var totalGrossSales;
var totalNetSales;
var avgGrossSales;
var avgNetSales;
var ordersCount;

var endTimeInput;
var startTimeInput;
var daysOfRange;

decimalSeparator = decimal_point_type == "COMMA" ? "," : "."
thousandSeparator = thousands_point_type == "COMMA" ? "," : ".";
if(currency_symbol_position.charAt(0) == "B"){
	number_prefix = currency_symbol;
	number_suffix = "";
}
else{
	number_suffix = currency_symbol;
	number_prefix = "";
}

$(document).ready(function() {
	endTimeInput = document.getElementById("endTime");
	startTimeInput = document.getElementById("startTime");
	chartDiv = document.getElementById('chart_div');
	$(".btn_sales_by_date").on("click", function(){
		$("#customeTimeDiv").addClass("d-none");
		
		$(".btn_sales_by_date").each(function(e){
			$(this).removeClass("btn-primary").addClass("btn-light");
		});
		
		$(this).removeClass("btn-light").addClass("btn-primary");
		
		period = $(this).attr("period");
		loadSaleReportByDate(period);				
	});
	
	$("#btnCustomTime").on("click", function(){
		$(this).removeClass("btn-light").addClass("btn-primary");
		$("#customeTimeDiv").removeClass("d-none");		
		initCustomDateRangeInput();  	
		loadSaleReportByDate("custom_date");	
	});
	$("#btnViewReportDataByDateRange").on("click", function(){
		valid = validateDateRange();
		if(valid){
			loadSaleReportByDate("custom_date");
		}
	})
	
	

});

function initCustomDateRangeInput(){
	today = new Date();  
	endTimeInput.valueAsDate = today;
	startTime = new Date();
	startTime.setDate(today.getDate() - 30);
	startTimeInput.valueAsDate = startTime;
}

function validateDateRange(){
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


function loadSaleReportByDate(period){
	if(period == "custom_date"){
		startTime = $("#startTime").val();
		endTime = $("#endTime").val();
		url = contextPath + "reports/sale_by_date/" + startTime + "/" + endTime;				
	}else{
		url = contextPath + "reports/sale_by_date/" + period;		
	}
	$.get(url, function(response){
		prepareData(response);
		customizeChart(period);
		drawChart(period);		
	});
	
}

function prepareData(responseJSON){
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');
	data.addColumn('number', 'Orders');
	
	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	ordersCount = 0;
	
	$.each(responseJSON, function(index, reportItem){
		data.addRows([   [reportItem.identifier, reportItem.grossSales, reportItem.netSales,
		 reportItem.ordersCount]   ]);
		totalGrossSales += reportItem.grossSales;
		totalNetSales += reportItem.netSales;
		ordersCount += reportItem.ordersCount;	 
	});
}

function customizeChart(period){
	chartOptions = {
		title: getChartTitle(period),
		'height': 360,
		legend: {position: 'top'},
		
		series: {
			0: {targetAxisIndex: 0},
			1: {targetAxisIndex: 0},
			2: {targetAxisIndex: 1},
		},
		
		vAxes: {
			0: {title: 'Sales Amount', format: 'currency'},
			1: {title: 'Number of Orders'}
		}
	};
	
	var formatter = new google.visualization.NumberFormat(
		{
			decimalSymbol: decimalSeparator, fractionDigits: decimal_digits,
			groupingSymbol: thousandSeparator, prefix: number_prefix,
			suffix: number_suffix
		});
	
//	if(currency_symbol_position.charAt(0) == "B"){
//		formatter.setPrefix(currency_symbol);
//	}
//	else{
//		formatter.setSuffix(currency_symbol);setSuffix
//	}
//	formatter.setDecimalDigits(decimal_digits);
//	formatter.setGroupingSymbol(thousandSeparator);
//	formatter.setDecimalSymbol(decimalSeparator);
	
	formatter.format(data, 1);
	formatter.format(data, 2);
}

function drawChart(period){  
	var salesChart = new google.visualization.ColumnChart(chartDiv);
	salesChart.draw(data, chartOptions);
	$("#textTotalGrossSales").text( formatNumber(totalGrossSales) );
	$("#textTotalNetSales").text( formatNumber(totalNetSales));
	$("#textTotalOrders").text(ordersCount);
	
	
	$("#textAvgGrossSales").text( formatNumber(totalGrossSales / getDenominator(period)) );
	$("#textAvgNetSales").text( formatNumber(totalNetSales / getDenominator(period)) );
	
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
	else if(period == "custom_date"){
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
	else if(period == "custom_date"){
		return "report with custom date";
	}
	
	return "report last 7 days";
}
function formatNumber(number){
	return number_prefix +  $.number(number, decimal_digits, decimalSeparator, thousandSeparator) + number_suffix;		
	
}

