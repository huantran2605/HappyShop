var chartOptions;
var data;

var totalGrossSales;
var totalNetSales;
var avgGrossSales;
var avgNetSales;
var ordersCount;


$(document).ready(function() {
	initButtonSaleBy("_date", loadSaleReportByDate);
	initButtonCustomTimeAndViewReportData("_date", loadSaleReportByDate);		

});

function loadSaleReportByDate(period){
	if(period == "custom"){
		startTime = $("#startTime_date").val();
		endTime = $("#endTime_date").val();
		url = contextPath + "reports/sale_by_date/"+  startTime + "/" + endTime;				
	}else{
		url = contextPath + "reports/sale_by_date/" + period;		
	}
	$.get(url, function(response){
		prepareData(response);
		customizeChart(period);
		formatChartData(1,2);
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

}

function drawChart(period){  
	var salesChart = new google.visualization.ColumnChart(document.getElementById('chart_div_date'));
	salesChart.draw(data, chartOptions);
	$("#textTotalGrossSales_date").text( formatNumber(totalGrossSales) );
	$("#textTotalNetSales_date").text( formatNumber(totalNetSales));
	$("#textTotalItems_date").text(ordersCount);
	
	
	$("#textAvgGrossSales_date").text( formatNumber(totalGrossSales / getDenominator(period)) );
	$("#textAvgNetSales_date").text( formatNumber(totalNetSales / getDenominator(period)) );
	
}



