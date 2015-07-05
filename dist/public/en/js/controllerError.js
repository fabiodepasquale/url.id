'use strict';

var app = angular.module('app', [], function($locationProvider) {
    $locationProvider.html5Mode(true);
});



app.controller('ErrorCtrl', ['$scope', '$location', '$window', function($scope, $location, $window) {

    $scope.expired = false;
    $scope.serverError = false;

    $scope.errorReason = $location.search()['reason'];

    if($scope.errorReason == "expired"){
        $scope.expired = true;
    }
    else if ($scope.errorReason == "server-error"){
        $scope.serverError=true;
    }
    else {
        $window.location.href = '/404.html';
    }

}]);
