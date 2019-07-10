(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-contact', {
            parent: 'entity',
            url: '/instance-contact',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceContact.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-contact/instance-contacts.html',
                    controller: 'InstanceContactController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceContact');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-contact-detail', {
            parent: 'entity',
            url: '/instance-contact/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceContact.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-contact/instance-contact-detail.html',
                    controller: 'InstanceContactDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceContact');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceContact', function($stateParams, InstanceContact) {
                    return InstanceContact.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('instance-contact.new', {
            parent: 'instance-contact',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-contact/instance-contact-dialog.html',
                    controller: 'InstanceContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                contactNumberPrincipal: null,
                                zipCode: null,
                                address: null,
                                website: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-contact', null, { reload: true });
                }, function() {
                    $state.go('instance-contact');
                });
            }]
        })
        .state('instance-contact.edit', {
            parent: 'instance-contact',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-contact/instance-contact-dialog.html',
                    controller: 'InstanceContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceContact', function(InstanceContact) {
                            return InstanceContact.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-contact', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-contact.delete', {
            parent: 'instance-contact',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-contact/instance-contact-delete-dialog.html',
                    controller: 'InstanceContactDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceContact', function(InstanceContact) {
                            return InstanceContact.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-contact', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
