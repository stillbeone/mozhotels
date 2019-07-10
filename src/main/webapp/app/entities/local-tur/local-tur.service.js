(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('LocalTur', LocalTur);

    LocalTur.$inject = ['$resource'];

    function LocalTur ($resource) {
        var resourceUrl =  'api/local-turs/:id';

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
