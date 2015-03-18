<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="hm-header-main-wrapper" class="hm-header-main-wrapper">
    <div class="container">
        <div class="hm-header-row hm-header-row-main clearfix">
            <div class="float-left hm-header-row-left text-center"><spring:message code="label.buildcompanyhierarchy.key"/></div>
        </div>
    </div>
</div>
<div id="server-message" class="hide">
	<jsp:include page="messageheader.jsp"></jsp:include>
</div>
<div class="container bd-hr-container">
   <div class="bd-hr-left-panel col-lg-3 col-md-3 col-sm-3">
        <div id ="prof-hierarchy-container" class="hide">
        	<!-- hierarchy structure comes here  -->
        </div>
   </div>
        <div class="bd-hr-rt-panel col-lg-9 col-md-9 col-sm-9 col-xs-12">
            <div class="bd-hr-rt-header"><i><spring:message code="label.woulduliketoadd.key"/> <strong><spring:message code="label.new.key"/></strong></i></div>
            <div class="bd-hr-tabs-wrapper">
                <div class="bd-hr-tabs-header clearfix">
                	<c:if test="${isRegionAdditionAllowed}">
	                    <div class="bd-hr-tabs-header-item float-left">
	                        <span data-tab="region" class="bd-hdr-span bd-hdr-active-arr bd-hdr-active"><spring:message code="label.region.key"/></span>
	                    </div>
                   </c:if>
                   <c:if test="${isBranchAdditionAllowed}">
	                    <div class="bd-hr-tabs-header-item float-left">
	                        <span data-tab="office" class="bd-hdr-span"><spring:message code="label.office.key"/></span>
	                    </div>
                    </c:if>
	                    <div class="bd-hr-tabs-header-item float-left">
	                        <span data-tab="individual" class="bd-hdr-span"><spring:message code="label.individual.key"/></span>
	                    </div>
	                    <%-- <div class="bd-hr-tabs-header-item hdr-txt-rt-adj float-left">
	                        <span data-tab="csv" class="bd-hdr-span"><spring:message code="label.uploadcsv.key"/></span>
	                    </div> --%>
                </div>
            </div>
            
            <div id="bd-edit-form-section" class="bd-hr-form-wrapper">
            </div>
            
        </div>
    </div>
<!-- div to temporarily store message to be displayed  -->
<div id="temp-message" class="hide"></div>
<input class="ignore-clear" type="hidden" name="isUserAuthorized" id="is-user-authorized" value="${isUserAuthorized}"/>
<input class="ignore-clear" type="hidden" id="profile-name" value="${profileName}"/>
<script src="${pageContext.request.contextPath}/resources/js/editprofile.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/hierarchy-management.js"></script>
<script>
$(document).ready(function() {
	$(document).attr("title", "Build Hierarchy");
	checkUserAuthorization();
	fetchCompanyHierarchy();
	/**
	*	Region form is displayed by default
	*/
    getEditSectionForm('region');
	
    $(document).on('click', 'body', function() {
    	console.log("body clicked");
        $('.dd-com-list').slideUp(200);
    });
    
    $(document).on('click','.bd-hdr-span',function(){
        $('.bd-hdr-span').removeClass('bd-hdr-active');
        $('.bd-hdr-span').removeClass('bd-hdr-active-arr');
        $(this).addClass('bd-hdr-active');
        $(this).addClass('bd-hdr-active-arr');
        hideError();
        getEditSectionForm($(this).data('tab'));
    });

});
</script>