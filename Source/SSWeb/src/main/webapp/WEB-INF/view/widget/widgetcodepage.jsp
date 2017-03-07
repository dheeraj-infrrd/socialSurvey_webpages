<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div id="widget-container" class="prof-user-address prof-edit-icn">
	<textarea id="widget-code-area"></textarea>
</div>
<script>
$(document).ready(function() {
	var iden = "${ iden }";
	var profileLevel = "${ profileLevel }";
	var appBaseUrl = "${ applicationBaseUrl }";
	var body = "";
	if (iden == undefined || profileLevel == undefined || profileLevel == ""){
		body = "Incorrect parameters. Please check your selection.";
	} else {
		body = "&lt;iframe id = \"ss-widget-iframe\" src=\"" + appBaseUrl +  "rest/widget/" + profileLevel + "/" + iden + "\" frameborder=\"0\" width=\"100%\" height=\"500px\" style=\"overflow-y: scroll;\" /&gt;";
		body += "&lt;script type=\"text/javascript\"&gt;$(document).ready(function(){ var myEventMethod = window.addEventListener ? \"addEventListener\" : \"attachEvent\"; var myEventListener = window[myEventMethod]; var myEventMessage = myEventMethod == \"attachEvent\" ? \"onmessage\" : \"message\"; myEventListener(myEventMessage, function (e) { if (e.data === parseInt(e.data)) document.getElementById('ss-widget-iframe').height = e.data + \"px\";    }, false);});&lt;/script&gt;";
	}
	$("#widget-code-area").html(body);
});
</script>