(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tourist', {
            parent: 'entity',
            url: '/tourist',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.tourist.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tourist/tourists.html',
                    controller: 'TouristController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tourist');
                    $translatePartialLoader.addPart('language');
                    $translatePartialLoader.addPart('currency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tourist-detail', {
            parent: 'entity',
            url: '/tourist/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.tourist.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tourist/tourist-detail.html',
                    controller: 'TouristDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tourist');
                    $translatePartialLoader.addPart('language');
                    $translatePartialLoader.addPart('currency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tourist', function($stateParams, Tourist) {
                    return Tourist.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('tourist.new', {
            parent: 'tourist',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tourist/tourist-dialog.html',
                    controller: 'TouristDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                phoneNumber: null,
                                countryResidence: null,
                                countryBooking: null,
                                language: null,
                                currency: null,
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
                    $state.go('tourist', null, { reload: true });
                }, function() {
                    $state.go('tourist');
                });
            }]
        })
        .state('tourist.edit', {
            parent: 'tourist',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tourist/tourist-dialog.html',
                    controller: 'TouristDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tourist', function(Tourist) {
                            return Tourist.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tourist', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tourist.delete', {
            parent: 'tourist',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tourist/tourist-delete-dialog.html',
                    controller: 'TouristDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tourist', function(Tourist) {
                            return Tourist.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tourist', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
