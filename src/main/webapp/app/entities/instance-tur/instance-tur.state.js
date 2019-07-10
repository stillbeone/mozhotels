(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-tur', {
            parent: 'entity',
            url: '/instance-tur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceTur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-tur/instance-turs.html',
                    controller: 'InstanceTurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceTur');
                    $translatePartialLoader.addPart('instanceRating');
                    $translatePartialLoader.addPart('currency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-tur-detail', {
            parent: 'entity',
            url: '/instance-tur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceTur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-tur/instance-tur-detail.html',
                    controller: 'InstanceTurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceTur');
                    $translatePartialLoader.addPart('instanceRating');
                    $translatePartialLoader.addPart('currency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceTur', function($stateParams, InstanceTur) {
                    return InstanceTur.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('instance-tur.new', {
            parent: 'instance-tur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur/instance-tur-dialog.html',
                    controller: 'InstanceTurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceTurName: null,
                                rating: null,
                                description: null,
                                latitude: null,
                                longitude: null,
                                rooms: null,
                                beds: null,
                                floors: null,
                                currency: null,
                                photoPrincipal: null,
                                photoPrincipalContentType: null,
                                agreementNumber: null,
                                createDate: null,
                                editDate: null,
                                active: null,
                                approval: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-tur', null, { reload: true });
                }, function() {
                    $state.go('instance-tur');
                });
            }]
        })
        .state('instance-tur.edit', {
            parent: 'instance-tur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur/instance-tur-dialog.html',
                    controller: 'InstanceTurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceTur', function(InstanceTur) {
                            return InstanceTur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-tur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-tur.delete', {
            parent: 'instance-tur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur/instance-tur-delete-dialog.html',
                    controller: 'InstanceTurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceTur', function(InstanceTur) {
                            return InstanceTur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-tur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
