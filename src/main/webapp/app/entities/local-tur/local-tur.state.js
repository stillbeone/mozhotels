(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('local-tur', {
            parent: 'entity',
            url: '/local-tur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.localTur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/local-tur/local-turs.html',
                    controller: 'LocalTurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('localTur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('local-tur-detail', {
            parent: 'entity',
            url: '/local-tur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.localTur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/local-tur/local-tur-detail.html',
                    controller: 'LocalTurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('localTur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LocalTur', function($stateParams, LocalTur) {
                    return LocalTur.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('local-tur.new', {
            parent: 'local-tur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/local-tur/local-tur-dialog.html',
                    controller: 'LocalTurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                localTurName: null,
                                description: null,
                                photoPrincipal: null,
                                photoPrincipalContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('local-tur', null, { reload: true });
                }, function() {
                    $state.go('local-tur');
                });
            }]
        })
        .state('local-tur.edit', {
            parent: 'local-tur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/local-tur/local-tur-dialog.html',
                    controller: 'LocalTurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LocalTur', function(LocalTur) {
                            return LocalTur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('local-tur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('local-tur.delete', {
            parent: 'local-tur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/local-tur/local-tur-delete-dialog.html',
                    controller: 'LocalTurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LocalTur', function(LocalTur) {
                            return LocalTur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('local-tur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
