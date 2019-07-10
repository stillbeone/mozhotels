(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('province', {
            parent: 'entity',
            url: '/province',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.province.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/province/provinces.html',
                    controller: 'ProvinceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('province');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('province-detail', {
            parent: 'entity',
            url: '/province/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.province.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/province/province-detail.html',
                    controller: 'ProvinceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('province');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Province', function($stateParams, Province) {
                    return Province.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('province.new', {
            parent: 'province',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/province/province-dialog.html',
                    controller: 'ProvinceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                provinceName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('province', null, { reload: true });
                }, function() {
                    $state.go('province');
                });
            }]
        })
        .state('province.edit', {
            parent: 'province',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/province/province-dialog.html',
                    controller: 'ProvinceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Province', function(Province) {
                            return Province.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('province', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('province.delete', {
            parent: 'province',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/province/province-delete-dialog.html',
                    controller: 'ProvinceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Province', function(Province) {
                            return Province.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('province', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
