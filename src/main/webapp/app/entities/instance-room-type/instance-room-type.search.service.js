(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceRoomTypeSearch', InstanceRoomTypeSearch);

    InstanceRoomTypeSearch.$inject = ['$resource'];

    function InstanceRoomTypeSearch($resource) {
        var resourceUrl =  'api/_search/instance-room-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
