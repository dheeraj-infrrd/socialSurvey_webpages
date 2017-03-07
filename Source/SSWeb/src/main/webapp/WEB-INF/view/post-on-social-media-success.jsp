<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Thank You</title>
    <link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/style.css">
    <link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/style-common-1.1.css">
    <link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/style-common.css">
    <link rel="stylesheet" href="${initParam.resourcesPath}/resources/css/style-resp.css">
</head>
<body>
	<div class="err-wrapper-main">
        <div class="header-main-wrapper err-header-wrapper">
            <div class="container clearfix">
                <div class="float-left hdr-logo-blue"></div>
                <div class="header-user-info float-right clearfix">
                    <c:if test="${displaylogo != null}">
                        <div class="float-left user-info-seperator"></div>
                        <div class="float-left user-error-logo" style="background: url(${displaylogo}) no-repeat center; background-size: 100% auto;"></div>
                    </c:if>
                </div>			
            </div>
        </div>
        
        <div class="hm-header-main-wrapper">
            <div class="container">
                <div class="hm-header-row hm-header-row-main clearfix">
                    <div class="text-center err-header-row">Successfully Posted</div>
                </div>
            </div>
        </div>

        <div class="error-main-wrapper container">
            <div class="err-line-1 text-center">Thank You</div>  
            <div class="err-line-2 text-center">Your review has been successfully posted on ${socialMedia}.</div>
        </div>

        <div class="footer-main-wrapper">
            <div class="container text-center footer-text">
                Copyright &copy; Social Survey <span class="center-dot">.</span> All Rights Reserved.
            </div>
        </div>
    </div>
    <jsp:include page="scripts.jsp"/>
    
    <script>
        $(document).ready(function(){
            adjustMinHeight();
            $(window).resize(adjustMinHeight);
            function adjustMinHeight(){
                var winH = $(window).height();
                var minH = "";
                if($(window).width() < 768){
                    minH = winH - 50 - 50 - 5 - 1;
                }else{
                    minH = winH - 83 - 78 - 78 -1;
                }
                $('.error-main-wrapper').css('min-height',minH+'px');
            }
        });
    </script>
</body>
</html>