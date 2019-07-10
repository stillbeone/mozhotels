(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('LocalTurController', LocalTurController);

    LocalTurController.$inject = ['$scope', '$state', 'DataUtils', 'LocalTur', 'LocalTurSearch'];

    function LocalTurController ($scope, $state, DataUtils, LocalTur, LocalTurSearch) {
        var vm = this;
        
        vm.localTurs = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            LocalTur.query(function(result) {
                vm.localTurs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LocalTurSearch.query({query: vm.searchQuery}, function(result) {
                vm.localTurs = result;
            });
        }    }
})();
