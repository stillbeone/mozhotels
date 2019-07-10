(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('our-hotels', {
            parent: 'app-frontOffice',
            url: '/our-hotels',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/our-hotels/our-hotels.html',
                    controller: 'OurHotelsController',
                    controllerAs: 'vm'
                }
            },
            // resolve: {
            //     mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
            //         $translatePartialLoader.addPart('our-hotels');
            //         return $translate.refresh();
            //     }]
            // }
        });
    }
})();
