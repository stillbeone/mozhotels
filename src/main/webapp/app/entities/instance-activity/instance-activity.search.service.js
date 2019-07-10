(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceActivitySearch', InstanceActivitySearch);

    InstanceActivitySearch.$inject = ['$resource'];

    function InstanceActivitySearch($resource) {
        var resourceUrl =  'api/_search/instance-activities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
