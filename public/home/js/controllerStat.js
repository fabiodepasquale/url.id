var app = angular.module('myApp', ['googlechart', 'ngRoute', 'ngInputDate'], function($locationProvider) {
    $locationProvider.html5Mode(true);
});


app.controller('StatCtrl', ['$scope', '$http', '$location', '$filter', function($scope, $http, $location, $filter) {

    //show all info and stat, while url is not found hide info
    $scope.showUrl = false;
    $scope.errorUrl = true;
    $scope.infoMessage = "Searching Url...";

    //Url info
    $scope.showUrlInfo = false;

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
        width: 300,
        height: 250,
        backgroundColor : 'transparent',
        is3D: true,
        legend : {position: 'relative', textStyle: {top: 5, color: 'white', fontSize: 12}},
        chartArea: {top: 20, left: 20, width:'100%',height:'100%'}
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

    //div Os
    $scope.showOsStats = false;

    //Os data
    $scope.chartOs = JSON.parse(JSON.stringify(chartTemp)); //copy object

    $scope.chartOs.data = [
        ['OS', 'Clicks']
    ];

    //div Country
    $scope.showCcStats = false;

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

        if($scope.pass != undefined){
            alert($scope.pass);
            urlRequest = urlRequest + '?pass=' + $scope.pass;
        }



        $http({method: $scope.method, url: urlRequest, params: {from: $scope.parD1, to: $scope.parD2}}).
            success(function(data, status) {

                $scope.data = data;
                $scope.status = status;


                //link found, else error message
                if(data.error == null){

                    if(data.clicks == null){
                        $data.clicks = 0;
                    }

                    $scope.clicksP = 0;  //Add clicks in the first loop

                    //browser
                    $scope.chartBr.data = [['Browser', 'Clicks']];
                    for (i = 0; i < data.analytics[0].browsers.length ; i++) {
                        $scope.chartBr.data.push([data.analytics[0].browsers[i].id , data.analytics[0].browsers[i].count ]);
                        $scope.clicksP = $scope.clicksP + data.analytics[0].browsers[i].count; //calc the click for the period
                    }

                    $scope.showBrStats = true;


                        //platforms - os
                    $scope.chartOs.data = [['OS', 'Clicks']];
                    for (i = 0; i < data.analytics[0].platforms.length ; i++) {
                        $scope.chartOs.data.push([data.analytics[0].platforms[i].id , data.analytics[0].platforms[i].count ]);
                    }

                    $scope.showOsStats = true;

                        //country
                    $scope.chartCc.data = [['Country', 'Clicks']];
                    for (i = 0; i < data.analytics[0].countries.length ; i++) {
                        $scope.chartCc.data.push([data.analytics[0].countries[i].id , data.analytics[0].countries[i].count ]);
                    }

                    $scope.showCcStats = true;

                    $scope.showUrl = true;
                    $scope.showUrlInfo = true;

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

