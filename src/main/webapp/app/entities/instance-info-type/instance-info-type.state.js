(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-info-type', {
            parent: 'entity',
            url: '/instance-info-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceInfoType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-info-type/instance-info-types.html',
                    controller: 'InstanceInfoTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceInfoType');
                    $translatePartialLoader.addPart('infoType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-info-type-detail', {
            parent: 'entity',
            url: '/instance-info-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceInfoType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-info-type/instance-info-type-detail.html',
                    controller: 'InstanceInfoTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceInfoType');
                    $translatePartialLoader.addPart('infoType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceInfoType', function($stateParams, InstanceInfoType) {
                    return InstanceInfoType.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('instance-info-type.new', {
            parent: 'instance-info-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info-type/instance-info-type-dialog.html',
                    controller: 'InstanceInfoTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceInfoType: null,
                                instanceInfoTypeName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-info-type', null, { reload: true });
                }, function() {
                    $state.go('instance-info-type');
                });
            }]
        })
        .state('instance-info-type.edit', {
            parent: 'instance-info-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info-type/instance-info-type-dialog.html',
                    controller: 'InstanceInfoTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceInfoType', function(InstanceInfoType) {
                            return InstanceInfoType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-info-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-info-type.delete', {
            parent: 'instance-info-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info-type/instance-info-type-delete-dialog.html',
                    controller: 'InstanceInfoTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceInfoType', function(InstanceInfoType) {
                            return InstanceInfoType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-info-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
