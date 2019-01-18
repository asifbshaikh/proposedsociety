mobile_app.controller('VisitorHomeCtrl', function($scope,$ionicSideMenuDelegate,$state) {
    console.log("in visitor home");

    $scope.logoutBtn = function () {

        window.localStorage.clear();
        $state.go("tab.login");

    }

})

