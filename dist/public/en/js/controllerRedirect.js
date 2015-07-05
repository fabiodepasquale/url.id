var app = angular.module('myApp', ['ngRoute'], function($locationProvider) {
    $locationProvider.html5Mode(true);
});



app.controller('RedirectCtrl', ['$scope', '$http', '$location', '$window', function($scope, $http, $location, $window) {

    // Initial div settings
    $scope.passwordRequired = false;

    $scope.fetch = function() {

        // Setting default variables
        $scope.expired = false;

        // API request settings
        var entryPoint = 'http://localhost:8080/api/v1/url';      // API entry-point for statistics
        var shortUrl = $location.search()['shortUrl'];              // look for shortUrl in the url

        var requestUrl = entryPoint + "?shortUrl=" + shortUrl;

        if($scope.password != undefined){
            requestUrl = requestUrl + '&pass=' + $scope.password;
        }

        $http({method: 'GET', url: requestUrl}).
            success(function(data) {
                if(data.code == 200){     // data retrieved with success
                    $window.location.href = data.longUrl;
                }
                else if(data.code == 401){    // unauthorized
                    $scope.errorMessage = data.error.message;
                    $scope.passwordRequired = true; // show input form
                }
                else if(data.code == 204) { // expired
                    $scope.expired = true;
                }
                else if (data.code == 400 || data.code == 404) {    // empty shortUrl parameter
                    $window.location.href = "/404.html";
                }
            }).
            error(function(data) {
            });
    };

    $scope.fetch();

}]);
