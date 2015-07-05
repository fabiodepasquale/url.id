var app = angular.module('myApp', ['ngRoute'], function($locationProvider) {
    $locationProvider.html5Mode(true);
});



app.controller('ErrorCtrl', ['$scope', '$http', '$location', '$window', function($scope, $http, $location, $window) {


    $scope.erroreErr = $location.search()['reason'];


    if($scope.erroreErr == "expired"){
        $scope.erroreErr = "Sorry, short url was expired!";
        $scope.image = "clock.ico";
    }
    else{
        $scope.erroreErr = "Sorry, internal server error!";
        $scope.image = "error500.gif";
    }

}]);
