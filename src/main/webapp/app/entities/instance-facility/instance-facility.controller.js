(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceFacilityController', InstanceFacilityController);

    InstanceFacilityController.$inject = ['$scope', '$state', 'InstanceFacility', 'InstanceFacilitySearch'];

    function InstanceFacilityController ($scope, $state, InstanceFacility, InstanceFacilitySearch) {
        var vm = this;
        
        vm.instanceFacilities = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceFacility.query(function(result) {
                vm.instanceFacilities = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceFacilitySearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceFacilities = result;
            });
        }    }
})();
