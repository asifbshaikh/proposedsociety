mobile_app.controller('applicantsHomeCtrl', function($scope,$ionicSideMenuDelegate,$state) {
    console.log("in applicants home");
    $scope.logoutBtn = function () {

        window.localStorage.clear();
        $state.go("tab.login");
    }

})

