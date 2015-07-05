'use strict';

var app = angular.module('app', ['datetimepicker'], function($locationProvider) {
    $locationProvider.html5Mode(true);
});

app.config([
        'datetimepickerProvider',
        function (datetimepickerProvider) {
            datetimepickerProvider.setOptions({
                locale: 'en'
            });
        }
    ]);

app.run([
        '$rootScope',
        function ($rootScope) {
            $rootScope.scoped = {
                format: 'HH:mm:ss'
            };

            $rootScope.vm = {
                datetime: Date.now
            }


        }
    ]);

app.controller ('homeCtrl', ['$scope', '$http', '$filter', function($scope, $http) {

    $scope.content = false;



    $scope.fetch =  function() {

        $scope.error = false;

        var requestUrl = 'http://localhost:8080/api/v1/url';      // API entry-point
        var start = false;

        if ($scope.shortUrl != undefined && $scope.customNameOpt) {
            requestUrl = requestUrl + '?shortUrl=' + $scope.shortUrl;
            start = true;
        }
        else {
            $scope.shortUrl = undefined;
        }

        if($scope.password != undefined && !start && $scope.passwordOpt){
            requestUrl = requestUrl + '?pass=' + $scope.password;
        }
        else if ($scope.password != undefined && start && $scope.passwordOpt) {
            requestUrl = requestUrl + '&pass=' + $scope.password;
        }
        else {
            $scope.password = undefined;
        }

        if ($scope.vm.datetime != "" && $scope.expiryOpt) {
            $scope.extime = moment($scope.vm.datetime, 'DD.MM.YYYY HH:mm').format('YYYY-MM-DDTHH:mm:ss');
        }
        else {
            $scope.extime = 0;
        }

        if ($scope.exclicks == undefined || !$scope.clicksOpt) {
            $scope.exclicks = 0;
        }


        $http({method: 'POST', url: requestUrl, params: {longUrl: $scope.longUrl, extime: $scope.extime, exclicks: $scope.exclicks}}).
            success(function(data) {
                $scope.json = JSON.stringify(data);
                $scope.info = data;
                if(data.code == 200) {     // data retrieved with success
                    $scope.content = true;
                }
                else if (data.code == 400) {
                    alert("400");
                }
                else if (data.code == 404) {
                    alert("404");
                }
                else {
                    alert("error");
                    $scope.errorMessage = data.error.message;
                    $scope.error = true;    // set error visible
                }

            }).
            error(function(data) {
                $scope.errorMessage = "Unknown Error. Please, try again.";
                $scope.error = true;    // set error visible
            });


    };

}]);