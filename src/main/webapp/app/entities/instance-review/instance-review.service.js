(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceReview', InstanceReview);

    InstanceReview.$inject = ['$resource', 'DateUtils'];

    function InstanceReview ($resource, DateUtils) {
        var resourceUrl =  'api/instance-reviews/:id';

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
