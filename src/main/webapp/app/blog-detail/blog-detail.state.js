(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('blog-detail', {
            parent: 'app-frontOffice',
            url: '/blog-detail',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/blog-detail/blog-detail.html',
                    controller: 'BlogDetailController',
                    controllerAs: 'vm'
                }
            },
            // resolve: {
            //     mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
            //         $translatePartialLoader.addPart('blog-detail');
            //         return $translate.refresh();
            //     }]
            // }
        })
        .state('blog-detail.newtopic', {
          url:         '#/blog-detail/#newtopic',
          templateUrl: 'app/blog-detail/blog.html'
        })
        .state('blog-detail.comments', {
          url:         '#/blog-detail/#comments',
          templateUrl: 'app/blog-detail/blog.html'
        });
    }
})();
