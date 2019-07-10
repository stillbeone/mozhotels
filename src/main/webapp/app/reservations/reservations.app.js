(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('reservations', {
            parent: 'app-frontOffice',
            url: '/reservations',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/reservations/reservations.html',
                    controller: 'reservationsController',
                    controllerAs: 'vm'
                }
            },
            // resolve: {
            //     mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
            //         $translatePartialLoader.addPart('reservations');
            //         return $translate.refresh();
            //     }]
            // }
        });
    }
})();
