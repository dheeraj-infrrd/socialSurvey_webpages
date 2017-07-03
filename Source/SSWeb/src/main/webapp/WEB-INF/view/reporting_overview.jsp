<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set value="${detractor}" var="detractors"></c:set>
<c:set value="${passives}" var="passives"></c:set>
<c:set value="${promoters}" var="promoters"></c:set>
<c:set value="${Survey_sent}" var="surveysSent"></c:set>
<c:set value="${Survey_completed}" var="surveysCompleted"></c:set>
<c:set value="${Social_posts}" var="socialPosts"></c:set>
<c:set value="${Zillow_reviews}" var="zillowReviews"></c:set>

<style>
	.incomplete-trans-span{
		text-align:center;
		font-family: cursive;
		font-size: small;
		font-style: italic;
		font-variant: small-caps;
		font-weight: bold !important;
		line-height: 15px;
	}
</style>
<script src="${initParam.resourcesPath}/resources/js/googleloader.js"></script>
<div class="col-lg-3 col-md-3 col-sm-3">
	<c:if test="${detractors !=0 || passives !=0 || promoters !=0}">
		<jsp:include page="reporting_spsGauge.jsp"></jsp:include>
	</c:if>
	<c:if test="${detractors == 0 && passives == 0 && promoters == 0}">
		<div style="text-align:center; margin:30% auto">
			<span class="incomplete-trans-span">There are No</span>
			<div style="clear: both">
				<span class="incomplete-trans-span">Detractors, Passives and Promoters</span> 
			</div>
			<div style="clear:both">
				<span class="incomplete-trans-span">SPS score is 0</span>
			</div>
		</div>
	</c:if>
</div>
<div class="col-lg-3 col-md-3 col-sm-3"
	style="display: grid; margin-top: 20px;">
	<div style="display: inline-flex; margin-top: 10px">
		<div class="float-left dash-sel-lbl" style="text-align:center">Detractors</div>
		<div class="float-left dash-sel-lbl"
			style="width:${detractors}%;; height:65%; background:#E8341F; margin:auto -5px;"></div>
		<div class="float-left dash-sel-lbl" style="color: #E8341F;">${detractors}%</div>
	</div>
	<div style="display: inline-flex; margin-top: 10px">
		<div class="float-left dash-sel-lbl">Passives</div>
		<div class="float-left dash-sel-lbl"
			style="width:${passives}%; height:65%; background:#999999; margin:auto 2px"></div>
		<div class="float-left dash-sel-lbl" style="color: #999999;">${passives}%</div>
	</div>
	<div style="display: inline-flex; margin-top: 10px">
		<div class="float-left dash-sel-lbl">Promotors</div>
		<div class="float-left dash-sel-lbl"
			style="width:${promoters}%;; height:65%; background:#7ab400; margin:auto 2px"></div>
		<div class="float-left dash-sel-lbl" style="color: #7ab400;">${promoters}%</div>
	</div>
</div>

<div class="col-lg-3 col-md-3 col-sm-3 donut-chart">
	<c:if test="${total_incomplete_transactions > 0}">
		<jsp:include page="reporting_donutChart.jsp"></jsp:include>
	</c:if>
	<c:if test="${total_incomplete_transactions <= 0}">
		<div style="text-align:center; margin:30% auto">
			<span class="incomplete-trans-span">Congratulations</span>
			<div style="clear: both">
				<span class="incomplete-trans-span">You have No</span> 
				<span class="incomplete-trans-span">Incomplete Transactions</span>
			</div>
		</div>
	</c:if>
</div>

<div class="col-lg-3 col-md-3 col-sm-3">
	<div class="col-lg-6 col-md-6 col-sm-6" style="margin-top: 10px">
		<div class="row-lg-6 row-md-6 row-sm-6"
			style="font-weight: bold !important; font-size: medium">
			Surveys Sent
			<div style="font-size: -webkit-xxx-large">${surveysSent}</div>
		</div>
		<div class="row-lg-6 row-md-6 row-sm-6"
			style="font-weight: bold !important; font-size: medium">
			Surveys Completed
			<div style="font-size: -webkit-xxx-large">${surveysCompleted}</div>
		</div>
	</div>
	<div class="col-lg-6 col-md-6 col-sm-6" style="margin-top: 10px">
		<div class="row-lg-6 row-md-6 row-sm-6"
			style="font-weight: bold !important; font-size: medium">
			Social<br/>Posts
			<div style="font-size: -webkit-xxx-large">${socialPosts}</div>
		</div>
		<div class="row-lg-6 row-md-6 row-sm-6"
			style="font-weight: bold !important; font-size: medium;">
			Zillow Reviews
			<div style="font-size: -webkit-xxx-large">${zillowReviews}</div>
		</div>
	</div>
</div>
<div id="graphTabs" style="margin-top: 10px; display: inline-block; float:left; width:100%;height:500px">
	<ul class="nav nav-tabs" role="tablist">
		<li id="sps-stats-btn" class="active"><a href="#sps-stats-tab"
			data-toggle="tab">SPS Stats</a></li>
		<li id="average-rating-btn"><a href="#average-rating-tab"
			data-toggle="tab">Average Rating</a></li>
		<li id="completion-rate-btn"><a href="#completion-rate-tab"
			data-toggle="tab">Completion Rate</a></li>
	</ul>

	<div class="tab-content">
		<div class="tab-pane fade active in" id="sps-stats-tab" style="width:100%"><jsp:include
				page="reporting_spsStatsGraph.jsp"></jsp:include></div>
		<div class="tab-pane fade" id="average-rating-tab"><jsp:include
				page="reporting_averageRatingGraph.jsp"></jsp:include></div>
		<div class="tab-pane fade" id="completion-rate-tab"><jsp:include
				page="reporting_completionRateGraph.jsp"></jsp:include></div>
	</div>
</div>