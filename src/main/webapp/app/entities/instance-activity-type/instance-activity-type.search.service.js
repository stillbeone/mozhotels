(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceActivityTypeSearch', InstanceActivityTypeSearch);

    InstanceActivityTypeSearch.$inject = ['$resource'];

    function InstanceActivityTypeSearch($resource) {
        var resourceUrl =  'api/_search/instance-activity-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
