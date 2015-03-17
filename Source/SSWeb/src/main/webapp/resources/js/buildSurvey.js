// Existing Survey Questions 
function commonActiveSurveyCallback(response){
	showInfo(response);
	$('#bs-ques-wrapper').html('');
	loadActiveSurveyQuestions();
}

function loadActiveSurveyQuestions() {
	var url = "./getactivesurveyquestions.do";
	callAjaxGET(url, populateActiveSurveyQuestions, true);
}

function populateActiveSurveyQuestions(response) {
	var surveyDetail = $.parseJSON(response);
	var surveyQuestions = surveyDetail.questions;
	var htmlData = "";

	if (surveyQuestions != null) {
		var countQues = 1;
		
		// For Each Question
		$.each(surveyQuestions, function(i, surveyQuestion) {
			// Question start
			htmlData = htmlData + '<div class="bd-srv-tbl-row clearfix" data-questionid="' + surveyQuestion.questionId + '">';

			// Question order
			htmlData = htmlData + '<div class="float-left srv-tbl-num"><span>' + surveyQuestion.questionOrder + '</span></div>';
			
			// Question Text
			var questionTypeCode = surveyQuestion.questionType.trim();
			htmlData = htmlData + '<div class="float-left srv-tbl-txt" q-type="' + questionTypeCode + '">' + surveyQuestion.question + '</div>';

			// Buttons
			htmlData = htmlData
				+ '<div class="float-right srv-tbl-rem">Remove</div>'
				+ '<div class="float-right srv-tbl-edit">Edit</div>';
			
			// Question End
			htmlData = htmlData + '</div>';
			
			countQues++;
		});
		
		$('#bs-ques-wrapper').html(htmlData);
	} else {
		$('#bs-ques-wrapper').html('');
	}
}

$(document).on('mouseover', '.bd-srv-tbl-row', function() {
	$(this).addClass('bd-srv-tbl-row-hover');
	$(this).find('.srv-tbl-rem').show();
	$(this).find('.srv-tbl-edit').show();
});

$(document).on('mouseout', '.bd-srv-tbl-row', function() {
	$(this).removeClass('bd-srv-tbl-row-hover');
	$(this).find('.srv-tbl-rem').hide();
	$(this).find('.srv-tbl-edit').hide();
});

// Add Survey Question overlay
$('#btn-add-question').click(function() {
	$('#bd-srv-pu').show();
	$('body').addClass('body-no-scroll');
});

$('.bd-q-btn-cancel').click(function() {
	$('#bd-srv-pu').hide();
	$('body').removeClass('body-no-scroll');
});

$('.bd-q-btn-done').click(function() {
	$('#bd-srv-pu').hide();
	$('body').removeClass('body-no-scroll');
});

// Remove question from survey
$('body').on('click', '.srv-tbl-rem', function(){
	var questionId = $(this).parent().data('questionid');
	var url = "./removequestionfromsurvey.do?questionId=" + questionId;
	
	createPopupConfirm("Delete Question");
	$('body').on('click', '#overlay-continue', function(){
		callAjaxPOST(url, commonActiveSurveyCallback, true);
		
		overlayRevert();
		$('#overlay-continue').unbind('click');
	});
});

// Select question type
$(document).on('click', '.bd-tab-rat', function() {
	$(this).parent().find('.bd-ans-tab-item').removeClass('bd-ans-tab-sel');
	$(this).addClass('bd-ans-tab-sel');
	$(this).parent().parent().parent().find('.bd-ans-type-item').hide();
	$(this).parent().parent().parent().find('.bd-ans-type-rating').show();
});

$(document).on('click', '.bd-tab-mcq', function() {
	$(this).parent().find('.bd-ans-tab-item').removeClass('bd-ans-tab-sel');
	$(this).addClass('bd-ans-tab-sel');
	$(this).parent().parent().parent().find('.bd-ans-type-item').hide();
	$(this).parent().parent().parent().find('.bd-ans-type-mcq').show();
});

$(document).on('click', '.bd-tab-com', function() {
	$(this).parent().find('.bd-ans-tab-item').removeClass('bd-ans-tab-sel');
	$(this).addClass('bd-ans-tab-sel');
	$(this).parent().parent().parent().find('.bd-ans-type-item').hide();
	$(this).parent().parent().parent().find('.bd-ans-type-com').show();
});

$(document).on('click', '.bd-com-chk', function() {
	if ($(this).hasClass('bd-com-unchk')) {
		$(this).removeClass('bd-com-unchk');
	} else {
		$(this).addClass('bd-com-unchk');
	}
});

$(document).on('click', '.bd-ans-img-wrapper', function() {
	$(this).parent().parent().find('.bd-ans-img').addClass('bd-img-sel');
	$(this).find('.bd-ans-img').removeClass('bd-img-sel');
});

// Add another question
var newQuestTemplateWithTopTxt = '<div class="bd-quest-item hide">'
	+ '<div class="bd-q-pu-header clearfix">'
		+ '<div class="float-left bd-q-pu-header-lft">I Would Like To Add Another Question</div>'
	+ '</div>'
	+ '<div class="bd-q-pu-txt-wrapper pos-relative">'
		+ '<input class="bd-q-pu-txt" data-nextquest="false" data-qno="2">'
		+ '<div class="bd-q-pu-close hide"></div>'
	+ '</div>'
	+ '<div class="bs-ans-wrapper hide">'
		+ '<div class="bd-and-header-txt">I want my customer replying using</div>'
		+ '<div class="bd-ans-options-wrapper">'
			+ '<div class="bd-ans-header clearfix">'
				+ '<div class="bd-ans-hd-container clearfix float-left">'
					+ '<div id="" class="bd-tab-rat float-left bd-ans-tab-item bd-ans-tab-sel">Rating</div>'
					+ '<div id="" class="bd-tab-com float-left bd-ans-tab-item">Comment</div>'
					+ '<div id="" class="bd-tab-mcq float-left bd-ans-tab-item">Mutiple Choice</div>'
				+ '</div>'
			+ '</div>'
			+ '<div id="" class="bd-ans-type-rating bd-ans-type-item">'
				+ '<div class="bd-and-tier2">My Customers can answer using</div>'
				+ '<div class="row clearfix bd-ans-type bd-ans-type-rating-adj">'
					+ '<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">'
						+ '<div class="bd-ans-img-wrapper">'
							+ '<div class="bd-ans-img bd-ans-smiley"></div>'
							+ '<div class="bd-ans-img-txt">Smiley</div>'
						+ '</div>'
					+ '</div>'
					+ '<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">'
						+ '<div class="bd-ans-img-wrapper">'
							+ '<div class="bd-ans-img bd-ans-star"></div>'
							+ '<div class="bd-ans-img-txt">Stars</div>'
						+ '</div>'
					+ '</div>'
					+ '<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">'
						+ '<div class="bd-ans-img-wrapper">'
							+ '<div class="bd-ans-img bd-ans-scale"></div>'
							+ '<div class="bd-ans-img-txt">Scale</div>'
						+ '</div>'
					+ '</div>'
				+ '</div>'
			+ '</div>'
			+ '<div id="" class="bd-ans-type-mcq bd-ans-type-item hide">'
				+ '<div class="bd-and-tier2">My Customers can answer from</div>'
				+ '<div class="clearfix bd-ans-type bd-ans-type-mcq-adj">'
					+ '<div class="bd-mcq-row clearfix">'
						+ '<div class="float-left bd-mcq-lbl">Option</div>'
						+ '<input class="float-left bd-mcq-txt">'
						+ '<div class="float-left bd-mcq-close"></div>'
					+ '</div>'
					+ '<div class="bd-mcq-row clearfix">'
						+ '<div class="float-left bd-mcq-lbl">Option</div>'
						+ '<input class="float-left bd-mcq-txt">'
						+ '<div class="float-left bd-mcq-close"></div>'
						+ '</div>'
					+ '</div>'
				+ '</div>'
				+ '<div id="" class="bd-ans-type-com bd-ans-type-item hide">'
					+ '<div class="clearfix bd-com-wrapper">'
						+ '<div class="float-left bd-com-chk"></div>'
						+ '<div class="float-left bd-com-txt">Textarea</div>'
					+ '</div>'
				+ '</div>'
			+ '</div>'
		+ '</div>'
	+ '</div>';
$(document).on("input", '.bd-q-pu-txt', function() {
	if ($(this).val().trim().length > 0) {
		$(this).parent().next('.bs-ans-wrapper').show();
		if ($(this).data('nextquest') == false) {
			$(this).parent().parent().after(newQuestTemplateWithTopTxt);
			$(this).parent().parent().next('.bd-quest-item').show();
			$(this).data('nextquest', 'true');
		}
	}
	// else{
	// $(this).parent().next('.bs-ans-wrapper').hide();
	// $(this).parent().parent().next('.bd-quest-item').hide();
	// }
	if ($(this).data('qno') != '1') {
		$(this).next('.bd-q-pu-close').show();
	}
});

$(document).on('click', '.bd-q-pu-close', function() {
	$(this).parent().parent().remove();
});

// Overlay Popup
function createPopupConfirm(header) {
	$('#overlay-header').html(header);
	$('#overlay-continue').html('Continue');
	$('#overlay-cancel').html('Cancel');

	$('#overlay-main').show();
}
function overlayRevert() {
	$('#overlay-main').hide();
	$("#overlay-header").html('');
	$("#overlay-text").html('');
	$('#overlay-continue').html('');
	$('#overlay-cancel').html('');
}
$('body').on('click', '#overlay-cancel', function(){
	$('#overlay-continue').unbind('click');
	overlayRevert();
});