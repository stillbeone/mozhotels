(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('list', {
            parent: 'app-frontOffice',
            url: '/list/:place/:rooms/:adults/:childs/:checkIn/:checkOut',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/list/list.html',
                    controller: 'ListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                  $translatePartialLoader.addPart('instanceTur');
                  $translatePartialLoader.addPart('instanceRating');
                  $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            },

        });
    }
})();
