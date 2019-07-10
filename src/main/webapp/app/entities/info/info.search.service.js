(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('InfoSearch', InfoSearch);

    InfoSearch.$inject = ['$resource'];

    function InfoSearch($resource) {
        var resourceUrl =  'api/_search/infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
