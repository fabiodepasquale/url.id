var app = angular.module('myApp', ['googlechart', 'ngRoute', 'ngInputDate'], function($locationProvider) {
    $locationProvider.html5Mode(true);
});


app.controller('StatCtrl', ['$scope', '$http', '$location', '$filter', function($scope, $http, $location, $filter) {


    //show all info and stat, while url is not found hide info
    $scope.showUrl = false;
    $scope.errorUrl = true;
    $scope.infoMessage = "Searching Url...";


    //Url info
    $scope.showUrlInfo = true;



    //date validation
    $scope.startD = new Date(2015,0,1);
    $scope.endD = new Date();


    $scope.verifyStartD = function(){

        if(!$scope.myForm.inputS.$valid || !($scope.startD < $scope.endD)){

            $scope.startD = new Date(2015,0,1);
            alert("Error: date must be in a valid format and lesser then end date");
        }
    }

    $scope.verifyEndD = function(){

        if(!$scope.myForm.inputE.$valid || !($scope.startD < $scope.endD)){
            $scope.startD = new Date(2015,0,1);

            $scope.endD = new Date();
            alert("Error: date must be in a valid format and greater then start date");
        }
    }





    //general chart setting, a copy to format all chart together
    var chartTemp = {};
    chartTemp.type = "PieChart";
    chartTemp.options = {
        displayExactValues: true,
        width: 400,
        height: 250,
        backgroundColor : 'transparent',
        is3D: true,
        legend : {position: 'right', textStyle: {top: 10, color: 'white', fontSize: 16}},
        chartArea: {top: 10, left: 60, width:'200%',height:'200%'}
    };
    chartTemp.formatters = {
        number : [{
            columnNum: 1,
            pattern: "Clicks #0"
        }]
    };


    //div Browser
    $scope.showBrStats = false;

    //Browser data
    $scope.chartBr = chartTemp;

    $scope.chartBr.data = [
        ['Browser', 'Clicks']
    ];
    //chartTemp.data.push(['Services',20000]);


    //div Os
    $scope.showOsStats = false;

    //Os data
    $scope.chartOs = JSON.parse(JSON.stringify(chartTemp)); //copy object

    $scope.chartOs.data = [
        ['Os', 'Clicks']
    ];


    //div Country
    $scope.showCcStats = false;

    $scope.showDivCc = function() {
        alert($scope.showCcStats);
        $scope.showCcStats = !$scope.showCcStats;
        alert($scope.showCcStats);
    };


    //Country data
    $scope.chartCc = JSON.parse(JSON.stringify(chartTemp));

    $scope.chartCc.data = [
        ['Country', 'Clicks']
    ];

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



}]);

