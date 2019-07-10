(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceFacilitySearch', InstanceFacilitySearch);

    InstanceFacilitySearch.$inject = ['$resource'];

    function InstanceFacilitySearch($resource) {
        var resourceUrl =  'api/_search/instance-facilities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
