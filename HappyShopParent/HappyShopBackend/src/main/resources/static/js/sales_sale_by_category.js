
var data;
var chartOptions;

var totalGrossSales;
var totalNetSales;
var avgGrossSales;
var avgNetSales;
var totalItems;
$(document).ready(function() {
	initButtonSaleBy("_category", loadSaleReportByCategory);
	initButtonCustomTimeAndViewReportData("_category", loadSaleReportByCategory);		

});

function loadSaleReportByCategory(period){
	if(period == "custom"){
		startTime = $("#startTime_category").val();
		endTime = $("#endTime_category").val();
		url = contextPath + "reports/category/"+  startTime + "/" + endTime;				
	}else{
		url = contextPath + "reports/category/" + period;		
	}
	$.get(url, function(response){
		prepareDataForCategory(response);
		customizeChartForCategory(period);
		formatChartData(1,2);
		drawChartForCategory(period);		
	});
	
}

function prepareDataForCategory(responseJSON){
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Category');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');
	
	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalItems = 0;
	
	$.each(responseJSON, function(index, reportItem){
		data.addRows([   [reportItem.identifier, reportItem.grossSales, reportItem.netSales]   ]);
		totalGrossSales += reportItem.grossSales;
		totalNetSales += reportItem.netSales;	
		totalItems +=  reportItem.productsCount;
	});
}

function customizeChartForCategory(period){
	chartOptions = {
		title: getChartTitle(period),
		height: 360, legend: {position: 'right'}
	};
	
}

function drawChartForCategory(period){  
	var salesChart = new google.visualization.PieChart(document.getElementById('chart_div_category'));
	salesChart.draw(data, chartOptions);
	$("#textTotalGrossSales_category").text( formatNumber(totalGrossSales) );
	$("#textTotalNetSales_category").text( formatNumber(totalNetSales));
	$("#textTotalItems_category").text(totalItems);
	
	
	$("#textAvgGrossSales_category").text( formatNumber(totalGrossSales / getDenominator(period)) );
	$("#textAvgNetSales_category").text( formatNumber(totalNetSales / getDenominator(period)) );
	
}

