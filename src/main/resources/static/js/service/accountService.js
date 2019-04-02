'use strict';

App.factory('AccountService', ['$http', '$q', function($http, $q){

    var HOST = "/";

    return {
        loadStat: function() {
            return $http.get(HOST + 'stat')
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while load accounts');
                        return $q.reject(errResponse);
                    }
                );
        },

        loadInfo: function() {
            return $http.get(HOST + 'info')
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while load accounts');
                        return $q.reject(errResponse);
                    }
                );
        }
    };

}]);
