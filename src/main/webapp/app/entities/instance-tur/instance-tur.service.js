(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceTur', InstanceTur);

    InstanceTur.$inject = ['$resource', 'DateUtils'];

    function InstanceTur ($resource, DateUtils) {
        var resourceUrl =  'api/instance-turs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                        data.editDate = DateUtils.convertDateTimeFromServer(data.editDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
