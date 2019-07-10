(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceRoomTypeController', InstanceRoomTypeController);

    InstanceRoomTypeController.$inject = ['$scope', '$state', 'DataUtils', 'InstanceRoomType', 'InstanceRoomTypeSearch'];

    function InstanceRoomTypeController ($scope, $state, DataUtils, InstanceRoomType, InstanceRoomTypeSearch) {
        var vm = this;
        
        vm.instanceRoomTypes = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceRoomType.query(function(result) {
                vm.instanceRoomTypes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceRoomTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceRoomTypes = result;
            });
        }    }
})();
