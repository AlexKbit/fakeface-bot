'use strict';

App.controller('adminController', ['$scope', 'ControlService', '$interval', function($scope, ControlService, $interval) {

    var enableGame = function () {
        ControlService.enableGame().then(function (results) {
               console.log(results);
           }, function (error) {
               console.log(error.message);
           });
    };

    var disableGame = function () {
        ControlService.disableGame().then(function (results) {
               console.log(results);
           }, function (error) {
               console.log(error.message);
           });
    };

    var notifyWinners = function () {
        ControlService.notifyWinners().then(function (results) {
               console.log(results);
           }, function (error) {
               console.log(error.message);
           });
    };

    var sendResults = function () {
        ControlService.sendResults().then(function (results) {
               console.log(results);
           }, function (error) {
               console.log(error.message);
           });
    };

}]);
