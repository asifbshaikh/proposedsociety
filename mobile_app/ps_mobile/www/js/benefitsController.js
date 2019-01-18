mobile_app.controller('VisitorBenefitsCtrl', function($scope,$http,$rootScope,$sce) {
    console.log("in visitor benefits");
    title = {title: "benefits"};
    $scope.info="";
    console.log(title)
    console.log(JSON.stringify(title))
    var config = {
        url: $rootScope.appconfig.serverUrl + '/info',
        method: 'POST',
        data: JSON.stringify(title),
        header: {
            'content-type': 'application/json'
        }
    }
    $http(config).success(function (data, status, headers, config) {
        $scope.info=$sce.trustAsHtml(data);
    }).error(function (data, status, headers, config) {
        console.log("Error while fetching concept: " + data)
        $scope.info="Error while fetching data";
    });
})