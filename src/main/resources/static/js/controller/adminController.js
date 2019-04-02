'use strict';

App.controller('adminController', ['$scope', 'ControlService', '$interval', function($scope, ControlService, $interval) {

    var enableGame = function () {
        ControlService.enableGame();
    };

    var disableGame = function () {
        ControlService.disableGame();
    };

    var notifyWinners = function () {
        ControlService.notifyWinners();
    };

    var sendResults = function () {
        ControlService.sendResults();
    };

}]);
