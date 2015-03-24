<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="user" value="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal}" />
<c:set var="accountType" value="${user.company.licenseDetails[0].accountsMaster.accountName}" />
<form id="edit-individual-form">
	<div id="bd-assign-to" class="bd-hr-form-item clearfix">
	    <div class="float-left bd-frm-left"><spring:message code="label.assignto.key"/></div>
	    <div id="assign-to-selector" class="float-left bd-frm-right pos-relative">
	    <c:choose>
		    <c:when test="${accountType == 'Enterprise'}">
		        <input id="assign-to-txt" data-assignto="office" value='<spring:message code="label.office.key"/>' class="bd-frm-rt-txt bd-frm-rt-dd bd-frm-rt-dd-adj dd-com-main bd-dd-img ignore-clear"/>
		        <div id="assign-to-droplist" class="bd-frm-rt-dd-list dd-com-list hide">
		        	<div data-assign-to-option="office" class="bd-frm-rt-dd-item dd-com-item hm-dd-hover hm-assignto-options"><spring:message code="label.office.key"/></div>
		            <c:if test="${highestrole == 1 || highestrole == 2}">
		            	<div data-assign-to-option="region" class="bd-frm-rt-dd-item dd-com-item hm-dd-hover hm-assignto-options"><spring:message code="label.region.key"/></div>
		            </c:if>
		            <c:if test="${highestrole == 1}">
		            	<div data-assign-to-option="company" class="bd-frm-rt-dd-item dd-com-item hm-dd-hover hm-assignto-options"><spring:message code="label.company.key"/></div>
		            </c:if>
		        </div>
	        </c:when>
	        <c:when test="${accountType == 'Company'}">
		    	<input id="assign-to-txt" data-assignto="office" value='<spring:message code="label.office.key"/>' class="bd-frm-rt-txt bd-frm-rt-dd bd-frm-rt-dd-adj dd-com-main bd-dd-img ignore-clear"/>
	        	<div id="assign-to-droplist" class="bd-frm-rt-dd-list dd-com-list hide">
	        		<div data-assign-to-option="office" class="bd-frm-rt-dd-item dd-com-item hm-dd-hover hm-assignto-options"><spring:message code="label.office.key"/></div>
	            	<c:if test="${highestrole == 1}">
	            		<div data-assign-to-option="company" class="bd-frm-rt-dd-item dd-com-item hm-dd-hover hm-assignto-options"><spring:message code="label.company.key"/></div>
	            	</c:if>
	        	</div>
		    </c:when>
		    <c:when test="${accountType == 'Team'}">
		    	<input id="assign-to-txt" data-assignto="company" value='<spring:message code="label.team.key"/>' class="bd-frm-rt-txt bd-frm-rt-dd bd-frm-rt-dd-adj dd-com-main bd-dd-img ignore-clear"/>
	        	<div id="assign-to-droplist" class="bd-frm-rt-dd-list dd-com-list hide">
	            	<div data-assign-to-option="company" class="bd-frm-rt-dd-item dd-com-item hm-dd-hover hm-assignto-options"><spring:message code="label.team.key"/></div>
	        	</div>
		    </c:when>
	   </c:choose>
	    </div>
	</div>
	<div id="bd-region-selector" class="bd-hr-form-item clearfix hide">
	    <div class="float-left bd-frm-left"><spring:message code="label.selectregion.key"/></div>
	    <div class="float-left bd-frm-right pos-relative" id="region-selector">
	        <input id="selected-region-txt" class="bd-frm-rt-txt bd-frm-rt-dd bd-frm-rt-dd-adj dd-com-main bd-dd-img clear" placeholder='<spring:message code="label.regionselector.placeholder.key"/>'/>
	        <input type="hidden" name="regionId" id="selected-region-id-hidden"/>
	        <div id="regions-droplist" class="bd-frm-rt-dd-list dd-com-list hide">
	            <!-- regions list get populated here -->
	        </div>
	    </div>
	</div>
	<div id="bd-office-selector" class="bd-hr-form-item clearfix">
	    <div class="float-left bd-frm-left"><spring:message code="label.selectoffice.key"/></div>
	    <div class="float-left bd-frm-right pos-relative" id="office-selector">
	        <input id="selected-office-txt" class="bd-frm-rt-txt bd-frm-rt-dd bd-frm-rt-dd-adj dd-com-main bd-dd-img" placeholder='<spring:message code="label.officeselector.placeholder.key"/>'/>
	        <input type="hidden" name="officeId" id="selected-office-id-hidden"/>
	        <div id="offices-droplist" class="bd-frm-rt-dd-list dd-com-list hide">
	            <!-- offices list get populated here  -->
	        </div>
	    </div>
	</div>
	<div class="bd-hr-form-item clearfix">
	    <div class="float-left bd-frm-left"></div>
	    <div class="float-left bd-frm-right">
	        <div class="bd-frm-rad-wrapper clearfix">
	            <div class="float-left bd-cust-rad-item clearfix">
	                <div data-type="single" class="float-left bd-cust-rad-img bd-cust-rad-img-checked"></div>
	                <div class="float-left bd-cust-rad-txt"><spring:message code="label.addsingleuser.key"/></div>
	            </div>
	            <div class="float-left bd-cust-rad-item bd-cust-rad-item-adj clearfix">
	                <div data-type="multiple" class="float-left bd-cust-rad-img"></div>
	                <div class="float-left bd-cust-rad-txt"><spring:message code="label.addmultipleusers.key"/></div>
	            </div>
	        </div>
	    </div>
	</div>
	<input type="hidden" data-user-selection-type="single" id="user-selection-info"/>
	<div id="bd-multiple" class="bd-hr-form-item clearfix hide">
	    <div class="float-left bd-frm-left"><spring:message code="label.addmultipleusers.key"/></div>
	    <div class="float-left bd-frm-right">
	        <textarea class="bd-frm-rt-txt-area" id="selected-user-txt-area" name="selectedUserEmailArray" placeholder='<spring:message code="label.addmultipleemailids.key"/>'></textarea>
	    </div>
	</div>
	<div id="bd-single" class="bd-hr-form-item clearfix">
	    <div class="float-left bd-frm-left"><spring:message code="label.adduser.key"/></div>
	    <div class="float-left bd-frm-right pos-relative">
	        <input autocomplete="off" id="selected-user-txt" name="selectedUserEmail" class="bd-frm-rt-txt bd-frm-rt-dd bd-frm-rt-dd-adj dd-com-main bd-dd-img" placeholder='<spring:message code="label.userselector.placeholder.key"/>'/>
	        <input type="hidden" name="selectedUserId" id="selected-userid-hidden">
	        <div id="users-droplist" class="bd-frm-rt-dd-list dd-com-list hide">
	            <!-- list of users from solr gets displayed here   -->
	        </div>
	    </div>
	</div>
	<div class="bd-hr-form-item clearfix" id="admin-privilege-div">
	    <div class="float-left bd-frm-left"></div>
	    <div class="float-left bd-frm-right">
	        <div class="bd-frm-check-wrapper clearfix">
	            <div class="float-left bd-check-img"></div>
	            <input type="hidden" name="isAdmin" value="true" id="is-admin-chk">
	            <div class="float-left bd-check-txt"><spring:message code="label.grantadminprivileges.key"/></div>
	        </div>
	    </div>
	</div>
	<div class="bd-hr-form-item clearfix">
	    <div class="float-left bd-frm-left"></div>
	    <div class="float-left bd-frm-right">
	        <div class="bd-btn-save cursor-pointer" id="btn-individual-save"><spring:message code="label.save.key"/></div>
	    </div>
	</div>
</form>