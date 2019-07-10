(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('Picture', Picture);

    Picture.$inject = ['$resource'];

    function Picture ($resource) {
        var resourceUrl =  'api/pictures/:id';

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
