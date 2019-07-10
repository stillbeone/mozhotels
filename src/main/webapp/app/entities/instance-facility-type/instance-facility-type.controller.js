(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceFacilityTypeController', InstanceFacilityTypeController);

    InstanceFacilityTypeController.$inject = ['$scope', '$state', 'InstanceFacilityType', 'InstanceFacilityTypeSearch'];

    function InstanceFacilityTypeController ($scope, $state, InstanceFacilityType, InstanceFacilityTypeSearch) {
        var vm = this;
        
        vm.instanceFacilityTypes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceFacilityType.query(function(result) {
                vm.instanceFacilityTypes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceFacilityTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceFacilityTypes = result;
            });
        }    }
})();
