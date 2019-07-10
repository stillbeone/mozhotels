(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('join-now', {
            parent: '',
            url: '/join-now',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/join-now/join-now.html',
                    controller: 'JoinNowController',
                    controllerAs: 'vm'
                }
            },
            // resolve: {
            //     mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
            //         $translatePartialLoader.addPart('join-now');
            //         return $translate.refresh();
            //     }]
            // }
        });
    }
})();
