'use strict';

App.controller('adminController', ['$scope', 'ControlService', '$interval', function($scope, ControlService, $interval) {

    $scope.enableGame = function () {
        ControlService.enableGame().then(function (results) {
               console.log(results);
           }, function (error) {
               console.log(error.message);
           });
    };

    $scope.disableGame = function () {
        ControlService.disableGame().then(function (results) {
               console.log(results);
           }, function (error) {
               console.log(error.message);
           });
    };

    $scope.notifyWinners = function () {
        ControlService.notifyWinners().then(function (results) {
               console.log(results);
           }, function (error) {
               console.log(error.message);
           });
    };

    $scope.sendResults = function () {
        ControlService.sendResults().then(function (results) {
               console.log(results);
           }, function (error) {
               console.log(error.message);
           });
    };

}]);
