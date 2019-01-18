mobile_app.controller('applicantsSendQueryCtrl', function($scope,$http,$rootScope) {
    console.log("in visitor sendQuery");

    $scope.user={};
    $scope.signupForm= function (form) {
        if (form.$valid) {
            console.log($scope.user);

            var config = {
                url: $rootScope.appconfig.serverUrl + "/mobile/applicant/dashboard/sendQuery",
                data: JSON.stringify($scope.user),
                method: "POST",
                headers: {
                    'Content-type': 'application/json',
                    'apiKey': window.localStorage['apiKey']
                }
            }
            $http(config).success(function (data, status, headers, config) {
                console.log("Successfully send query : " + data);
                alert(data);
                $scope.reset();
            }).error(function (data, status, headers, config) {
                console.log("Error while sending query.");
                alert("Error while sending query.");
            });

        } else {
            console.log("invalid form ");
        }
    }

    $scope.master = {};
    $scope.reset = function() {
        window.location.reload();
        $scope.user = angular.copy($scope.master);
    };



})