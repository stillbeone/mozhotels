(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceInfoType', InstanceInfoType);

    InstanceInfoType.$inject = ['$resource'];

    function InstanceInfoType ($resource) {
        var resourceUrl =  'api/instance-info-types/:id';

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
