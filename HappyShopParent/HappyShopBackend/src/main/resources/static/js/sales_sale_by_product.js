
var data;
var chartOptions;

var totalGrossSales;
var totalNetSales;
var avgGrossSales;
var avgNetSales;
var totalItems;
$(document).ready(function() {
	initButtonSaleBy("_product", loadSaleReportByProduct);
	initButtonCustomTimeAndViewReportData("_product", loadSaleReportByProduct);		

});

function loadSaleReportByProduct(period){
	if(period == "custom"){
		startTime = $("#startTime_product").val();
		endTime = $("#endTime_product").val();
		url = contextPath + "reports/product/"+  startTime + "/" + endTime;				
	}else{
		url = contextPath + "reports/product/" + period;		
	}
	$.get(url, function(response){
		prepareDataForProduct(response);
		customizeChartForProduct(period);
		formatChartData(2,3);
		drawChartForProduct(period);		
	});
	
}

function prepareDataForProduct(responseJSON){
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Product');
	data.addColumn('number', 'Quantity');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');
	
	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalItems = 0;
	
	$.each(responseJSON, function(index, reportItem){
		data.addRows([   [reportItem.identifier,reportItem.productsCount, reportItem.grossSales, reportItem.netSales]   ]);
		totalGrossSales += reportItem.grossSales;
		totalNetSales += reportItem.netSales;	
		totalItems +=  reportItem.productsCount;
	});
}

function customizeChartForProduct(period){
	chartOptions = {
		title: getChartTitle(period),
		showRowNumber: true, width: '80%', height: '100%',
		page:'enable'
	};
	
}

function drawChartForProduct(period){  
	var salesChart = new google.visualization.Table(document.getElementById('chart_div_product'));
	salesChart.draw(data, chartOptions);
	$("#textTotalGrossSales_product").text( formatNumber(totalGrossSales) );
	$("#textTotalNetSales_product").text( formatNumber(totalNetSales));
	$("#textTotalItems_product").text(totalItems);
	
	
	$("#textAvgGrossSales_product").text( formatNumber(totalGrossSales / getDenominator(period)) );
	$("#textAvgNetSales_product").text( formatNumber(totalNetSales / getDenominator(period)) );
	
}

