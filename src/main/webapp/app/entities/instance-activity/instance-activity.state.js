(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-activity', {
            parent: 'entity',
            url: '/instance-activity',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceActivity.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-activity/instance-activities.html',
                    controller: 'InstanceActivityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceActivity');
                    $translatePartialLoader.addPart('activityArea');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-activity-detail', {
            parent: 'entity',
            url: '/instance-activity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceActivity.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-activity/instance-activity-detail.html',
                    controller: 'InstanceActivityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceActivity');
                    $translatePartialLoader.addPart('activityArea');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceActivity', function($stateParams, InstanceActivity) {
                    return InstanceActivity.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('instance-activity.new', {
            parent: 'instance-activity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-activity/instance-activity-dialog.html',
                    controller: 'InstanceActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceActivityName: null,
                                area: null,
                                description: null,
                                photoPrincipal: null,
                                photoPrincipalContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-activity', null, { reload: true });
                }, function() {
                    $state.go('instance-activity');
                });
            }]
        })
        .state('instance-activity.edit', {
            parent: 'instance-activity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-activity/instance-activity-dialog.html',
                    controller: 'InstanceActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceActivity', function(InstanceActivity) {
                            return InstanceActivity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-activity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-activity.delete', {
            parent: 'instance-activity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-activity/instance-activity-delete-dialog.html',
                    controller: 'InstanceActivityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceActivity', function(InstanceActivity) {
                            return InstanceActivity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-activity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
