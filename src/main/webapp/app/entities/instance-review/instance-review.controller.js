(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceReviewController', InstanceReviewController);

    InstanceReviewController.$inject = ['$scope', '$state', 'InstanceReview', 'InstanceReviewSearch'];

    function InstanceReviewController ($scope, $state, InstanceReview, InstanceReviewSearch) {
        var vm = this;
        
        vm.instanceReviews = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceReview.query(function(result) {
                vm.instanceReviews = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceReviewSearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceReviews = result;
            });
        }    }
})();
