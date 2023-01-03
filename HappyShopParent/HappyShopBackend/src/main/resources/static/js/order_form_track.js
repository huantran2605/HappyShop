$(document).ready(function(){
	$("#addTrack").on("click", function(){//add track
		html = generateAddTrackHtml();
		$("#trackList").append(html);  
		setDescriptionWhenTrackAdded();
	});
	setDescriptionWhenStatusChanged();
	removeTrack();
});

function removeTrack(){
	$("#trackList").on("click", ".removeTrack", function(e){
		e.preventDefault();
		rowNumber = $(this).attr("rowNumber");
		$("#rowTrack" + rowNumber).remove();
		updateTrackDivCount();
	});
}

function updateTrackDivCount(){
	count = 1;
	$(".divTrackCount").each(function(){
		$(this).text(count);
		count++;
	})
}
function setDescriptionWhenTrackAdded(){
	rowNumber = $(".hiddenTrackId").length - 1;
	$("#trackNote" + rowNumber).text("Order was placed by the customer");
}

function setDescriptionWhenStatusChanged(){
	$("#trackList").on("change", ".dropDownStatus", function(){
		description = $("option:selected", this).attr("defaultDescription");
		rowNumber = $(this).attr("rowNumber");
		$("#trackNote" + rowNumber).text(description);
	});
}

function generateAddTrackHtml(){
	nextCount = $(".hiddenTrackId").length + 1;
	rowNumber = nextCount -1;
	rowId = 'rowTrack' + rowNumber;
	trackNoteId = 'trackNote' + rowNumber;
	dateTimeValue = formatCurrentDateTime();
	
	html = `<div class="row p-3 mt-3 border border-bottom"
					id="${rowId}">
					<input type="hidden" name="trackId" value="0" class="hiddenTrackId" />

					<div class="col-1">
						<span class="divTrackCount">${nextCount}</span>
						<a href="" class="fa-sharp fa-solid fa-trash fa-gray removeTrack" rowNumber="${rowNumber}"></a>
					</div>

					<div class="col-sm-10">
						<div class="row">
							<div class="col-sm-2">
								<label for="">Time: </label>
							</div>
							<div class="col-sm-5">
								<input type="datetime-local" name="trackDate"
									value="${dateTimeValue}" class="form-control" required/>
							</div>
						</div>

						<div class="row mt-3">
							<div class="col-sm-2">
								<label for="">Status: </label>
							</div>
							<div class="col-sm-5">
								<select name="trackStatus" class="form-control dropDownStatus" required rowNumber="${rowNumber}">`;
	
	html +=  $("#trackStatusOptions").clone().html();
	html += `</select>
							</div>
						</div>

						<div class="row mt-3">
							<div class="col-sm-2">
								<label for="">Notes: </label>
							</div>
							<div class="col-sm-5">
								<textarea rows="2" class="form-control" name="trackNote" id="${trackNoteId}" required></textarea>
							</div>
						</div>

					</div>
				</div>`;
	return html;
}

function formatCurrentDateTime() {
	date = new Date();
	year = date.getFullYear();
	month = date.getMonth() + 1;
	day = date.getDate();
	hour = date.getHours();
	minute = date.getMinutes();
	second = date.getSeconds();
	
	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;
	
	if (hour < 10) hour = "0" + hour;
	if (minute < 10) minute = "0" + minute;
	if (second < 10) second = "0" + second;
	
	return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;
	
}