(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('member-benefit', {
            parent: 'app-frontOffice',
            url: '/member-benefit',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/member-benefit/member-benefit.html',
                    controller: 'MemberBenefitController',
                    controllerAs: 'vm'
                }
            },
            // resolve: {
            //     mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
            //         $translatePartialLoader.addPart('member-benefit');
            //         return $translate.refresh();
            //     }]
            // }
        });
    }
})();
