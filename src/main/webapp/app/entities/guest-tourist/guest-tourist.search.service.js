(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('GuestTouristSearch', GuestTouristSearch);

    GuestTouristSearch.$inject = ['$resource'];

    function GuestTouristSearch($resource) {
        var resourceUrl =  'api/_search/guest-tourists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
