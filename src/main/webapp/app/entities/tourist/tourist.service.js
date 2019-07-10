(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('Tourist', Tourist);

    Tourist.$inject = ['$resource', 'DateUtils'];

    function Tourist ($resource, DateUtils) {
        var resourceUrl =  'api/tourists/:id';

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
