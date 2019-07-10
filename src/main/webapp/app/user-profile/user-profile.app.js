(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('user-profile', {
            parent: 'app-frontOffice',
            url: '/user-profile',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/user-profile/user-profile.html',
                    controller: 'UserProfileController',
                    controllerAs: 'vm'
                }
            },
            // resolve: {
            //     mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
            //         $translatePartialLoader.addPart('user-profile');
            //         return $translate.refresh();
            //     }]
            // }
        })
        .state('user-profile.profile', {
          url:         '#/user-profile/#profile',
          templateUrl: 'app/user-profile/user-profile.html'
        })
        .state('user-profile.bookings', {
          url:         '#/user-profile/#bookings',
          templateUrl: 'app/user-profile/user-profile.html'
        })
        .state('user-profile.wishlist', {
          url:         '#/user-profile/#wishlist',
          templateUrl: 'app/user-profile/user-profile.html'
        })
        .state('user-profile.settings', {
          url:         '#/user-profile/#settings',
          templateUrl: 'app/user-profile/user-profile.html'
        })
        .state('user-profile.history', {
          url:         '#/user-profile/#history',
          templateUrl: 'app/user-profile/user-profile.html'
        })
        .state('user-profile.password', {
          url:         '#/user-profile/#password',
          templateUrl: 'app/user-profile/user-profile.html'
        })
        .state('user-profile.newsletter', {
          url:         '#/user-profile/#newsletter',
          templateUrl: 'app/user-profile/user-profile.html'
        });
    }
})();
