(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-info', {
            parent: 'entity',
            url: '/instance-info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-info/instance-infos.html',
                    controller: 'InstanceInfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceInfo');
                    $translatePartialLoader.addPart('infoType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-info-detail', {
            parent: 'entity',
            url: '/instance-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-info/instance-info-detail.html',
                    controller: 'InstanceInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceInfo');
                    $translatePartialLoader.addPart('infoType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceInfo', function($stateParams, InstanceInfo) {
                    return InstanceInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('instance-info.new', {
            parent: 'instance-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info/instance-info-dialog.html',
                    controller: 'InstanceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceInfo: null,
                                instanceInfoName: null,
                                info: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-info', null, { reload: true });
                }, function() {
                    $state.go('instance-info');
                });
            }]
        })
        .state('instance-info.edit', {
            parent: 'instance-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info/instance-info-dialog.html',
                    controller: 'InstanceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceInfo', function(InstanceInfo) {
                            return InstanceInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-info.delete', {
            parent: 'instance-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info/instance-info-delete-dialog.html',
                    controller: 'InstanceInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceInfo', function(InstanceInfo) {
                            return InstanceInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
