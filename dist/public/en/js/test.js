var app = angular.module('myApp', ['googlechart', 'ngRoute', 'ngInputDate'], function($locationProvider) {
    $locationProvider.html5Mode(true);
});


app.controller('StatCtrl', ['$scope', '$http', '$location', '$window', '$filter', function($scope, $http, $location, $window, $filter) {

    // Initial div settings
    $scope.content = false;
    $scope.browsersStat = false;
    $scope.countriesStat = false;
    $scope.platformsStat = false;
    $scope.passwordRequired = false;

    // Charts settings
    var chart = {};
    chart.type = "PieChart";
    chart.options = {
        displayExactValues: true,
        width: 300,
        height: 250,
        backgroundColor: 'transparent',
        is3D: true,
        legend: {position: 'relative', textStyle: {top: 5, color: 'white', fontSize: 12}},
        chartArea: {top: 20, left: 20, width: '100%', height: '100%'}
    };
    chart.formatters = {
        number: [{
            columnNum: 1,
            pattern: "Clicks #0"
        }]
    };

    // Browsers chart
    $scope.BrowsersChart = chart;

    $scope.BrowsersChart.data = [
        ['Browser', 'Clicks']
    ];

    // Platforms chart
    $scope.PlatformsChart = JSON.parse(JSON.stringify(chart)); // chart object copy

    $scope.PlatformsChart.data = [
        ['Platform', 'Clicks']
    ];

    // Countries chart
    $scope.CountriesChart = JSON.parse(JSON.stringify(chart));

    $scope.CountriesChart.data = [
        ['Country', 'Clicks']
    ];


    /*
    *   Retrieve information from server
    */

    $scope.fetch = function() {

        // Setting default variables
        $scope.error = false;
        $scope.expired = false;
        var fromDate = new Date(2015,0,1);  // set fromDate to day in witch service starts
        var toDate = new Date();            // set toDate to current time

        // API request settings
        var entryPoint = 'http://localhost:8080/api/v1/stat/';      // API entry-point for statistics
        var shortUrl = $location.search()['shortUrl'];              // look for shortUrl in the url
        var filterDate = "yyyy-MM-ddTHH:mm:ss";                     // set date format to ISO-8601

        fromDate = $filter('date')(fromDate , filterDate);
        toDate = $filter('date')(toDate , filterDate);

        var requestUrl = entryPoint + shortUrl;

        if($scope.password != undefined){
            requestUrl = requestUrl + '?pass=' + $scope.password;
        }

        $http({method: 'GET', url: requestUrl, params: {from: fromDate, to: toDate}}).
            success(function(data) {

                if(data.code == 200){     // data retrieved with success

                    if(data.clicks == null){
                        data.clicks = 0;
                    }

                    $scope.partialClicks = 0;  // clicks in requested period

                    // Populate browsers chart
                    for (var i = 0; i < data.analytics[0].browsers.length ; i++) {
                        $scope.BrowsersChart.data.push([data.analytics[0].browsers[i].id , data.analytics[0].browsers[i].count ]);
                        $scope.partialClicks = $scope.partialClicks + data.analytics[0].browsers[i].count; // count clicks in requested period
                    }

                    // Populate platforms chart
                    for (i = 0; i < data.analytics[0].platforms.length ; i++) {
                        $scope.PlatformsChart.data.push([data.analytics[0].platforms[i].id , data.analytics[0].platforms[i].count ]);
                    }

                    // Populate countries chart
                    for (i = 0; i < data.analytics[0].countries.length ; i++) {
                        $scope.CountriesChart.data.push([data.analytics[0].countries[i].id , data.analytics[0].countries[i].count ]);
                    }

                    $scope.info = data;  // put retrieved data to object "info"

                    // set charts and other information visible
                    $scope.browsersStat = true;
                    $scope.platformsStat = true;
                    $scope.countriesStat = true;
                    $scope.content = true;

                }
                else if(data.code == 401){    // unauthorized
                    $scope.errorMessage = data.error.message;
                    $scope.error = true;    // set error visible
                    $scope.passwordRequired = true; // show input form
                }
                else if(data.code == 204) { // expired
                    $scope.expired = true;
                    $scope.errorMessage = data.error.message;
                    $scope.error = true;    // set error visible
                }
                else if (data.code == 404){
                    $window.location.href = '/404.html';
                }
                else {
                    $scope.errorMessage = data.error.message;
                    $scope.error = true;    // set error visible
                }
            }).
            error(function(data) {
                $scope.errorMessage = "Unknown Error. Please, try again.";
                $scope.error = true;    // set error visible
            });
    };

    $scope.fetch();

}]);

