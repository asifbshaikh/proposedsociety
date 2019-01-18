mobile_app.controller('VisitorConceptCtrl', function($scope,$http,$rootScope) {
    console.log("in visitor concept");
    title = {title: "concept"};
    $scope.info="";
    console.log(title)
    console.log(JSON.stringify(title))
    var config = {
        url: $rootScope.appconfig.serverUrl + '/info',
        method: 'POST',
        data: JSON.stringify(title),
        header: {
            'content-type': 'application/json',
            'apiKey': window.localStorage['apiKey'] 
        }
    }
    $http(config).success(function (data, status, headers, config) {
        $scope.info=data;   
    }).error(function (data, status, headers, config) {
        console.log("Error while fetching concept: " + data)
        $scope.info="Error while fetching data";
    });

})