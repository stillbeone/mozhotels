(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceActivityType', InstanceActivityType);

    InstanceActivityType.$inject = ['$resource'];

    function InstanceActivityType ($resource) {
        var resourceUrl =  'api/instance-activity-types/:id';

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
