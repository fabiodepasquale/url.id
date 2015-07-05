var app = angular.module('myApp', ['ngRoute', 'ngInputDate'], function($locationProvider) {
    $locationProvider.html5Mode(true);
});


app.controller('IndexCtrl', ['$scope', '$http', '$location', '$filter', function($scope, $http, $location, $filter) {


    //show all info and stat, while url is not found hide info
    $scope.showOption = false;

    //show the form to hort url
    $scope.showInsertForm = true;


    //reset option camp
    $scope.resetOption = function(){

        $scope.longUrl = null;

        $scope.shortUrl = "";

        $scope.protected = false;
        $scope.password = "";

        $scope.showExpiredTime = false;
        $scope.expiredTime = null;

        $scope.showExpiredClicks = false;
        $scope.expiredClicks = 0;

    }

    $scope.resetOption();



    //short Url controll
    $scope.resetShortUrl = function(){
        $scope.shortUrl = "";
    }


    //password and protected contoll
    $scope.resetPassword = function(){
        $scope.password = "";
    }
    $scope.showHidePassword = function(){
        $scope.protected = false;
        $scope.password = "";
    }



    //expired clicks controller
    $scope.resetExpiredClicks = function(){
        $scope.expiredClicks = 0;
    }
    $scope.verifyExpiredClicks = function(){
        if(!($scope.expiredClicks % 1 === 0)){
            $scope.expiredClicks = 0;
        }
    }
    $scope.showHideExpiredClicks = function(){
        $scope.showExpiredClicks = !$scope.showExpiredClicks;
        $scope.expiredClicks = 0;
    }



    //expired date validation
    $scope.verifyExpiredTime = function(){

        if($scope.expiredTime < new Date()){

            $scope.expiredTime = new Date();
            alert("Error: date must be in a valid format and greater then today date");
        }
    }
    $scope.resetExpiredTime = function(){
        $scope.expiredTime = null;
    }
    $scope.showHideExpiredTime = function(){
        $scope.showExpiredTime = !$scope.showExpiredTime;
        $scope.expiredTime = null;
    }






    //get data from server
    $scope.method = 'POST';
    $scope.url = 'http://localhost:8080/api/v1/url';

    $scope.filterDate = "yyyy-MM-dd'T'HH:mm:ss.sssZ";

    $scope.postUrl = function() {

        $scope.infoMessage = "Shorting Url...";


        //control param expired Time and convert it
        if($scope.expiredTime == null){
            $scope.expiredTime = "0";
        }
        else{
            $scope.expiredTime = $filter('date')($scope.expiredTime , $scope.filterDate);
        }




        $http({method: $scope.method,
                url: $scope.url,
                params: {
                    longUrl: $scope.longUrl,
                    shortUrl: $scope.shortUrl,
                    pass: $scope.password,
                    extime: $scope.expiredTime,
                    exclicks: $scope.expiredClicks
               }}).
            success(function(data, status) {

                $scope.data = data;
                $scope.status = status;


                //link found, else error message
                if(data.error == null){
                    $scope.showInsertForm = false;
                    $scope.infoMessage = data;
                }
                else{
                    $scope.infoMessage = data;
                    $scope.showInsertForm = true;

                    if(data.code == 400){
                        $scope.infoMessage.error.message = "Long url is malformed, please insert a valid link!";
                    }
                    else if(data.code == 404){
                        $scope.infoMessage.error.message = "Long urlis not found, please insert a valid link!";
                    }
                    else{
                        $scope.infoMessage.error.message = "Long url server is down, please try again later!";
                    }

                }


            }).
            error(function(data, status) {
                $scope.data = data || "Request failed";
                $scope.status = status;

                $scope.showUrl = false;
                $scope.infoMessage.error.message = "Error 404! Invalid request or server is down!";
                $scope.errorUrl = true;

            });
    };



    $scope.verifyData = function(){
        var reg = /^[a-z\d_-]+$/i;

        var validShortUrl = false;
        if($scope.shortUrl == "" || (reg.test($scope.shortUrl) && $scope.shortUrl.length < 21))
        {
            validShortUrl = true;
        }

        if($scope.longUrl != null && $scope.longUrl != "" && validShortUrl == true ){

            $scope.postUrl();

        }
        else{
            alert("Insert Url to shorten it");
        }

    }









    /*



    //get data from server
    $scope.method = 'GET';
    $scope.url = 'http://localhost:8080/api/v1/stat/';

    $scope.par = $location.search()['shortUrl'];

    $scope.filterDate = "yyyy-MM-dd'T'HH:mm:ss.sssZ";

    $scope.fetch = function() {

        //show all info and stat, while url is not found hide info
        $scope.showUrl = false;
        $scope.errorUrl = true;
        $scope.infoMessage = "Cercando l'Url...";

        $scope.parD1 = $filter('date')($scope.startD , $scope.filterDate);
        $scope.parD2 = $filter('date')($scope.endD , $scope.filterDate);

        $scope.code = null;
        $scope.response = null;


        var urlRequest = $scope.url + $scope.par;

        if($scope.pass  != undefined){
            urlRequest = urlRequest + '?pass=' + $scope.pass;
        }



        $http({method: $scope.method, url: urlRequest, params: {from: $scope.parD1, to: $scope.parD2}}).
            success(function(data, status) {

                $scope.data = data;
                $scope.status = status;


                //link found, else error message
                if(data.error == null){
                    //Url information
                    $scope.longUrl = data.longUrl;
                    $scope.shortUrl = data.shortUrl;
                    $scope.title = data.pageTitle;
                    $scope.status = data.status;
                    if(data.clicks == null){
                        $scope.clicks = 0;
                    }
                    else{
                        $scope.clicks = data.clicks;
                    }
                    $scope.clicksP = 0;  //Add clicks in the first loop
                    $scope.createdTime = new Date(data.createdTime);



                    //browser
                    $scope.chartBr.data = [['Browser', 'Clicks']];
                    for (i = 0; i < data.analytics[0].browsers.length ; i++) {
                        $scope.chartBr.data.push([data.analytics[0].browsers[i].id , data.analytics[0].browsers[i].count ]);
                        $scope.clicksP = $scope.clicksP + data.analytics[0].browsers[i].count; //calc the click for the period
                    }


                    //platforms - os
                    $scope.chartOs.data = [['Os', 'Clicks']];
                    for (i = 0; i < data.analytics[0].platforms.length ; i++) {
                        $scope.chartOs.data.push([data.analytics[0].platforms[i].id , data.analytics[0].platforms[i].count ]);
                    }


                    //country
                    $scope.chartCc.data = [['Country', 'Clicks']];
                    for (i = 0; i < data.analytics[0].countries.length ; i++) {
                        $scope.chartCc.data.push([data.analytics[0].countries[i].id , data.analytics[0].countries[i].count ]);
                    }

                    $scope.showUrl = true;

                }
                else{
                    $scope.showUrl = false;
                    if(data.message =="Unauthorized"){
                        $scope.infoMessage = "Url http://uid.id/"+ $scope.par  +" required password";
                        $scope.errorUrl = false;
                    }
                    else{
                        $scope.infoMessage = "Url http://uid.id/"+ $scope.par  +" not found or expired";
                        $scope.errorUrl = true;
                    }

                }


            }).
            error(function(data, status) {
                $scope.data = data || "Request failed";
                $scope.status = status;

                $scope.showUrl = false;
                $scope.infoMessage = "Error 404! Invalid request or server is down!";
                $scope.errorUrl = true;

            });
    };

    $scope.fetch();

    */



}]);

