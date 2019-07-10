(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('app-frontOffice', {
            abstract: true,
            views: {
                'frontOffice@': {
                    templateUrl: '/headerFrontOffice.html'

                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        })
        // .state('app', {
        //     abstract: true,
        //     views: {
        //         'index-bo@': {
        //             templateUrl: '/index-bo.html',
        //
        //         }
        //     }
        // })
        // .state('index-fo', {
        //     abstract: true,
        //     views: {
        //         'index-fo@': {
        //             templateUrl: '/index-fo.html',
        //
        //         }
        //     }
        // });
    }
})();
