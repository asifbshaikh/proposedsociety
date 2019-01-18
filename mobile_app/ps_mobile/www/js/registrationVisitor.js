mobile_app.controller('registrationVisitorCtrl', function($scope,$http,$rootScope,$state,$window) {

    var apiKey = window.localStorage['apiKey'];
    if(apiKey !=null){
        $state.go("app.visitorHome");
    }else {

        $scope.user = {};
        $scope.user.name = "";
        $scope.user.mobile = "";
        $scope.user.email = "";
        $scope.msgFlag = true;
        $scope.getCode = function () {
            var config = {
                url: $rootScope.appconfig.serverUrl + "/mobile/visitor/authcode/generate",
                data: JSON.stringify($scope.user),
                method: "POST",
                headers: {
                    'Content-type': 'application/json'
                }
            }
            $http(config).success(function (data, status, headers, config) {
                console.log("Success");
                $state.go("tab.authentication");
            }).error(function (data, status, headers, config) {
                $scope.msgFlag = false;
            });
        }
    }
})