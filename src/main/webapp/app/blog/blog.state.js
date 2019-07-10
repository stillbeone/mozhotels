(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('blog', {
            parent: 'app-frontOffice',
            url: '/blog',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/blog/blog.html',
                    controller: 'BlogController',
                    controllerAs: 'vm'
                }
            },
            // resolve: {
            //     mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
            //         $translatePartialLoader.addPart('blog');
            //         return $translate.refresh();
            //     }]
            // }
        })
        .state('blog.newtopic', {
          url:         '#/blog/#newtopic',
          templateUrl: 'app/blog/blog.html'
        })
        .state('blog.comments', {
          url:         '#/blog/#comments',
          templateUrl: 'app/blog/blog.html'
        });
    }
})();
