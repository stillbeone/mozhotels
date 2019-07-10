(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('FavoriteSearch', FavoriteSearch);

    FavoriteSearch.$inject = ['$resource'];

    function FavoriteSearch($resource) {
        var resourceUrl =  'api/_search/favorites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
