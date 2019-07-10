(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('detail', {
            parent: 'app-frontOffice',
            url: '/detail/:instanceID/:rooms/:adults/:childs/:checkIn/:checkOut',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/detail/detail.html',
                    controller: 'DetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                              }],
              				                                                                                                                                                                                                                                                                                                                                                entity: ['$stateParams', 'InstanceTur', function($stateParams, InstanceTur) {
                  return InstanceTur.get({id : $stateParams.instanceID});
              }]
                          }
        })
        .state('detail.summary', {
          url:         '#/detail/#summary',
          templateUrl: 'app/detail/detail.html'
        })
        .state('detail.roomrates', {
          url:         '#/detail/#roomrates',
          templateUrl: 'app/detail/detail.html'
        })
        .state('detail.preferences', {
          url:         '#/detail/#preferences',
          templateUrl: 'app/detail/detail.html'
        })
        .state('detail.maps', {
          url:         '#/detail/#maps',
          templateUrl: 'app/detail/detail.html'
        })
        .state('detail.reviews', {
          url:         '#/detail/#reviews',
          templateUrl: 'app/detail/detail.html'
        })
        .state('detail.thingstodo', {
          url:         '#/detail/#thingstodo',
          templateUrl: 'app/detail/detail.html'
        });
    }
})();
