'use strict';

App.factory('ControlService', ['$http', '$q', function($http, $q){

    var HOST = "/";

    return {

        enableGame: function() {
            return $http.get(HOST + 'enable')
                .then(
                    function(response){
                        return response;
                    },
                    function(errResponse){
                        console.error('Error while load accounts');
                        return $q.reject(errResponse);
                    }
                );
        },

        disableGame: function() {
            return $http.get(HOST + 'disable')
                .then(
                    function(response){
                        return response;
                    },
                    function(errResponse){
                        console.error('Error while load accounts');
                        return $q.reject(errResponse);
                    }
                );
        },

        notifyWinners: function() {
            return $http.get(HOST + 'notify')
                .then(
                    function(response){
                        return response;
                    },
                    function(errResponse){
                        console.error('Error while load accounts');
                        return $q.reject(errResponse);
                    }
                );
        },

        sendResults: function() {
            return $http.get(HOST + 'results')
                .then(
                    function(response){
                        return response;
                    },
                    function(errResponse){
                        console.error('Error while load accounts');
                        return $q.reject(errResponse);
                    }
                );
        }
    };

}]);
