<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript" src="//apis.google.com/js/plusone.js"></script>
<script type="text/javascript" async src="//platform.twitter.com/widgets.js"></script>
<script type="text/javascript">
  (function() {
   var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
   po.src = 'https://apis.google.com/js/client:plusone.js';
   var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
 })();
</script>

<script type="text/javascript">

    function twitterFn (loop,twitterElement) {
    	console.log("in twitterFn");
    	var twitText = "";
      	var twitLink = $("#twitt_"+loop).data('link');
      	var String=twitLink.substring(twitLink.indexOf("=")+1,twitLink.lastIndexOf("&"));
    	var twitId = 'twttxt_'+loop;
    	if($("#"+twitId) != undefined)
    		{
    		console.log("twittId not null");
    		 twitText = $("#"+twitId).val();
    		}
    	
    	var length = twitText.length;
    	if(length > 109)
    		{
    		var arr = twitLink.split('');
       		var twittStrnDot = "...";
    		var substringed = twitText.substring(0, 105);
    		var finalString = substringed.concat(twittStrnDot);
    		if($("#"+twitId) != undefined)
    			{
    			$("#"+twitId).val(finalString);
    			}
    		
    		twitLink = twitLink.replace(String,finalString);
    		if(document.getElementById('twitt_'+loop) != undefined)
    		document.getElementById('twitt_'+loop).setAttribute('data-link',twitLink);
   		}
    	 
    	
    }
    function getImageandCaption(loop)
    {
    	console.log("inside imagCaption- dashboard");
    	var name = "";
    	var designation = "";
    	var company = "";
    	var pictureandCaptionLink = "";
    	var fblink="";
    	if($("#fb_"+loop) != undefined)
    		{
    		
    		fblink = $("#fb_"+loop).data('link');	
    		console.log("fb id is not undefined");
    		}
   
    var imgId= "";
    if($("#dsh-prsn-img").css("background-image") != undefined && $("#dsh-prsn-img") != undefined)
    	{
  	 imgId = $("#dsh-prsn-img").css("background-image");
     var imgIdFirstIndex = imgId.indexOf("(");
     var imgIdlastIndex = imgId.lastIndexOf(")");
     imgId = imgId.substring(imgIdFirstIndex+1, imgIdlastIndex);
     console.log("imgId id is not undefined");
    	}
    
	    if(document.getElementById("name") != null)
		{
		  name = document.getElementById("name").innerHTML;
		  console.log("name id is not undefined");
		}

		if(document.getElementById("designation") != null)
		{
			 console.log("designation id is not undefined");
		designation = document.getElementById("designation").innerHTML;
		}
		
		if(document.getElementById("company") != null)
		{
			console.log("company id is not undefined");
			company = document.getElementById("company").innerHTML;
		
		}
 
    pictureandCaptionLink = "&picture="+imgId+"&caption="+name+","+designation+","+company;
    fblink = fblink.concat(pictureandCaptionLink);
    if(document.getElementById('fb_'+loop) != null)
    document.getElementById('fb_'+loop).setAttribute('data-link',fblink);
    console.log("exit dashboard get img");
    	}
</script>
<c:choose>
	<c:when test="${not empty reviews}">
		<c:forEach var="feedback" varStatus="loop" items="${reviews}">
			<c:set value="ppl-review-item" var="reviewitemclass"></c:set>
			<div data-firstname="${feedback.customerFirstName}" data-lastname="${feedback.customerLastName}"
				data-agentid="${feedback.agentId}" data-agentname="${feedback.agentName}" data-customeremail="${feedback.customerEmail}"
				data-review="${feedback.review}" data-score="${feedback.score}" class="${reviewitemclass}">
				
				<div class="ppl-header-wrapper clearfix">
					<div class="float-left ppl-header-left">
						<div class="ppl-head-1">
							${feedback.customerFirstName} ${feedback.customerLastName}
						</div>
						<div class="ppl-head-2" data-modifiedon="<fmt:formatDate type="date" pattern="yyyy-MM-dd-H-mm-ss"
							value="${feedback.modifiedOn}" />">
						</div>
					</div>
					<div class="float-right ppl-header-right">
						<div class="st-rating-wrapper maring-0 clearfix review-ratings float-right" data-rating="${feedback.score}" data-source="${feedback.source }">
						</div>
						<c:if test="${feedback.source != 'Zillow'}">
							<div class="report-resend-icn-container clearfix float-right">
								<div class="report-abuse-txt report-txt">Report</div>
								|
								<div class="restart-survey-mail-txt report-txt">Retake</div>
							</div>
						</c:if>
					</div>
				</div>
				<div class="ppl-content">${feedback.review}</div>
				<div class="ppl-share-wrapper clearfix">
					<div class="float-left blue-text ppl-share-shr-txt"><spring:message code="label.share.key" /></div>
					<div class="float-left icn-share icn-plus-open" style="display: block;"></div>
					<div class="float-left clearfix ppl-share-social hide" style="display: none;">
					
						<span id ="fb_${loop.index}"class="float-left ppl-share-icns icn-fb" title="Facebook" onclick ="getImageandCaption(${loop.index})" data-link="https://www.facebook.com/dialog/feed?${feedback.faceBookShareUrl}&link=${feedback.completeProfileUrl}&description=${feedback.score}-star response from ${feedback.customerFirstName} ${feedback.customerLastName} for ${feedback.agentName} at SocialSurvey - ${feedback.review} .&redirect_uri=https://www.facebook.com"></span>
						
						<input type="hidden" id="twttxt_${loop.index}" class ="twitterText_loop" value ="${feedback.score}-star response from ${feedback.customerFirstName} ${reviewItem.customerLastName} for ${feedback.agentName} at SocialSurvey - ${feedback.review}"/>
						<span class="float-left ppl-share-icns icn-twit" id ="twitt_${loop.index}" onclick="twitterFn(${loop.index},this);" data-link="https://twitter.com/intent/tweet?text=${feedback.score}-star response from ${feedback.customerFirstName} ${feedback.customerLastName} for ${feedback.agentName} at SocialSurvey - ${feedback.review}&url=${feedback.completeProfileUrl}"></span>
						 <span
							class="float-left ppl-share-icns icn-lin" title="LinkedIn"
							data-link="https://www.linkedin.com/shareArticle?mini=true&url=${feedback.completeProfileUrl} &title=&summary=${feedback.score}-star response from ${feedback.customerFirstName} ${feedback.customerLastName} for ${feedback.agentName} at SocialSurvey - ${feedback.review} + &source="></span>
                        <span class="float-left ppl-share-icns icn-gplus" title="Google+">
                        <button 
                            class="g-interactivepost float-left ppl-share-icns icn-gplus"
                            data-contenturl="${feedback.completeProfileUrl}"
                            data-clientid="${feedback.googleApi}"
                            data-cookiepolicy="single_host_origin"
                            data-prefilltext="${feedback.score}-star response from ${feedback.customerFirstName} ${feedback.customerLastName} for ${feedback.agentName} at SocialSurvey - ${feedback.review}"
                            data-calltoactionlabel="USE"
                            data-calltoactionurl="${feedback.completeProfileUrl}"
                         >
                           <span class="icon">&nbsp;</span>
                           <span class="label">share</span>
                       </button>
                        </span>
						<c:if test="${not empty feedback.yelpProfileUrl}">
							<span class="float-left ppl-share-icns icn-yelp" title="Yelp" data-link="${feedback.yelpProfileUrl}"></span>
						</c:if>
						<c:if test="${not empty feedback.zillowProfileUrl}">
							<span class="float-left ppl-share-icns icn-zillow" title="Zillow" data-link="${feedback.zillowProfileUrl}"></span>
						</c:if>
						<c:if test="${not empty feedback.lendingTreeProfileUrl}">
							<span class="float-left ppl-share-icns icn-lendingtree" title="LendingTree" data-link="${feedback.lendingTreeProfileUrl}"></span>
						</c:if>
						<c:if test="${not empty feedback.realtorProfileUrl}">
							<span class="float-left ppl-share-icns icn-realtor" title="Realtor" data-link="${feedback.realtorProfileUrl}"></span>
						</c:if>
					</div>
					<div class="float-left icn-share icn-remove icn-rem-size hide" style="display: none;"></div>
				</div>
			</div>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<div class="dash-lp-header" id="incomplete-survey-header"><spring:message code="label.noincompletesurveys.key" /></div>
	</c:otherwise>
</c:choose>

</html>
<script>
$(document).ready(function(){
	$('.ppl-head-2').each(function(index, currentElement) {
		var dateSplit = $(this).attr('data-modifiedon').split('-');
		var date = convertUserDateToLocale(new Date(dateSplit[0], dateSplit[1]-1, dateSplit[2], dateSplit[3], dateSplit[4], dateSplit[5]));
		$(this).html(date.toDateString());
	});
	
	$('.icn-yelp').each(function(index, currentElement) {
		var url = $(this).parent().attr('href');
		$(this).parent().attr('href', returnValidWebAddress(url));
	});
	$('.icn-zillow').each(function(index, currentElement) {
		var url = $(this).parent().attr('href');
		$(this).parent().attr('href', returnValidWebAddress(url));
	});
	$('.icn-lendingtree').each(function(index, currentElement) {
		var url = $(this).parent().attr('href');
		$(this).parent().attr('href', returnValidWebAddress(url));
	});
	
	$('.ppl-share-icns').bind('click', function() {
		var link = $(this).attr('data-link');
		var title = $(this).attr('title');
		if (link == undefined || link == "") {
			return false;
		}
		window.open(link, 'Post to ' + title, 'width=800,height=600,scrollbars=yes');
	});
});
</script>