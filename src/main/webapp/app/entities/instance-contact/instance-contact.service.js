(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceContact', InstanceContact);

    InstanceContact.$inject = ['$resource'];

    function InstanceContact ($resource) {
        var resourceUrl =  'api/instance-contacts/:id';

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
