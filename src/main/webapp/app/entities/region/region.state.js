(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('region', {
            parent: 'entity',
            url: '/region',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.region.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/region/regions.html',
                    controller: 'RegionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('region');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('region-detail', {
            parent: 'entity',
            url: '/region/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.region.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/region/region-detail.html',
                    controller: 'RegionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('region');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Region', function($stateParams, Region) {
                    return Region.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('region.new', {
            parent: 'region',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/region/region-dialog.html',
                    controller: 'RegionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                regionName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('region', null, { reload: true });
                }, function() {
                    $state.go('region');
                });
            }]
        })
        .state('region.edit', {
            parent: 'region',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/region/region-dialog.html',
                    controller: 'RegionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Region', function(Region) {
                            return Region.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('region', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('region.delete', {
            parent: 'region',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/region/region-delete-dialog.html',
                    controller: 'RegionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Region', function(Region) {
                            return Region.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('region', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
