(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('GuestTourist', GuestTourist);

    GuestTourist.$inject = ['$resource'];

    function GuestTourist ($resource) {
        var resourceUrl =  'api/guest-tourists/:id';

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
