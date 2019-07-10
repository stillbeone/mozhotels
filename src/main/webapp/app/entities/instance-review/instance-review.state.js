(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-review', {
            parent: 'entity',
            url: '/instance-review',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceReview.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-review/instance-reviews.html',
                    controller: 'InstanceReviewController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceReview');
                    $translatePartialLoader.addPart('evaluation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-review-detail', {
            parent: 'entity',
            url: '/instance-review/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsbookingApp.instanceReview.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-review/instance-review-detail.html',
                    controller: 'InstanceReviewDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceReview');
                    $translatePartialLoader.addPart('evaluation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceReview', function($stateParams, InstanceReview) {
                    return InstanceReview.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('instance-review.new', {
            parent: 'instance-review',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-review/instance-review-dialog.html',
                    controller: 'InstanceReviewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cleanliness: null,
                                roomConfort: null,
                                location: null,
                                serviceStaff: null,
                                sleepQuality: null,
                                valuePrice: null,
                                evaluation: null,
                                title: null,
                                comment: null,
                                createDate: null,
                                editDate: null,
                                active: null,
                                approval: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-review', null, { reload: true });
                }, function() {
                    $state.go('instance-review');
                });
            }]
        })
        .state('instance-review.edit', {
            parent: 'instance-review',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-review/instance-review-dialog.html',
                    controller: 'InstanceReviewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceReview', function(InstanceReview) {
                            return InstanceReview.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-review', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-review.delete', {
            parent: 'instance-review',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-review/instance-review-delete-dialog.html',
                    controller: 'InstanceReviewDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceReview', function(InstanceReview) {
                            return InstanceReview.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-review', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
