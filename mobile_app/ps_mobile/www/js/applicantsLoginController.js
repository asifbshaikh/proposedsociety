mobile_app.controller('applicantsLoginCtrl', function($scope,$state,$http,$rootScope) {
    $scope.user={};
    $scope.user.password="";
    $scope.user.email="";
    $scope.fpassFlag=false;
    $scope.msgFlag = true;
    $scope.pageTitle="Applicants";

    console.log("in ApplicantsLogin controller");

    $scope.enterSite=function(){
        console.log($scope.user);
        var config = {
            url: $rootScope.appconfig.serverUrl + "/mobile/visitor/authenticate",
            data: JSON.stringify($scope.user),
            method: "POST",
            headers: {
                'Content-type': 'application/json'
            }
        }
        $http(config).success(function(responce,status,header,config){
            console.log("Response: " + responce.authcode);
            window.localStorage['apiKey'] = responce.authcode;
            $state.go("appApplicants.applicantsHome");

        }).error(function(data){
            console.log("Error");
            $scope.msgFlag = false;

        })
    }
    $scope.forgetCodeBlock=function(){
        $scope.fpassFlag=true;
        $scope.pageTitle="Recovery";
    }
    $scope.forgetpassword=function(){
        var config = {
            url: $rootScope.appconfig.serverUrl + "/mobile/member/forget/password",
            data: JSON.stringify($scope.user),
            method: "POST",
            headers: {
                'Content-type': 'application/json'
            }
        }
        $http(config).success(function(responce,status,header,config){
            alert("check your email inbox to get password.");
            $scope.user={};
            $scope.backToLogin();
            $state.go("tab.applicants");
        }).error(function(data){
            console.log("Error");
            $scope.msgFlag = false;
        })
    }
    $scope.backToLogin=function(){
        //$scope.msgFlag = true;
        //window.location.reload();
        $scope.fpassFlag = false;
        $scope.pageTitle="Applicants";
    }


})