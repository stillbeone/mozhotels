(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-room-type', {
            parent: 'entity',
            url: '/instance-room-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceRoomType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-room-type/instance-room-types.html',
                    controller: 'InstanceRoomTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceRoomType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-room-type-detail', {
            parent: 'entity',
            url: '/instance-room-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceRoomType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-room-type/instance-room-type-detail.html',
                    controller: 'InstanceRoomTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceRoomType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceRoomType', function($stateParams, InstanceRoomType) {
                    return InstanceRoomType.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('instance-room-type.new', {
            parent: 'instance-room-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-type/instance-room-type-dialog.html',
                    controller: 'InstanceRoomTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceRoomTypeName: null,
                                description: null,
                                roomQuantity: null,
                                capacityAdults: null,
                                capacityChildren: null,
                                onlinePrice: null,
                                branchPrice: null,
                                taxInclude: null,
                                tax: null,
                                photoPrincipal: null,
                                photoPrincipalContentType: null,
                                createDate: null,
                                editDate: null,
                                active: null,
                                approval: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-room-type', null, { reload: true });
                }, function() {
                    $state.go('instance-room-type');
                });
            }]
        })
        .state('instance-room-type.edit', {
            parent: 'instance-room-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-type/instance-room-type-dialog.html',
                    controller: 'InstanceRoomTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceRoomType', function(InstanceRoomType) {
                            return InstanceRoomType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-room-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-room-type.delete', {
            parent: 'instance-room-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-type/instance-room-type-delete-dialog.html',
                    controller: 'InstanceRoomTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceRoomType', function(InstanceRoomType) {
                            return InstanceRoomType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-room-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
