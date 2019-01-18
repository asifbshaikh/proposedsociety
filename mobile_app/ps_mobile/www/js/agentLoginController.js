mobile_app.controller('agentLoginCtrl', function($scope,$state,$http,$rootScope) {
    $scope.user={};
    $scope.user.password="";
    $scope.user.email="";
    $scope.fpassFlag=false;
    $scope.msgFlag = true;
    $scope.pageTitle="Agent";

    console.log("in ApplicantsLogin controller");

    $scope.forgetCodeBlock=function(){
        $scope.fpassFlag=true;
        $scope.pageTitle="Recovery";
    }
    $scope.forgetpassword=function(){
        var config = {
            url: $rootScope.appconfig.serverUrl + "/mobile/agent/forget/password",
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
            $state.go("tab.agent");
        }).error(function(data){
            console.log("Error");
            $scope.msgFlag = false;
        })
    }
    $scope.backToLogin=function(){
        //$scope.msgFlag = true;
        //window.location.reload();
        $scope.fpassFlag = false;
        $scope.pageTitle="Agent";
    }


})