<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-2.1.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/script.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/usermanagement.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/changepassword.js"></script>
<script src="https://js.braintreegateway.com/v2/braintree.js"></script>
<!-- <script src="https://maps.googleapis.com/maps/api/js"></script> -->
<script src="${pageContext.request.contextPath}/resources/js/rangeslider.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/editprofile.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/surveyBuilder.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/proList.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/rangeslider.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/surveyQuestion.js"></script>
<script>
	$('#company-setting').click(function(e) {
		showMainContent('./showcompanysettings.do');
		e.stopImmediatePropagation();
	});
	$('#header-logo').click(function(e){
		showMainContent('./dashboard.do');
		e.stopImmediatePropagation();
	});
	$('#profile-setting').click(function(e) {
		showMainContent('./showprofilepage.do');
		e.stopImmediatePropagation();
	});
</script>
