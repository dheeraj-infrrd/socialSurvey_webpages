<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><spring:message code="label.title.settings.key" /></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style-common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style-resp.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
</head>
<body>
	<div id="overlay-toast" class="overlay-toast"></div>
    <div class="overlay-loader hide"></div>
	<div class="login-main-wrapper padding-001 company-wrapper-min-height">
		<div class="container login-container margin-top-25 margin-bottom-25">
			<div class="row login-row">
				<form id="company-info-form" method="POST" action="./addcompanyinformation.do" enctype="multipart/form-data">
					<div id="company-info-div" class="login-wrapper-resp padding-001 margin-top-25 margin-bottom-25 login-wrapper bg-fff margin-0-auto col-xs-12">
						<div class="logo login-logo margin-bottom-25 margin-top-25"></div>
						<div class="login-txt text-center font-24 margin-bot-20">
							<spring:message code="label.companysettings.header.key"/>
						</div>
                        <div id="serverSideerror" class="validation-msg-wrapper" >
                            <!--Use this container to input all the messages from server-->
                            <jsp:include page="messageheader.jsp"/>
                        </div>
                        <div id="jsError" class="validation-msg-wrapper hide">
                            <!--Use this container to input all the messages from JS-->
                            <div class="error-wrapper clearfix">
                                <div class="float-left msg-err-icn jsErrIcn"></div>
                                <div class="float-left msg-err-txt-area">
                                    <div class="err-msg-area">
                                        <div class="err-msg-con">
                                            <p id="jsErrTxt"></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
						<div class="login-input-wrapper margin-0-auto clearfix">
							<div class="float-left login-wrapper-icon icn-company"></div>
							<input class="float-left login-wrapper-txt" id="com-company" data-non-empty="true" name="company" placeholder='<spring:message code="label.company.key"/>'>
						</div>
						<div id="com-page-company" class="login-reg-err margin-0-auto"></div>
						<div class="login-input-wrapper margin-0-auto clearfix pos-relative input-file-company">
                            <div class="clearfix file-decoy">
                                <div class="float-left login-wrapper-icon icn-lname input-file-icn-left" id="input-file-icn-left"></div>
                                <input type="text" class="float-left login-wrapper-txt txt-company-logo" id="com-logo-decoy" name="" placeholder='Logo'>
                                <div class="float-right input-icon-internal icn-file file-pick-logo" id="icn-file"></div>
                            </div>
                            <div class="clearfix file-main">
                                <div class="float-left login-wrapper-icon icn-lname" id=""></div>
                                <input type="file" class="float-left login-wrapper-txt txt-company-logo" id="com-logo" name="logo" placeholder='Logo'>
                                <div class="float-right input-icon-internal icn-file" id="icn-file"></div>
                            </div>
						</div>
						<div class="login-input-wrapper margin-0-auto clearfix">
							<div class="float-left login-wrapper-icon icn-address"></div>
							<input class="float-left login-wrapper-txt" id="com-address1" data-non-empty="true" name="address1" placeholder='<spring:message code="label.address1.key"/>'>
						</div>
						<div id="com-page-address1" class="login-reg-err margin-0-auto"></div>
                        <div class="login-input-wrapper margin-0-auto clearfix">
							<div class="float-left login-wrapper-icon icn-address"></div>
							<input class="float-left login-wrapper-txt" id="com-address2" name="address2" placeholder='<spring:message code="label.address2.key"/>'>
						</div>
						<div class="login-input-wrapper margin-0-auto clearfix">
							<div class="float-left login-wrapper-icon icn-address"></div>
							<input class="float-left login-wrapper-txt" id="com-country" data-non-empty="true" name="country" placeholder='<spring:message code="label.country.key"/>'>
						</div>
						<div id="com-page-country" class="login-reg-err margin-0-auto"></div>
                        <div class="login-input-wrapper margin-0-auto clearfix">
							<div class="float-left login-wrapper-icon icn-zip"></div>
							<input class="float-left login-wrapper-txt" id="com-zipcode" data-non-empty="true" data-zipcode = "true" name="zipcode" placeholder='<spring:message code="label.zipcode.key"/>'>
						</div>
						<div id="com-page-zipcode" class="login-reg-err margin-0-auto"></div>
                        <div class="login-input-wrapper margin-0-auto clearfix">
							<div class="float-left login-wrapper-icon icn-contact"></div>
							<input class="float-left login-wrapper-txt" id="com-contactno" data-non-empty="true" data-phone = "true" name="contactno" placeholder='<spring:message code="label.companycontactno.key"/>'>
						</div>
						<div id="com-page-contactno" class="login-reg-err margin-0-auto"></div>
						<c:if test="${not empty verticals }">
							<div class="login-input-wrapper margin-0-auto clearfix">
								<select name="vertical" id="select-vertical" class="login-input-wrapper">
									<option disabled selected><spring:message code="label.vertical.key"/></option>
									<c:forEach items="${verticals }" var="vertical">
										<option id="vertical-${vertical.verticalsMasterId }">${vertical.verticalName}</option>
									</c:forEach>
								</select>
							</div>
						</c:if>
						<div class="btn-submit margin-0-auto cursor-pointer font-18 text-center" id="company-info-submit">
                            <spring:message code="label.done.key" />
						</div>
					</div>
					<input type="hidden" value="${emailid}" name="originalemailid" id="originalemailid">
					<input type="hidden" name="countrycode" id="country-code">
				</form>
				<div class="login-footer-wrapper login-footer-txt clearfix margin-0-auto margin-bottom-50 col-xs-12">
					<div class="float-right">
						<spring:message code="label.alreadyhaveanacoount.key" />?
						<span class="cursor-pointer">
							<a class="login-link" href="./login.do">
								<strong><spring:message code="label.login.key" /></strong>
							</a>
						</span>
					</div>
				</div>
				<div class="footer-copyright text-center">
					<spring:message code="label.copyright.key"/> 
					&copy; 
					<spring:message code="label.footer.socialsurvey.key"/> 
					<span class="center-dot">.</span> 
					<spring:message code="label.allrightscopyright.key"/>
				</div>
                
			</div>
		</div>
	</div>

	<script src="${pageContext.request.contextPath}/resources/js/jquery-2.1.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/common.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/script.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/countrydata.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/zipcoderegex.js"></script>
	<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
  	<script src="http://code.jquery.com/ui/1.11.2/jquery-ui.js"></script>

	<script>
		var isCompanyInfoPageValid;
		var selectedCountryRegEx = "";
		$(document).ready(function() {
		$(document).attr("title", "Company Information");
			isCompanyInfoPageValid=false;
//			adjustOnResize();

//			$(window).resize(adjustOnResize);

			function adjustOnResize() {
				var winH = $(window).height();
				var winH2 = winH / 2;
				var conH2 = $('.login-row').height() / 2;
				var offset = winH2 - conH2;
				if (offset > 25) {
					$('.login-row').css('margin-top', offset + 'px');
				}
			}
            
            $('#icn-file').click(function(){
                $('#com-logo').trigger('click');
            });
            
            $('#company-info-submit').click(function() {
            	submitCompanyInfoForm();
            });
            
            $('#com-logo').change(function(){
                var fileAdd = $(this).val().split('\\');
                $('#com-logo-decoy').val(fileAdd[fileAdd.length - 1]);
            });
			
            //Integrating autocomplete with country input text field
            $( "#com-country" ).autocomplete({
                minLength: 1,
                source: countryData,
                delay : 0,
                open : function( event, ui ) {
                    $( "#country-code" ).val("");
                },
                focus: function( event, ui ) {
                  $( "#com-country" ).val( ui.item.label );
                  return false;
                },
                select: function( event, ui ) {
                  $( "#com-country" ).val( ui.item.label );
                  $( "#country-code" ).val( ui.item.code );
                  for(var i=0;i<postCodeRegex.length;i++){
                	  if(postCodeRegex[i].code == ui.item.code){
                		  selectedCountryRegEx = "^"+postCodeRegex[i].regex+"$";
                		  selectedCountryRegEx = new RegExp(selectedCountryRegEx);
                		  break;
                	  }
                  }
                  return false;
                },
                close: function( event, ui ) {
                }
              })
              .autocomplete( "instance" )._renderItem = function( ul, item ) {
                return $( "<li>" )
                  .append(item.label)
                  .appendTo( ul );
              };
            
		});
		$('#com-company').blur(function() {
			validateCompany(this.id);
		});
        
		$('#com-address1').blur(function() {
			validateAddress1(this.id);
		});
		
		$('#com-address2').blur(function() {
			validateAddress2(this.id);
		});
		
		$('#com-zipcode').blur(function() {
			validateCountryZipcode(this.id);
		});
		
		$('#com-contactno').blur(function() {
			validatePhoneNumber(this.id);
		});
		
		function validateCompanyInformationForm(elementId) {
			//hide the server error
        	$("#serverSideerror").hide();
			isCompanyInfoPageValid = true;
			var isFocussed = false;
        	var isSmallScreen = false;
        	if($(window).width()<768){
        		isSmallScreen = true;
        	}
        	
			if(!validateCompany('com-company')){
				isCompanyInfoPageValid = false;
				if(!isFocussed){
        			$('#com-company').focus();
        			isFocussed=true;
        		}
        		if(isSmallScreen){
        			return isCompanyInfoPageValid;
        		}
			}
			if(!validateAddress1('com-address1')){
				isCompanyInfoPageValid = false;
				if(!isFocussed){
        			$('#com-address1').focus();
        			isFocussed=true;
        		}
        		if(isSmallScreen){
        			return isCompanyInfoPageValid;
        		}
			}
			if(!validateAddress2('com-address2')){
				isCompanyInfoPageValid = false;
				if(!isFocussed){
        			$('#com-address2').focus();
        			isFocussed=true;
        		}
        		if(isSmallScreen){
        			return isCompanyInfoPageValid;
        		}
			}
			
			if(!validateCountry('com-country')){
				isCompanyInfoPageValid = false;
				if(!isFocussed){
        			$('#com-country').focus();
        			isFocussed=true;
        		}
        		if(isSmallScreen){
        			return isCompanyInfoPageValid;
        		}
			}
			
			if(!validateCountryZipcode('com-zipcode')){
				isCompanyInfoPageValid = false;
				if(!isFocussed){
        			$('#com-zipcode').focus();
        			isFocussed=true;
        		}
        		if(isSmallScreen){
        			return isCompanyInfoPageValid;
        		}
			}
			if(!validatePhoneNumber('com-contactno')){
				isCompanyInfoPageValid = false;
				if(!isFocussed){
        			$('#com-contactno').focus();
        			isFocussed=true;
        		}
        		if(isSmallScreen){
        			return isCompanyInfoPageValid;
        		}
			}
			return isCompanyInfoPageValid;
		}
		
		function submitCompanyInfoForm() {
			console.log("submitting company information form");
			if(validateCompanyInformationForm('company-info-div')){
				$('#company-info-form').submit();
			}
		}
		function uploadImageSuccessCallback(response) {
			$("#serverSideerror").html(response);
			var success = "Logo has been uploaded successfully";
			var successMsg = $("#serverSideerror").find('.display-message').text().trim();
			if (success != successMsg) {
				$('#com-logo').val('');
				$('#com-logo-decoy').val('');
			}
		}
		$('input').keypress(function(e){
        	// detect enter
        	if (e.which==13){
        		e.preventDefault();
        		submitCompanyInfoForm();
        	}
		});
		$("#com-logo").on("change", function() {
			var formData = new FormData();
			formData.append("logo", $('#com-logo').prop("files")[0]);
			formData.append("logo_name", $('#com-logo').prop("files")[0].name);
			callAjaxPOSTWithTextData("./uploadcompanylogo.do", uploadImageSuccessCallback, true, formData);
		});
		
		function validateCountry(){
			var country = $.trim($('#com-country').val());
			if(country == ""){
				$('#com-page-country').html("Please enter country name");
				$('#com-page-country').show();
				return false;
			}else{
				var countryCode = $.trim($('#country-code').val());
				if(countryCode == ""){
					$('#com-page-country').html("Please enter valid country name");
					$('#com-page-country').show();
					return false;
				}else{
					$('#com-page-country').html("");
					$('#com-page-country').hide();
					return true;
				}
			}
		}
		
		//Function to validate the zipcode
		function validateCountryZipcode(elementId){
			var zipcode = $('#'+elementId).val();
			if($(window).width()<768){
				if (zipcode != "") {
					if (selectedCountryRegEx.test(zipcode) == true) {
						return true;
					}else {
						$('#overlay-toast').html('Please enter a valid zipcode.');
						showToast();
						return false;
					}
				}else{
					$('#overlay-toast').html('Please enter zipcode.');
					showToast();
					return false;
				}
			}else{
		    	if (zipcode != "") {
					if (selectedCountryRegEx.test(zipcode) == true) {
						$('#'+elementId).parent().next('.login-reg-err').hide();
						return true;
					}else {
						$('#'+elementId).parent().next('.login-reg-err').html('Please enter a valid zipcode.');
						$('#'+elementId).parent().next('.login-reg-err').show();
						return false;
					}
				}else{
					$('#'+elementId).parent().next('.login-reg-err').html('Please enter zipcode.');
					$('#'+elementId).parent().next('.login-reg-err').show();
					return false;
				}
			}
		}
		
	</script>
</body>
</html>