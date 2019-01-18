mobile_app.controller('authentication', function($scope,$state,$http,$rootScope) {
    $scope.user={};
    $scope.user.password="";
    $scope.user.email="";
    $scope.fpassFlag=false;
    $scope.msgFlag = true;
    $scope.pageTitle="Existing Visitor";

    console.log("in Authentication controller");
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
            console.log("Response: " + responce.authcode)
			window.localStorage['apiKey'] = responce.authcode;
            $state.go("app.visitorHome");

        }).error(function(data){
            console.log("Error ");
            $scope.msgFlag = false;

        })
    }
    $scope.forgetpassword=function(){
        var config = {
            url: $rootScope.appconfig.serverUrl + "/mobile/visitor/forget/authcode",
            data: JSON.stringify($scope.user),
            method: "POST",
            headers: {
                'Content-type': 'application/json'
            }
        }
        $http(config).success(function(responce,status,header,config){
            alert("check your email inbox to get code.");
            $scope.user={};
            $scope.backToLogin();
            $state.go("tab.authentication");
        }).error(function(data){
            console.log("Error");
            $scope.msgFlag = false;
        })
    }
    $scope.forgetCodeBlock=function(){
        $scope.fpassFlag=true;
        $scope.pageTitle="Recovery";
    }
    $scope.test=function(){
        $scope.msgFlag = true;
    }
    $scope.backToLogin=function(){
        //$scope.msgFlag = true;
        //window.location.reload();
        $scope.fpassFlag = false;
        $scope.pageTitle="Existing Visitor";
    }
})