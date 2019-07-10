(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceActivityController', InstanceActivityController);

    InstanceActivityController.$inject = ['$scope', '$state', 'DataUtils', 'InstanceActivity', 'InstanceActivitySearch'];

    function InstanceActivityController ($scope, $state, DataUtils, InstanceActivity, InstanceActivitySearch) {
        var vm = this;
        
        vm.instanceActivities = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceActivity.query(function(result) {
                vm.instanceActivities = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceActivitySearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceActivities = result;
            });
        }    }
})();
