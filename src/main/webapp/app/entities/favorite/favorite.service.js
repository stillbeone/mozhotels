(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('Favorite', Favorite);

    Favorite.$inject = ['$resource'];

    function Favorite ($resource) {
        var resourceUrl =  'api/favorites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
