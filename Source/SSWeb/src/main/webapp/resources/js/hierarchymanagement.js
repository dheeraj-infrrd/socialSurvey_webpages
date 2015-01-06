/**
 * js functions for hierarchy management
 */

/**
 * js function for adding a branch
 */
function addBranch(formId) {
	console.log("add branch called for form " + formId);
	var url = "./addbranch.do";
	callAjaxFormSubmit(url, addBranchCallBack, formId);
}

/**
 * call back function for add branch
 * 
 * @param data
 */
function addBranchCallBack(data) {
	console.log("Added branch successfully");
	showBranches();
	$(".create-branch-dd input[type='text']").val("");
}

function addOrUpdateBranch(formId) {
	/**
	 * if branch id is set in the form, branch needs an update else it is a new
	 * branch addition
	 */
	if ($("#branch-id-hidden").val().length > 0) {
		console.log("updating branch");
		updateBranch(formId);
	} else {
		console.log("adding new branch");
		addBranch(formId);
	}
}

/**
 * function to display branches
 */
function showBranches() {
	searchBranches("");
}

/**
 * call back function for displaying branches
 * 
 * @param data
 */
function showBranchesCallBack(data) {
	$("#existing-branches").html(data);

	// bind the click event of branches with edit
	$(".branch-element").click(function() {
		populateUpdateBranchForm(this);
	});
}

/**
 * Function to delete a branch
 * 
 * @param branchId
 */
function deleteBranch(branchId) {
	var url = "./deactivatebranch.do?branchId=" + branchId;
	console.log("delete branch url : " + url);
	callAjaxPOST(url, deleteBranchCallBack, true);
}

/**
 * Call back function for deleting a branch
 * 
 * @param data
 */
function deleteBranchCallBack(data) {
	showBranches();
}

/**
 * Method to populate the update form with selected branch contents
 * 
 * @param obj
 */
function populateUpdateBranchForm(obj) {
	$('#branch-name-txt').val($(obj).html());
	$("#selected-region-id-hidden").val($(obj).data('regionid'));
	$("#branch-id-hidden").val($(obj).data('branchid'));
	$('#selected-region-txt').val($(obj).data('regionname'));
}

/**
 * Method to update a branch
 * 
 * @param formId
 */
function updateBranch(formId) {
	var url = "./updatebranch.do";
	callAjaxFormSubmit(url, updateBranchCallBack, formId);
}

/**
 * Call back method after updating a branch
 * 
 * @param data
 */
function updateBranchCallBack(data) {
	showBranches();
	$(".create-branch-dd input[type='text']").val("");
	$(".create-branch-dd input[type='hidden']").val("");
}

/**
 * Method to perform search on solr for provided branch pattern
 * @param branchPattern
 */
function searchBranches(branchPattern) {
	var url = "./searchbranches.do?branchPattern="+branchPattern;
	callAjaxGET(url, searchBranchesCallBack, true);
}

/**
 * Callback method for search branches
 * 
 * @param data
 */
function searchBranchesCallBack(data) {
	console.log("search branches callback : "+data);
	var text =  $.parseJSON(data);
	if(text != null) {
		var searchResult = text.response.docs;
		if(searchResult != null) {
			var len = searchResult.length;
			var htmlData = "";
			console.log("searchResult is "+searchResult);
			if(len > 0) {
				$.each(searchResult,function(i,branch) {
					htmlData = htmlData +'<div class="hm-sub-item clearfix">';
					htmlData = htmlData +'<div class="float-left hm-sub-item-left branch-element" data-branchid = "'+branch.branchId+'" data-regionid = "'+branch.regionId+'" data-regionname = "'+branch.regionName+'">'+branch.branchName+'</div>';
					htmlData = htmlData +'<div class="float-right icn-remove cursor-pointer hm-item-height-adjust" onclick ="deleteBranchPopup('+branch.branchId+')"></div></div>';
				});
			}
			else {
				htmlData = 'No branches are added yet';
			}
			$("#existing-branches").html(htmlData);
			
			// bind the click event of branches with edit
			$(".branch-element").click(function() {
				populateUpdateBranchForm(this);
			});
		}
	
	}
}

/**
 * function to display regions
 */
function showRegions() {
	searchRegions("");
}

/**
 * js function for adding a region
 */
function addRegion(formId) {
	console.log("add region called for form " + formId);
	var url = "./addregion.do";
	callAjaxFormSubmit(url, addRegionCallBack, formId);
}

/**
 * call back function for add region
 * 
 * @param data
 */
function addRegionCallBack(data) {
	console.log("Added region successfully");
	showRegions();
	$(".create-branch-dd input[type='text']").val("");
}

/**
 * Method to add or update a region based on the hidden value of regionId in
 * form
 * 
 * @param formId
 */
function addOrUpdateRegion(formId) {
	if ($("#region-id-hidden").val().length > 0) {
		console.log("updating region");
		updateRegion(formId);
	} else {
		console.log("adding new region");
		addRegion(formId);
	}
}

/**
 * Function to delete a region
 * 
 * @param branchId
 */
function deleteRegion(regionId) {
	var url = "./deactivateregion.do?regionId=" + regionId;
	console.log("delete region url : " + url);
	callAjaxPOST(url, deleteRegionCallBack, true);
}

/**
 * Call back function for deleting a region
 * 
 * @param data
 */
function deleteRegionCallBack(data) {
	showRegions();
}

/**
 * Method to populate the update region form
 * 
 * @param obj
 */
function populateUpdateRegionForm(obj) {
	$('#region-name-txt').val($(obj).html());
	$("#region-id-hidden").val($(obj).data('regionid'));
}

/**
 * Method to update a region
 * 
 * @param formId
 */
function updateRegion(formId) {
	var url = "./updateregion.do";
	callAjaxFormSubmit(url, updateRegionCallBack, formId);
}

/**
 * Call back method to update a region
 * 
 * @param data
 */
function updateRegionCallBack(data) {
	showRegions();
	$(".create-branch-dd input[type='text']").val("");
	$(".create-branch-dd input[type='hidden']").val("");
}

/**
 * Region Delete popup overlay
 * 
 * @param regionId
 */
function deleteRegionPopup(regionId) {
	$('#regionid-to-delete-hidden').val(regionId);
	var urlCheck = "./checkbranchesinregion.do?regionId=" + regionId;
	console.log("Check for deactivation url : " + urlCheck);
	callAjaxPOST(urlCheck, deleteRegionCheckCallBack, true);
}


function deleteRegionCheckCallBack(response) {
	$("#serverSideerror").html(response);

	var success = "Selected Region could be deleted";
	var successMsg = $("#serverSideerror").find('.success-message').text().trim();
	if (success == successMsg) {
		createPopupConfirm("Remove Region", "Are you Sure?");
	} else {
		createPopupInfo("Remove Region");
	}
}


/**
 * Branch Delete popup overlay
 * 
 * @param branchId
 */
function deleteBranchPopup(branchId) {
	$('#branchid-to-delete-hidden').val(branchId);
	var urlCheck = "./checkusersinbranch.do?branchId=" + branchId;
	console.log("Check for deactivation url : " + urlCheck);
	callAjaxPOST(urlCheck, deleteBranchCheckCallBack, true);
}


function deleteBranchCheckCallBack(response) {
	$("#serverSideerror").html(response);
	
	var success = "Selected Branch could be deleted";
	var successMsg = $("#serverSideerror").find('.success-message').text().trim();
	if (success == successMsg) {
		createPopupConfirm("Remove Branch", "Are you Sure?");
	} else {
		createPopupInfo("Remove Branch");
	}
}


/**
 * Pop-up Overlay modifications
 * 
 * @param header
 * @param body
 */
function createPopupConfirm(header, body) {
	if ($('#overlay-continue').attr("disabled") == "disabled") {
		$('#overlay-continue').removeAttr("disabled");
	}
	$('#overlay-header').html(header);
	$('#overlay-txt').html(body);

	$('#overlay-confirm').show();
}

function createPopupInfo(header) {
	$('#overlay-continue').attr("disabled", true);
	$('#overlay-header').html(header);
	
   	$('#overlay-confirm').show();
}

$('#overlay-continue').click(function(){
	if ($('#overlay-continue').attr("disabled") != "disabled") {
		$('#overlay-confirm').hide();
		$("#serverSideerror").html('');
		
		var regionIdDelete = $('#regionid-to-delete-hidden').val();
		var branchIdDelete = $('#branchid-to-delete-hidden').val();
		if(regionIdDelete != "") {
			deleteRegion(regionIdDelete);
			$('#regionid-to-delete-hidden').val('');
		} else if(branchIdDelete != "") {
			deleteBranch(branchIdDelete);
			$('#branchid-to-delete-hidden').val('');
		}
	}
});
$('#overlay-cancel').click(function(){
	if ($('#overlay-continue').attr("disabled") == "disabled") {
		$('#overlay-continue').removeAttr("disabled");
	}
	$('#overlay-confirm').hide();
	$('#regionid-to-delete-hidden').val('');
	$('#branchid-to-delete-hidden').val('');
	$("#serverSideerror").html('');
});


/**
 * Branch details validation
 */

var isBranchValid;
$('#branch-name-txt').blur(function() {
	validateBranchName(this.id);
});
$('#branch-address1-txt').blur(function() {
	validateCompanyEnterpriseAddress1(this.id);
});
$('#branch-address2-txt').blur(function() {
	validateAddress2(this.id);
});

function validateBranchInformation(elementId) {
	isBranchValid = true;
	var isFocussed = false;
	
	if(!validateBranchName('branch-name-txt')){
		isBranchValid = false;
		if(!isFocussed){
			$('#branch-name-txt').focus();
			isFocussed=true;
		}
	}
	if(!validateCompanyEnterpriseAddress1('branch-address1-txt')){
		isBranchValid = false;
		if(!isFocussed){
			$('#branch-address1-txt').focus();
			isFocussed=true;
		}
	}
	if(!validateAddress2('branch-address2-txt')){
		isBranchValid = false;
		if(!isFocussed){
			$('#branch-address2-txt').focus();
			isFocussed=true;
		}
	}
	return isBranchValid;
}

$("#branch-save-icon").click(function() {
	if ($("#branch-save-icon").attr("disabled") != "disabled") {
		console.log("submitting branch information form");
		if (validateBranchInformation('add-branch-form')) {
			addOrUpdateBranch("add-branch-form");
		}
	}
});


/**
 * Region details validation
 */
var isRegionValid;
$('#region-name-txt').blur(function() {
	validateRegionName(this.id);
});
$('#region-address1-txt').blur(function() {
	validateCompanyEnterpriseAddress1(this.id);
});
$('#region-address2-txt').blur(function() {
	validateAddress2(this.id);
});

function validateRegionInformation(elementId) {
	isRegionValid = true;
	var isFocussed = false;
	
	if(!validateRegionName('region-name-txt')){
		isRegionValid = false;
		if(!isFocussed){
			$('#region-name-txt').focus();
			isFocussed=true;
		}
	}
	if(!validateCompanyEnterpriseAddress1('region-address1-txt')){
		isRegionValid = false;
		if(!isFocussed){
			$('#region-address1-txt').focus();
			isFocussed=true;
		}
	}
	if(!validateAddress2('region-address2-txt')){
		isRegionValid = false;
		if(!isFocussed){
			$('#region-address2-txt').focus();
			isFocussed=true;
		}
	}
	return isRegionValid;
}

/**
 * Method to fetch regions from solr for populating region selector
 * 
 * @param regionPattern
 */
function populateRegionsSelector(regionPattern) {
	console.log("Method populateRegionsSelector called for regionPattern : "+regionPattern);
	var url = "./searchregions.do?regionPattern="+regionPattern;
	callAjaxGET(url, populateRegionsSelectorCallBack, true);
}

/**
 * callback method for fetching regions from solr for populating region selector
 * @param data
 */
function populateRegionsSelectorCallBack(data) {
	console.log("populateRegionsSelectorCallBack : "+data);
	var text = $.parseJSON(data);
	if(text != null) {
		var searchResult = text.response.docs;
		if(searchResult != null) {
			var len = searchResult.length;
			var htmlData = "";
			console.log("searchResult is "+searchResult);
			if(len > 0) {
				$.each(searchResult,function(i,region) {
						htmlData = htmlData +'<div class="hm-dd-item" data-regionid="'+region.regionId+'">'+region.regionName+'</div>';
				});
				
				$("#hm-dd-wrapper-bottom").html(htmlData).slideDown(200);
				
				// bind the click event of selector
				$('.hm-dd-item').click(function() {
					$('#selected-region-txt').val($(this).html());
					$('#selected-region-id-hidden').val($(this).data('regionid'));
					$('#hm-dd-wrapper-bottom').slideToggle(200);
				});				
			}
		}
	}
}

/**
 * Method to search regions for provided pattern
 * 
 * @param regionPattern
 */
function searchRegions(regionPattern){
	console.log("search regions called for regionPattern : "+regionPattern);
	var url = "./searchregions.do?regionPattern="+regionPattern;
	callAjaxGET(url, searchRegionsCallBack, true); 
}

/**
 * call back for searching regions with provided pattern
 * @param data
 */
function searchRegionsCallBack(data) {
	var text =  $.parseJSON(data);
	if(text != null) {
		var searchResult = text.response.docs;
		if(searchResult != null) {
			var len = searchResult.length;
			var htmlData = "";
			console.log("searchResult is "+searchResult);
			if(len > 0) {
				htmlData = htmlData +'<input type="hidden" id="enable-branches-form" value="true">';
				$.each(searchResult,function(i,region) {
					htmlData = htmlData +'<div class="hm-sub-item clearfix">';
						htmlData = htmlData + '<div class="float-left hm-sub-item-left region-element" data-regionid = '+region.regionId+'>'+region.regionName+'</div>';
						htmlData = htmlData + '<div class="float-right icn-remove cursor-pointer hm-item-height-adjust" onclick=deleteRegionPopup('+region.regionId+')></div></div>';
				});
			}
			else {
				htmlData = 'No regions added yet';
			}
			
			$("#existing-regions").html(htmlData);
			
			//validations for enabling branches form
			if($("#enable-branches-form").length > 0) {
				$("#add-branch-form :input").prop("disabled", false);
				$("#branch-actions").children().attr("disabled", false);
			} else {
				$("#add-branch-form :input").prop("disabled", true);
				$("#branch-actions").children().attr("disabled", true);
			}
			
			// bind the click event of branches with edit
			$(".region-element").click(function() {
				populateUpdateRegionForm(this);
			});
		}
	}
}

$("#region-save-icon").click(function(e) {
	console.log("submitting region information form");
	if(validateRegionInformation('enterprise-branch-region')){
		addOrUpdateRegion("add-region-form");
	}
});

$('#selected-region-txt').click(function(){
	populateRegionsSelector("");
	$('#hm-dd-wrapper-bottom').slideToggle(200);
});

$("#selected-region-txt").focus(function() {
	var text = $("#selected-region-txt").val();
	if (text.length > 1) {
		delay(function() {
			populateRegionsSelector(text);
		}, 500);
	}
	else {
		$('#hm-dd-wrapper-bottom').slideUp(200);
	}
});

$("#selected-region-txt").keyup(function() {
	var text = $("#selected-region-txt").val();
	if (text.length > 1) {
		delay(function() {
			populateRegionsSelector(text);
		}, 500);
	}
	else {
		$('#hm-dd-wrapper-bottom').slideUp(200);
	}
});

$("#search-region-txt").focus(function() {
	var text = $("#search-region-txt").val();
	if (text.length > 1) {
		delay(function() {
			searchRegions(text);
		}, 500);
	}
	else {
		delay(function() {
			searchRegions("");
		}, 500);
	}
});

$("#search-region-txt").keyup(function() {
	var text = $("#search-region-txt").val();
	if (text.length > 1) {
		delay(function() {
			searchRegions(text);
		}, 500);
	}
	else {
		delay(function() {
			searchRegions("");
		}, 500);
	}
});

$("#search-branch-txt").keyup(function() {
	var text = $("#search-branch-txt").val();
	if (text.length > 1) {
		delay(function() {
			searchBranches(text);
		}, 500);
	}
	else {
		delay(function() {
			searchBranches("");
		}, 500);
	}
});

/**
 * function for adding delay to a function call
 */
var delay = (function() {
	var timer = 0;
	return function(callback, ms) {
		clearTimeout(timer);
		timer = setTimeout(callback, ms);
	};
})();