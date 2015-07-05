var app = angular.module('myApp', ['ngRoute'], function($locationProvider) {
    $locationProvider.html5Mode(true);
});



app.controller('UrlCtrl', ['$scope', '$http', '$location', '$window', function($scope, $http, $location, $window) {


    //show all info and stat, while url is not found hide info
    $scope.infoMessage = "Searching Url...";

    //div Browser
    $scope.showIPass = false;



    //get data from server
    $scope.method = 'GET';
    $scope.url = 'http://localhost:8080/api/v1/stat/';

    $scope.par = $location.search()['shortUrl'];


    $scope.fetch = function() {

        //show all info and stat, while url is not found hide info
        $scope.showUrl = false;
        $scope.infoMessage = "Searching Url...";


        $scope.code = null;
        $scope.response = null;


        var urlRequest = $scope.url + $scope.par;

        if($scope.pass  != undefined){
            urlRequest = urlRequest + '?pass=' + $scope.pass;
        }

        $http({method: $scope.method, url: urlRequest}).
            success(function(data, status) {

                $scope.data = data;
                $scope.status = status;

                //link found, else error message
                if(data.error == null){
                    //Url information
                    $scope.longUrl = data.longUrl;
                    $scope.shortUrl = data.shortUrl;

                    $window.location.href = $scope.longUrl;

                }
                else{
                    $scope.showUrl = false;
                    if(data.message =="Unauthorized"){
                        $scope.infoMessage = "Url http://uid.id/"+ $scope.par  +" required password";
                        $scope.showIPass = true;
                    }
                    else{
                        $scope.infoMessage = "Url http://uid.id/"+ $scope.par  +" not found or expired";
                        $scope.showIPass = false;
                    }
                }

            }).
            error(function(data, status) {
                $scope.data = data || "Request failed";
                $scope.status = status;

                $scope.infoMessage = "Error 404! Invalid request or server is down!";
                $scope.showIPass = true;

            });
    };

    $scope.fetch();

}]);
