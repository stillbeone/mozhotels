(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('intro', {
            parent: '',
            url: '/intro',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/intro/intro.html',
                    controller: 'IntroController',
                    controllerAs: 'vm'
                }
            },
            // resolve: {
            //     mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
            //         $translatePartialLoader.addPart('intro');
            //         return $translate.refresh();
            //     }]
            // }
        });
    }
})();
