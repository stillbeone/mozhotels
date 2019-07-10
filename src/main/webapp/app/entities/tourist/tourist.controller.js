(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('TouristController', TouristController);

    TouristController.$inject = ['$scope', '$state', 'DataUtils', 'Tourist', 'TouristSearch'];

    function TouristController ($scope, $state, DataUtils, Tourist, TouristSearch) {
        var vm = this;
        
        vm.tourists = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Tourist.query(function(result) {
                vm.tourists = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TouristSearch.query({query: vm.searchQuery}, function(result) {
                vm.tourists = result;
            });
        }    }
})();
