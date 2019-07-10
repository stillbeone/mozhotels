(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('contact', {
            parent: 'app-frontOffice',
            url: '/contact',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/contact/contact.html',
                    controller: 'ContactController',
                    controllerAs: 'vm'
                }
            },
            // resolve: {
            //     mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
            //         $translatePartialLoader.addPart('contact');
            //         return $translate.refresh();
            //     }]
            // }
        });
    }
})();
