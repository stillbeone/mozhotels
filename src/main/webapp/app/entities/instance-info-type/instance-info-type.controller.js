(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceInfoTypeController', InstanceInfoTypeController);

    InstanceInfoTypeController.$inject = ['$scope', '$state', 'InstanceInfoType', 'InstanceInfoTypeSearch'];

    function InstanceInfoTypeController ($scope, $state, InstanceInfoType, InstanceInfoTypeSearch) {
        var vm = this;
        
        vm.instanceInfoTypes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceInfoType.query(function(result) {
                vm.instanceInfoTypes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceInfoTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceInfoTypes = result;
            });
        }    }
})();
