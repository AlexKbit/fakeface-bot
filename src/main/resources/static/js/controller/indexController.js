'use strict';

App.controller('indexController', ['$scope', 'AccountService', '$interval', function($scope, AccountService, $interval) {

    $scope.topIncorrectImage = "/top/incorrect";
    $scope.totalAccounts = 0;
    $scope.totalCompleted = 0;
    $scope.accounts = [];

    $scope.botName = '';
    $scope.botVersion = '';

    var loadStat = function () {
        AccountService.loadStat().then(function (results) {
            console.log(results);

            $scope.totalAccounts = results.totalTotal;
            $scope.totalCompleted = results.totalFinished;
            $scope.accounts = results.top;

        }, function (error) {
            console.log(error.message);
        });
    };

    var loadInfo = function () {
        AccountService.loadInfo().then(function (results) {
            console.log(results);

            $scope.botName = results.botName;
            $scope.botVersion = results.version;

        }, function (error) {
            console.log(error.message);
        });
    };

    loadInfo();
    loadStat();

    $scope.reloadRoute = function () {
        $route.reload();
    };

    $scope.pageChanged = function () {
        loadStat();
    };

    $interval(function(){
        $scope.pageChanged();
        $scope.topIncorrectImage = "/top/incorrect";
    }.bind(this), 3000);

    var toUTCDate = function(date){
        var _utc = new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(),  date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds(), date.getUTCMilliseconds());
        return _utc;
    };

    var millisToUTCDate = function(millis){
        return toUTCDate(new Date(millis));
    };

    $scope.toUTCDate = toUTCDate;
    $scope.millisToUTCDate = millisToUTCDate;

}]);
