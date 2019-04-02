'use strict';

App.factory('ControlService', ['$http', '$q', function($http, $q){

    var HOST = "/";

    return {

        enableGame: function() {
            return $http.get(HOST + 'enable');
        },

        disableGame: function() {
            return $http.get(HOST + 'disable');
        },

        notifyWinners: function() {
            return $http.get(HOST + 'notify');
        },

        sendResults: function() {
            return $http.get(HOST + 'results');
        }
    };

}]);
