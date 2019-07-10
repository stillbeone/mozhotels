(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceActivityTypeController', InstanceActivityTypeController);

    InstanceActivityTypeController.$inject = ['$scope', '$state', 'InstanceActivityType', 'InstanceActivityTypeSearch'];

    function InstanceActivityTypeController ($scope, $state, InstanceActivityType, InstanceActivityTypeSearch) {
        var vm = this;
        
        vm.instanceActivityTypes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceActivityType.query(function(result) {
                vm.instanceActivityTypes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceActivityTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceActivityTypes = result;
            });
        }    }
})();
