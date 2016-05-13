app.controller('accountSignupController', ['$scope', '$http', '$location', 'vcRecaptchaService', 'LoginService', function ($scope, $http, $location, vcRecaptchaService, LoginService) {
	$scope.activate = 0;
	$scope.accountRegistration = {};
	$scope.response = null;
    $scope.widgetId = null;
    $scope.model = {key: '6Le2wQYTAAAAAAacBUn0Dia5zMMyHfMXhoOh5A7K'};
    $scope.accountRegisterIds = {};
    
    $scope.submitLogin = function () {
        if (vcRecaptchaService.getResponse() === "") { //if string is empty
            showError("Please resolve the captcha and submit!");
            $scope.activate = 0;
        } else {
        	$scope.accountRegistration.captchaResponse = vcRecaptchaService.getResponse();
        	console.log($scope.accountRegistration.phone);
        	$scope.accountRegistration.phone = {"countryCode" : "1", "number" : "1234567890", "extension" : "12"};
        }
        LoginService.signup($scope.accountRegistration)
            .then(function (response) {
            	$scope.accountRegisterIds = response.data;
            	$scope.register();
            }, function (error) {
            	console.log(error);
            	showError(error);
            });
    };

    $scope.setResponse = function (response) {
        $scope.activate = 1;
        console.info(response);
        $scope.response = response;
    };
    
    $scope.setWidgetId = function (widgetId) {
        console.info('Created widget ID: %s', widgetId);
        $scope.widgetId = widgetId;
    };
    
    $scope.cbExpiration = function () {
        console.info('Captcha expired. Resetting response object');
        vcRecaptchaService.reload($scope.widgetId);
        $scope.response = null;
    };
    
    $('#reg-phone').intlTelInput({
        utilsScript: "../resources/js/utils.js"
    });
    
    $('#reg-phone').mask(phoneFormat, {
        'translation': {
            d: {
                pattern: /[0-9*]/
            },
            /*x:{
	    	 pattern:/[A-Z*]/
	     }*/
        }
    });
    
    $scope.register = function () {
    	$location.path('/linkedin').replace();
    };
}]);


app.controller('linkedInController', ['$http', '$location', function ($http, $location) {
    var vm = this;
    vm.title = 'AngularJS for SocialSurvey';
}]);


app.controller('profileController', ['$scope', '$http', '$location', 'UserProfileService', function ($scope, $http, $location, UserProfileService) {
	$scope.userProfile = {};
	
	UserProfileService.getUserProfile(1230).then(function(response){ 
		$scope.userProfile = response.data;
	}, function (error) {
	    console.log(error);
	    showError(error);
	});
	
    var myDropzone = new Dropzone("div#my-awesome-dropzone", {
        url: "/file/post"
    });
    
    $scope.profileAuthentication = function () {
        console.log($scope.userProfile);
    };
}]);

app.controller('profiledetailController', ['$http', '$location', function ($http, $location) {
    $('#reg-phone1').intlTelInput({
        utilsScript: "../resources/js/utils.js"
    });
    $('#reg-phone1').mask(phoneFormat, {
        'translation': {
            d: {
                pattern: /[0-9*]/
            },
            /*x:{
	    	 pattern:/[A-Z*]/
	     }*/
        }
    });
    $('#reg-phone2').intlTelInput({
        utilsScript: "../resources/js/utils.js"
    });
    $('#reg-phone2').mask(phoneFormat, {
        'translation': {
            d: {
                pattern: /[0-9*]/
            },
            /*x:{
	    	 pattern:/[A-Z*]/
	     }*/
        }
    });
}]);
app.controller('companydetailController', ['$scope', '$http', '$location', function ($scope, $http, $loction) {
    $scope.countrycode=='ax';
   if( $scope.countrycode=='us'){
    $scope.State='State';
    $scope.ZIP='ZIP';
   }else if( $scope.countrycode=='ca'){
    $scope.State='Province';
    $scope.ZIP='Postal Code';
   }else{
       $scope.State='State';
    $scope.ZIP='ZIP';
   }
    $("#country").countrySelect();
    $('#reg-phone-office').intlTelInput({
        utilsScript: "../resources/js/utils.js"
    });
    $('#reg-phone-office').mask(phoneFormat, {
        'translation': {
            d: {
                pattern: /[0-9*]/
            },
            /*x:{
	    	 pattern:/[A-Z*]/
	     }*/
        }
    });
}]);