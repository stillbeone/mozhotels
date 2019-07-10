(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('favorite', {
            parent: 'entity',
            url: '/favorite',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.favorite.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/favorite/favorites.html',
                    controller: 'FavoriteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('favorite');
                    $translatePartialLoader.addPart('favoriteType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('favorite-detail', {
            parent: 'entity',
            url: '/favorite/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.favorite.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/favorite/favorite-detail.html',
                    controller: 'FavoriteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('favorite');
                    $translatePartialLoader.addPart('favoriteType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Favorite', function($stateParams, Favorite) {
                    return Favorite.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('favorite.new', {
            parent: 'favorite',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/favorite/favorite-dialog.html',
                    controller: 'FavoriteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                favoriteType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('favorite', null, { reload: true });
                }, function() {
                    $state.go('favorite');
                });
            }]
        })
        .state('favorite.edit', {
            parent: 'favorite',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/favorite/favorite-dialog.html',
                    controller: 'FavoriteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Favorite', function(Favorite) {
                            return Favorite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('favorite', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('favorite.delete', {
            parent: 'favorite',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/favorite/favorite-delete-dialog.html',
                    controller: 'FavoriteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Favorite', function(Favorite) {
                            return Favorite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('favorite', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
