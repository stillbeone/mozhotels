(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('TouristSearch', TouristSearch);

    TouristSearch.$inject = ['$resource'];

    function TouristSearch($resource) {
        var resourceUrl =  'api/_search/tourists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
