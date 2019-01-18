mobile_app.controller('applicantsReferFriendCtrl', function($scope,$http,$rootScope) {
    console.log("in visitor Refer Friend");

    $scope.user={};
    $scope.signupForm= function (form) {
        if (form.$valid) {
             console.log($scope.user);

            var config = {
                url: $rootScope.appconfig.serverUrl + "/mobile/applicant/dashboard/referFriend",
                data: JSON.stringify($scope.user),
                method: "POST",
                headers: {
                    'Content-type': 'application/json',
                    'apiKey': window.localStorage['apiKey']
                }
            }
            $http(config).success(function (data, status, headers, config) {
                console.log("Successfully : " + data);
                alert(data);
                $scope.reset();
            }).error(function (data, status, headers, config) {
                console.log("Error while change password.")
                alert("Error while refer friend.");
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