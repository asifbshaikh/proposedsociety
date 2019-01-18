mobile_app.controller('applicantsChangePassCtrl', function($scope,$http,$rootScope) {
    console.log("in applicants changePass");

    $scope.user={};
    $scope.signupForm= function (form) {
        if (form.$valid) {
             console.log($scope.user);

            var config = {
                url: $rootScope.appconfig.serverUrl + "/mobile/applicant/change_password",
                data: JSON.stringify($scope.user),
                method: "POST",
                headers: {
                    'Content-type': 'application/json',
                    'apiKey': window.localStorage['apiKey']
                }
            }
            $http(config).success(function (data, status, headers, config) {
                console.log("Successfully change password : " + data);
                alert(data);
                $scope.reset();
            }).error(function (data, status, headers, config) {
                console.log("Error while change password.")
                alert("Error while change password.");
            });

        } else {
            console.log("invalid form ");
        }
    }

    $scope.master = {};
    $scope.reset = function() {
        $scope.user = angular.copy($scope.master);
        window.location.reload();
    };



})