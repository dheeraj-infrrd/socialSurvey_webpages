<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="${cannonicalusersettings.companySettings.contact_details}" var="contactdetail"></c:set>
<c:set value="${cannonicalusersettings.companySettings.vertical}" var="companyvertical"></c:set>

<input id="prof-name" class="prof-name prof-edditable" value="${contactdetail.name}">
<div class="prof-address">
	<input id="prof-vertical" class="prof-addline1 prof-edditable" value="${companyvertical}">
	<input id="prof-title" class="prof-addline2 prof-edditable" value="${contactdetail.title}">
</div>
<div class="prof-rating clearfix">
	<div class="st-rating-wrapper maring-0 clearfix float-left">
		<div class="rating-star icn-full-star"></div>
		<div class="rating-star icn-full-star"></div>
		<div class="rating-star icn-half-star"></div>
		<div class="rating-star icn-no-star"></div>
		<div class="rating-star icn-no-star"></div>
	</div>
</div>