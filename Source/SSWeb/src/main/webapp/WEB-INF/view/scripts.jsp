<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-2.1.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/common.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1','packages':['corechart']}]}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/script.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/usermanagement.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/changepassword.js"></script>
<script src="https://js.braintreegateway.com/v2/braintree.js"></script>
<!-- <script src="https://maps.googleapis.com/maps/api/js"></script> -->
<script src="${pageContext.request.contextPath}/resources/js/rangeslider.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/proList.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/rangeslider.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/surveyQuestion.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/progressbar.min.js"></script>
<script>
	$('#company-setting').click(function() {
		showMainContent('./showcompanysettings.do');
	});
	$('#header-logo').click(function(){
		showMainContent('./dashboard.do');
	});
	$('#profile-setting').click(function() {
		showMainContent('./showprofilepage.do');
	});
</script>
