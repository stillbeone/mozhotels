(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceRoomType', InstanceRoomType);

    InstanceRoomType.$inject = ['$resource', 'DateUtils'];

    function InstanceRoomType ($resource, DateUtils) {
        var resourceUrl =  'api/instance-room-types/:id';

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
