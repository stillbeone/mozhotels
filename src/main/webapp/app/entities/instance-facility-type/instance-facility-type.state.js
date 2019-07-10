(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-facility-type', {
            parent: 'entity',
            url: '/instance-facility-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceFacilityType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-facility-type/instance-facility-types.html',
                    controller: 'InstanceFacilityTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceFacilityType');
                    $translatePartialLoader.addPart('facilityType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-facility-type-detail', {
            parent: 'entity',
            url: '/instance-facility-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceFacilityType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-facility-type/instance-facility-type-detail.html',
                    controller: 'InstanceFacilityTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceFacilityType');
                    $translatePartialLoader.addPart('facilityType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceFacilityType', function($stateParams, InstanceFacilityType) {
                    return InstanceFacilityType.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('instance-facility-type.new', {
            parent: 'instance-facility-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility-type/instance-facility-type-dialog.html',
                    controller: 'InstanceFacilityTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceFacilityTypeName: null,
                                facilityType: null,
                                description: null,
                                instanceFacility: null,
                                instanceRoomFacility: null,
                                instanceBookingFacility: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-facility-type', null, { reload: true });
                }, function() {
                    $state.go('instance-facility-type');
                });
            }]
        })
        .state('instance-facility-type.edit', {
            parent: 'instance-facility-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility-type/instance-facility-type-dialog.html',
                    controller: 'InstanceFacilityTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceFacilityType', function(InstanceFacilityType) {
                            return InstanceFacilityType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-facility-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-facility-type.delete', {
            parent: 'instance-facility-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility-type/instance-facility-type-delete-dialog.html',
                    controller: 'InstanceFacilityTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceFacilityType', function(InstanceFacilityType) {
                            return InstanceFacilityType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-facility-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
