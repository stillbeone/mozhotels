(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceTurController', InstanceTurController);

    InstanceTurController.$inject = ['$scope', '$state', 'DataUtils', 'InstanceTur', 'InstanceTurSearch'];

    function InstanceTurController ($scope, $state, DataUtils, InstanceTur, InstanceTurSearch) {
        var vm = this;
        
        vm.instanceTurs = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceTur.query(function(result) {
                vm.instanceTurs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceTurSearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceTurs = result;
            });
        }    }
})();
