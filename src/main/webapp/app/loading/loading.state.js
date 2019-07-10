(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('loading', {
            parent: '',
            url: '/loading',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/loading/loading.html',
                    controller: 'LoadingController',
                    controllerAs: 'vm'
                }
            },
            // resolve: {
            //     mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
            //         $translatePartialLoader.addPart('loading');
            //         return $translate.refresh();
            //     }]
            // }
        });
    }
})();
