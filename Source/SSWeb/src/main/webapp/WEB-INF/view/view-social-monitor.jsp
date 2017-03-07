<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="user"
	value="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal}" />

<body>
	<div class="hm-header-main-wrapper">
		<div class="container">
			<div class="hm-header-row hm-header-row-main clearfix">
				<div class="float-left hm-header-row-left text-center">
					<spring:message code="label.socialmonitor.key" />
				</div>
				<div class="float-right sm-header-row-left text-center" style="margin-right:5%">
					<spring:message code="label.lastbuild.key" /> : <span id="last-build-date"></span>
				</div>
			</div>
		</div>
	</div>
	<div class="container v-sm-container">
		<div class="clearfix">
			<div id="search-panel" class="float-left clearfix search-panel-right-sm" >
				<div id="hierarchy-selection-panel" class="float-left clearfix hierarchy-selection-panel v-um-header">
 					<select id="select-hierarchy-level" class="float-left dash-sel-item-sm">
						<option value="companyId" data-entity="company"><spring:message code="label.company.key" /></option>
						<c:if test='${ accounttype != "INDIVIDUAL" }'>
							<option value="userId" data-entity="user"><spring:message code="label.user.key" /></option>
							<option value="branchId" data-entity="office"><spring:message code="label.office.key" /></option>
							<option value="regionId" data-entity="region"><spring:message code="label.region.key" /></option>
						</c:if>
					</select>
				</div>
				<div id="entity-selection-panel" class="float-left clearfix hierarchy-selection-panel v-um-header select-hierarchy-level">
					<input id="select-entity-id" class="float-left dash-sel-item-sm v-ed-txt-dd" placeholder="<spring:message code="label.starttyping.key" />">
					<input type="hidden" name="entity-id" id="selected-entity-id-hidden"/>
				</div>
				<div class="v-um-hdr-right v-um-hdr-search-sm float-left clearfix search-panel-item v-um-header">
					<input id="post-search-query" name="post-search-query" class="v-um-inp" placeholder="<spring:message code="label.searchpost.key" />">
					<span id="sm-search-icn" class="um-search-icn" onclick="postsSearch();"></span>
				</div>
			
			
			</div>
			<div class="sm-btn-dl-sd-admin  search-panel-item v-um-header">
				<div id="dsh-dwnld-report-btn" class="sm-down-rep-button float-left cursor-pointer">
					<spring:message code="label.downloadsocialmonitordata.click" />
				</div>
				<select id="download-survey-reports" class="float-left dash-download-sel-item hide">
					<option value=3 data-report="social-monitor"><spring:message code="label.downloadsurveydata.three.key" /></option>
				</select>
				<input id="dsh-start-date" class="dsh-date-picker sm-date-picker-wid"  placeholder="<spring:message code="label.startdate.key" />">
				<span>-</span>
				<input id="dsh-end-date" class="dsh-date-picker sm-date-picker-wid" placeholder="<spring:message code="label.enddate.key" />">
			</div>
		</div>
		<div class="v-sm-tbl-wrapper" id="social-post-list">
			<div id="ppl-post-cont" class="rt-content-main  clearfix">
				<div class="float-left panel-tweet-wrapper">
					<div id="prof-posts" class="tweet-panel tweet-panel-left sm-tweet-panel">
						<!--  latest posts get populated here -->
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="temp-message" class="hide"></div>

	<script>
		$(document).ready(function() {
			hideOverlay();
			bindDatePickerforSurveyDownload();
			$(document).attr("title", "Social Monitor");
			var currentProfileName = "${columnName}";
			var currentProfileValue = "${columnValue}";
			setColDetails(currentProfileName, currentProfileValue, "${ user.company.companyId }");
			if ($('#server-message>div').hasClass("error-message")) {
				$('#server-message').show();
			}
			autocompleteData = [];
			getRelevantEntities();
			
			var lastBuild = new Date(Number("${ lastBuild }"));
			if(lastBuild != null){
				//lastBuild = convertUserDateToLocale(lastBuild);
				$("#last-build-date").html(lastBuild.toString(" ddd, MMMM d, yyyy hh:mm"));
			} else {
				$("#last-build-date").html("Last build date unavailable");
			}
			
			attachEventsOnSocialMonitor();
		});
	</script>