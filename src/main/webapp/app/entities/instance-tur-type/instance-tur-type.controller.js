(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceTurTypeController', InstanceTurTypeController);

    InstanceTurTypeController.$inject = ['$scope', '$state', 'InstanceTurType', 'InstanceTurTypeSearch'];

    function InstanceTurTypeController ($scope, $state, InstanceTurType, InstanceTurTypeSearch) {
        var vm = this;
        
        vm.instanceTurTypes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceTurType.query(function(result) {
                vm.instanceTurTypes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceTurTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceTurTypes = result;
            });
        }    }
})();
