(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-tur-type', {
            parent: 'entity',
            url: '/instance-tur-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceTurType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-tur-type/instance-tur-types.html',
                    controller: 'InstanceTurTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceTurType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-tur-type-detail', {
            parent: 'entity',
            url: '/instance-tur-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceTurType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-tur-type/instance-tur-type-detail.html',
                    controller: 'InstanceTurTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceTurType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceTurType', function($stateParams, InstanceTurType) {
                    return InstanceTurType.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('instance-tur-type.new', {
            parent: 'instance-tur-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur-type/instance-tur-type-dialog.html',
                    controller: 'InstanceTurTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceTurTypeName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-tur-type', null, { reload: true });
                }, function() {
                    $state.go('instance-tur-type');
                });
            }]
        })
        .state('instance-tur-type.edit', {
            parent: 'instance-tur-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur-type/instance-tur-type-dialog.html',
                    controller: 'InstanceTurTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceTurType', function(InstanceTurType) {
                            return InstanceTurType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-tur-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-tur-type.delete', {
            parent: 'instance-tur-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur-type/instance-tur-type-delete-dialog.html',
                    controller: 'InstanceTurTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceTurType', function(InstanceTurType) {
                            return InstanceTurType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-tur-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
