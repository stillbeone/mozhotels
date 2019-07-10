(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('guest-tourist', {
            parent: 'entity',
            url: '/guest-tourist',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.guestTourist.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/guest-tourist/guest-tourists.html',
                    controller: 'GuestTouristController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('guestTourist');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('guest-tourist-detail', {
            parent: 'entity',
            url: '/guest-tourist/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.guestTourist.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/guest-tourist/guest-tourist-detail.html',
                    controller: 'GuestTouristDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('guestTourist');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GuestTourist', function($stateParams, GuestTourist) {
                    return GuestTourist.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('guest-tourist.new', {
            parent: 'guest-tourist',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/guest-tourist/guest-tourist-dialog.html',
                    controller: 'GuestTouristDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                countryResidence: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('guest-tourist', null, { reload: true });
                }, function() {
                    $state.go('guest-tourist');
                });
            }]
        })
        .state('guest-tourist.edit', {
            parent: 'guest-tourist',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/guest-tourist/guest-tourist-dialog.html',
                    controller: 'GuestTouristDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GuestTourist', function(GuestTourist) {
                            return GuestTourist.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('guest-tourist', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('guest-tourist.delete', {
            parent: 'guest-tourist',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/guest-tourist/guest-tourist-delete-dialog.html',
                    controller: 'GuestTouristDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GuestTourist', function(GuestTourist) {
                            return GuestTourist.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('guest-tourist', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
