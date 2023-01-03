var productDetailCount;
$(document).ready(function() {
	productDetailCount = $(".hiddenProductId").length;
	$("#linkAddProduct").on("click", function(e) {
		e.preventDefault();
		url = $(this).attr("href");
		$("#addProductModal").find("iframe").attr("src", url);
		$("#addProductModal").modal("show");  
	})
});

function addProduct(productId){
	
	getShippingCost(productId)
}

function getShippingCost(productId) {
	countryId = $("#country option:selected").val();
	state = $("#stateField").val();
	if (state == null) {
		state = $("city").val();
	}
	requestUrl = contextPath + "get_shipping_cost";

	$.ajax({
		url: requestUrl,
		type: 'POST',
		data: {
			 _csrf: csrfValue,
			productId: productId,
			countryId: countryId,
			state: state
			}
		}).done(function(shippingCost) {
			getProductInfo(productId, shippingCost);
		}).fail(function(err){
			showModal("Warning", err.responseJSON.message);
			shippingCost = 0.0;
			getProductInfo(productId, shippingCost);
		}).always(function(){
			$("#addProductModal").modal("hide");  
		});
}

function getProductInfo(productId, shippingCost){
	url = contextPath + "products/get/" + productId;
	$.get(url, function(response) {
		imagePath = contextPath.substring(0, contextPath.length - 1) + response.imagePath;
		name = response.name;
		shortName = response.shortName;
		cost =  $.number(response.cost, 2);
		quantity = response.quantity;
		price = $.number(response.price, 2);
		html = generateProductCode(productId, imagePath, name, shortName, cost, quantity, price, shippingCost);
		$("#productList").append(html);
		checkRemoveProductIcon();
		 updateOrderAmounts();
	});
}

function generateProductCode(productId, imagePath, name, shortName, cost, quantity, price, shippingCost){
	nextCount = productDetailCount + 1;
	productDetailCount++;
	rowNumber = nextCount - 1;
	rowId =  "row" + rowNumber;
	quantityId = "quantity" + rowNumber;
	warningId =  'warning' + rowNumber;
	priceId =  'unitPrice' + rowNumber;
	subTotalId = "subTotal" + rowNumber;
	
	
	htmlCode = `
				<div class="row p-3 mt-3 border border-bottom" id="${rowId}" >
				<input type="hidden" name="detailId" value="0"/>
				<input type="hidden" class="hiddenProductId" name="productId" value="${productId}"/>
				<div class="col-1">
					<span class="divCount">	${nextCount}</span>
					<a href="" class="fa-sharp fa-solid fa-trash fa-gray removeProduct" rowNumber="${rowNumber}"></a>
				</div>
				<div class="col-3">
					<img src="${imagePath}" class="img-fluid" width="150"/>
				</div>
				<div class="m-1">
					<b title="${name}">${shortName}</b>
				</div>
				<div class="form-group row mt-3">
					<label class="col-sm-2" >Product Cost:</label>
					<div class="col-sm-3">
						<input class="form-control cost-input" 
							name="productDetailCost"
							rowNumber ="${rowNumber}"
							type="text" value="${cost}"> 
					</div>
				</div>
				<div class="form-group row mt-3">
					<label class="col-sm-2" >Quantity:</label>
					<div class="col-sm-3">
						<input class="form-control quantity-input" type="number" 
							name="productQuantity"
						 	rowNumber ="${rowNumber}" maxQuantity="${quantity}"
						 	min="1" value="1" id="${quantityId}" > 
						<small id="${warningId}" class="text-warning"></small>
					</div>
				</div>
				<div class="form-group row mt-3">
					<label class="col-sm-2" >Unit Price:</label>
					<div class="col-sm-3">
						<input class="form-control price-input" id="${priceId}"
							name="productPrice"
							 rowNumber ="${rowNumber}"  type="text" value="${price}"> 
					</div>
				</div>
				<div class="form-group row mt-3">
					<label class="col-sm-2" >Sub Total:</label>
					<div class="col-sm-3">
						<input class="form-control subtotal-output"
							name="productSubTotal"
							 rowNumber ="${rowNumber}" id="${subTotalId}"
							 readonly type="text" value="${price}">   
					</div>
				</div>
				<div class="form-group row mt-3">
					<label class="col-sm-2" >Shipping Cost:</label>
					<div class="col-sm-3">
						<input class="form-control ship-input" rowNumber ="${rowNumber}"
							name="productShipCost"
							type="text" value="${shippingCost}"> 
					</div>
				</div>
			</div>`;
			
		return htmlCode;
}

function checkRemoveProductIcon(){
	if($(".divCount").length > 1){
		$(".removeProduct").show();
	}
	else{
		$(".removeProduct").hide();
	}			
}

function isProductExistedAdded(productId){
	existed = false;
	$(".hiddenProductId").each(function(){
		if(productId == $(this).val()){
			existed = true;
			return;
		}	
	})
	return existed;
}