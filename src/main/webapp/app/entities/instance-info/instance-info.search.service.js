(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceInfoSearch', InstanceInfoSearch);

    InstanceInfoSearch.$inject = ['$resource'];

    function InstanceInfoSearch($resource) {
        var resourceUrl =  'api/_search/instance-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
