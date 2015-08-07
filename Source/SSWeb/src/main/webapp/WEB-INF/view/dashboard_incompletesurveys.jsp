<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:if test="${not empty incompleteSurveys}">
	<c:forEach var="survey" items="${incompleteSurveys}"  varStatus="loop">
		<div class="dash-lp-item clearfix"  data-iden="sur-pre-${survey.surveyPreIntitiationId }">
			<div class="float-left dash-lp-txt">
				${survey.customerFirstName} ${survey.customerLastName}
					<div class="font-11 opensanslight" data-value="${survey.modifiedOn}">
					</div>
			</div>
			<div
				data-custname="${survey.customerFirstName} ${survey.customerLastName}"
				data-agentid="${survey.agentId}" data-agentname="${agentName}"
				data-custemail="${survey.customerEmailId}"
				class="float-right dash-lp-rt-img cursor-pointer"
				title="Resend Survey"></div>
		</div>
	</c:forEach>
</c:if>
<script>
$(document).ready(function(){
	$('.opensanslight').each(function(index, currentElement) {
		var dateStr = $(this).attr('data-value');
		var date = convertTimeStampToLocalTimeStamp(new Date(dateStr));
		$(this).html(date);
	});
});
</script>