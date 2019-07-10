(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('info', {
            parent: 'entity',
            url: '/info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.info.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/info/infos.html',
                    controller: 'InfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('info');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('info-detail', {
            parent: 'entity',
            url: '/info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.info.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/info/info-detail.html',
                    controller: 'InfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('info');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Info', function($stateParams, Info) {
                    return Info.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('info.new', {
            parent: 'info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/info/info-dialog.html',
                    controller: 'InfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                infoName: null,
                                description: null,
                                value: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('info', null, { reload: true });
                }, function() {
                    $state.go('info');
                });
            }]
        })
        .state('info.edit', {
            parent: 'info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/info/info-dialog.html',
                    controller: 'InfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Info', function(Info) {
                            return Info.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('info.delete', {
            parent: 'info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/info/info-delete-dialog.html',
                    controller: 'InfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Info', function(Info) {
                            return Info.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
