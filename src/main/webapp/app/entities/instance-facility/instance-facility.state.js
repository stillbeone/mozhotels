(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-facility', {
            parent: 'entity',
            url: '/instance-facility',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceFacility.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-facility/instance-facilities.html',
                    controller: 'InstanceFacilityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceFacility');
                    $translatePartialLoader.addPart('instanceArea');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-facility-detail', {
            parent: 'entity',
            url: '/instance-facility/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceFacility.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-facility/instance-facility-detail.html',
                    controller: 'InstanceFacilityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceFacility');
                    $translatePartialLoader.addPart('instanceArea');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceFacility', function($stateParams, InstanceFacility) {
                    return InstanceFacility.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('instance-facility.new', {
            parent: 'instance-facility',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility/instance-facility-dialog.html',
                    controller: 'InstanceFacilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceFacilityName: null,
                                area: null,
                                description: null,
                                quantity: null,
                                price: null,
                                bookingInclude: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-facility', null, { reload: true });
                }, function() {
                    $state.go('instance-facility');
                });
            }]
        })
        .state('instance-facility.edit', {
            parent: 'instance-facility',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility/instance-facility-dialog.html',
                    controller: 'InstanceFacilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceFacility', function(InstanceFacility) {
                            return InstanceFacility.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-facility', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-facility.delete', {
            parent: 'instance-facility',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility/instance-facility-delete-dialog.html',
                    controller: 'InstanceFacilityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceFacility', function(InstanceFacility) {
                            return InstanceFacility.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-facility', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
