<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- in highest roles comparison, 1=companyAdmin, 2=regionAdmin, 3=branchAdmin, 4=agent, 5=no profile  -->
<c:set var="user" value="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal}" />

<!-- Account masters 1=Individual, 2=Team, 3=Company, 4=Enterprise, 5=Free Account -->
<c:set var="accountMasterId" value="${user.company.licenseDetails[0].accountsMaster.accountsMasterId}"/>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title><spring:message code="label.login.title.key" /></title>

	<link rel="shortcut icon" href="/favicon.ico" sizes="16x16">
	<link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/bootstrap.min.css">
	<link rel="stylesheet" href="${initParam.resourcesPath}/resources/jcrop/jquery.Jcrop.min.css">
	<link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/style.css">
	<link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/style-common.css">
	<link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/rangeslider.css">
	<link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/style-common-1.1.css">
	<link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/style-resp.css">
	<link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/style-resp-1.1.css">
	<link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/perfect-scrollbar.min.css">
</head>
<body>
	<div id="overlay-linkedin-import" class="overlay-login overlay-main hide"></div>
	<div id="overlay-send-survey" class="overlay-login overlay-main hide"></div>
	<div id="srv-req-pop" class="survey-request-popup-container hide">
		<div class="survey-request-popup"></div>
	</div>
	<div id="overlay-incomplete-survey"
		class="overlay-login overlay-main hide">
		<div class="welcome-popup-wrapper">
			<div class="welcome-popup-hdr-wrapper clearfix">
				<div class="float-left wc-hdr-txt">
					<spring:message code="label.header.incompletesurvey.key" />
				</div>
				<div class="float-right popup-close-icn" onclick="hideIncompleteSurveyListPopup();"></div>
			</div>
			<div id="icn-sur-popup-cont" data-start="0" data-total="0" data-batch="5" class="welcome-popup-body-wrapper clearfix icn-sur-popup-cont">
			</div>
			<div class="paginate-buttons-survey">
				<div id="sur-previous" class="float-left">&lt; Prev</div>
				<div id="sur-next" class="float-right">Next &gt;</div>
			</div>
		</div>
	</div>
	<div id="toast-container" class="toast-container">
	   <span id="overlay-toast" class="overlay-toast"></span>
    </div>
    <div class="overlay-payment hide" id="outer-payment"></div>
    <div class="overlay-loader hide"></div>
    <div id="message-header" class="hide"></div>
    
	<div id="overlay-main" class="overlay-main hide">
		<div class="overlay-disable-wrapper">
			<div id="overlay-header" class="ol-header">
				<!-- Populated by javascript -->
			</div>
			<div class="ol-content">
				<div id="overlay-text" class="ol-txt">
					<!-- Populated by javascript -->
				</div>
				<div class="clearfix">
					<div class="float-left ol-btn-wrapper">
						<div id="overlay-continue" class="ol-btn cursor-pointer">
							<!-- Populated by javascript -->
						</div>
					</div>
					<div class="float-left ol-btn-wrapper">
						<div id="overlay-cancel" class="ol-btn cursor-pointer">
							<!-- Populated by javascript -->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="report-abuse-overlay" class="overlay-main hide">
    	<div class="overlay-disable-wrapper">
    		<div id="overlay-header" class="ol-header">Why do you want to report the review?</div>
    		<div class="ol-content">
    			<textarea id="report-abuse-txtbox" class="report-abuse-txtbox" placeholder="Type here on why do you want to report the review...."></textarea>
    		</div>
    		<div class="rpa-overlay-btn-cont clearfix">
    			<div class="rpa-btn rpa-report-btn ol-btn cursor-pointer">Report</div>
    			<div class="rpa-btn rpa-cancel-btn ol-btn cursor-pointer">Cancel</div>
    		</div>
    	</div>
    </div>
	
	<div id="header-slider-wrapper" class="header-slider-wrapper">
		<div class="header-slider">
			<div id="header-links-slider" class="header-links header-links-slider float-left clearfix">
				<div class="header-links-item">
					<a id="dashboard-link" href="javascript:showMainContent('./dashboard.do')"><spring:message code="label.header.dashboard.key" /></a>
				</div>
				<c:if test="${(accountMasterId == 2 || accountMasterId == 3 || accountMasterId == 4) && (highestrole == 1 || highestrole == 2 || highestrole == 3)}">
					<div class="header-links-item">
						<a href="javascript:showMainContent('./showbuildhierarchypage.do')"><spring:message code="label.header.buildhierarchy.key" /></a>
					</div>
				</c:if>
				<c:if test="${highestrole == 1 && accountMasterId != 5}">
					<div class="header-links-item">
						<a href="javascript:showMainContent('./showbuildsurveypage.do')"><spring:message code="label.header.buildsurvey.key" /></a>
					</div>
				</c:if>
				<c:if test="${accountMasterId != 5}">
					<div class="header-links-item">
						<a href="javascript:showMainContent('./showcompanysettings.do')"><spring:message code="label.editsettings.key" /></a>
					</div>
				</c:if>
				<c:if test="${highestrole == 1 && accountMasterId != 5}">
					<div class="header-links-item">
						<a href="javascript:showMainContent('./viewhierarchy.do');"><spring:message code="label.viewcompanyhierachy.key" /></a>
					</div>
				</c:if>
				<c:if test="${accountMasterId > 1 && accountMasterId <5 && highestrole != 4}">
					<div class="header-links-item">
						<a href="javascript:showMainContent('./showusermangementpage.do')"><spring:message code="label.header.usermanagement.key" /></a>
					</div>
				</c:if>
				<c:if test="${accountMasterId < 4}">
					<div class="header-links-item">
						<a href="javascript:showMainContent('./upgradepage.do')"><spring:message code="label.header.upgrade.key" /></a>
					</div>
				</c:if>
				<div class="header-links-item">
					<a href="javascript:showMainContent('./showprofilepage.do')"><spring:message code="label.editprofile.key" /></a>
				</div>
				<div class="header-links-item" >
					<a href="javascript:showMainContent('./showchangepasswordpage.do')"><spring:message code="label.changepassword.key"/></a>
				</div>
				<div class="header-links-item" >
					<a href="j_spring_security_logout"><spring:message code="label.logout.key" /></a>
				</div>
			</div>
		</div>
	</div>

	<div class="hdr-wrapper">
		<div class="container hdr-container clearfix">
			<div class="float-left hdr-logo cursor-pointer"></div>
			<div class="float-left hdr-links clearfix">
				<div class="hdr-link-item hdr-link-active">
					<a id="dashboard-link" href="javascript:showMainContent('./dashboard.do')" onclick="showOverlay();"><spring:message code="label.header.dashboard.key" /></a>
				</div>
				<c:if test="${accountMasterId > 1 && accountMasterId < 5 && highestrole != 4 }">
					<div class="hdr-link-item">
						<a href="javascript:showMainContent('./showusermangementpage.do')" onclick="showOverlay();"><spring:message code="label.header.usermanagement.key" /></a>
					</div>
				</c:if>
				<%-- <c:if test="${(accountMasterId == 2 || accountMasterId == 3 || accountMasterId == 4) && (highestrole == 1 || highestrole == 2 || highestrole == 3)}">
					<div class="hdr-link-item">
						<a href="javascript:showMainContent('./showbuildhierarchypage.do')" onclick="showOverlay();"><spring:message code="label.header.buildhierarchy.key" /></a>
					</div>
				</c:if> --%>
				<c:if test="${highestrole == 1 && accountMasterId != 5}">
					<div class="hdr-link-item">
						<a href="javascript:showMainContent('./showbuildsurveypage.do')" onclick="showOverlay();"><spring:message code="label.header.buildsurvey.key" /></a>
					</div>
				</c:if>
				<c:if test="${accountMasterId != 5}">
					<div class="hdr-link-item">
						<a href="javascript:showMainContent('./showcompanysettings.do')" onclick="showOverlay();"><spring:message code="label.editsettings.key" /></a>
					</div>
				</c:if>
				<div class="hdr-link-item">
					<a href="javascript:showMainContent('./showprofilepage.do')" onclick="showOverlay();"><spring:message code="label.editprofile.key" /></a>
				</div>
			</div>
			<div id="header-menu-icn" class="header-menu-icn icn-menu hide float-right"></div>
			<div id="header-user-info" class="header-user-info float-right clearfix">
				<div id="hdr-usr-img" class="float-right user-info-initial">
					<span id="usr-initl">${fn:substring(user.firstName, 0, 1)}</span>
					<div class="initial-dd-wrapper hide blue-arrow-bot text-normal">
						<%-- <c:if test="${accountMasterId != 5}">
							<div class="initial-dd-item" id="company-setting" onclick="showMainContent('./showcompanysettings.do'); showOverlay();">
								<spring:message code="label.editsettings.key" />
							</div>
						</c:if> --%>
						<%-- <div class="initial-dd-item" id="profile-setting" onclick="showMainContent('./showprofilepage.do'); showOverlay();">
							<spring:message code="label.editprofile.key" />
						</div> --%>
						<div class="initial-dd-item" id="change-password" onclick="showMainContent('./showchangepasswordpage.do'); showOverlay();">
							<spring:message code="label.changepassword.key"/>
						</div>
						<c:if test="${accountMasterId < 4}">
							<div class="initial-dd-item" id="upgrade-plan" onclick="showMainContent('./upgradepage.do')">
								<spring:message	code="label.header.upgrade.key" />
							</div>
						</c:if>
						<c:if test="${accountMasterId == 5}">
							<div class="initial-dd-item" id="upgrade-plan" onclick="showMainContent('./upgradetopaidplanpage.do')">
								<spring:message	code="label.header.upgrade.key" />
							</div>
						</c:if>
						<c:choose>
							<c:when test="${not empty realTechAdminId }">
								<div class="initial-dd-item" onclick="userSwitchToAdmin();">
									<spring:message code="label.switch.key" />
								</div>
							</c:when>
							<c:otherwise>
								<div class="initial-dd-item" onclick="userLogout();">
									<spring:message code="label.logout.key" />
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
                <c:if test="${displaylogo != null}">
					<div class="float-left user-info-seperator"></div>
					<div class="float-left user-info-logo"
						style="background: url(${displaylogo}) no-repeat center; background-size: contain;"></div>
				</c:if>
			</div>
		</div>
	</div>
	<script>
		function userLogout(){
			window.location.href = 'j_spring_security_logout';
		}
	</script>
