<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty profile && not empty profile.profileImageUrl}">
	<c:set value="${profile.profileImageUrl}" var="profileimage"></c:set>
</c:if>
<c:choose>
	<c:when test="${not empty profileimage}">
		<div id="prof-image" class="prof-image prof-image-edit pos-relative cursor-pointer" style="background: url(${profileimage}) no-repeat center;">
			<div class="prof-download-contact clearfix cursor-pointer">
				<div id="prof-image-upload" class="clearfix prof-dowload-width">
					<div class="float-left inc-dl"></div>
					<div class="float-left txt-download">
						<spring:message code="label.uploadimage.key"></spring:message>
					</div>
				</div>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div id="prof-image" class="prof-image prof-image-edit pos-relative cursor-pointer"	style="background-image:initial; background: no-repeat center;">
			<div class="prof-download-contact clearfix cursor-pointer">
				<div id="prof-image-upload" class="clearfix prof-dowload-width">
					<div class="float-left inc-dl"></div>
					<div class="float-left txt-download">
						<spring:message code="label.uploadimage.key"></spring:message>
					</div>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>
<form enctype="multipart/form-data">
	<input type="file" id="prof-image-edit" style="display: block;">
</form>
<div class="prof-rating-mobile-wrapper hide">
	<div class="st-rating-wrapper maring-0 clearfix">
		<div class="rating-star icn-full-star"></div>
		<div class="rating-star icn-full-star"></div>
		<div class="rating-star icn-half-star"></div>
		<div class="rating-star icn-no-star"></div>
		<div class="rating-star icn-no-star"></div>
	</div>
</div>