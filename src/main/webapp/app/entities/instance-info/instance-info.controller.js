(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceInfoController', InstanceInfoController);

    InstanceInfoController.$inject = ['$scope', '$state', 'InstanceInfo', 'InstanceInfoSearch'];

    function InstanceInfoController ($scope, $state, InstanceInfo, InstanceInfoSearch) {
        var vm = this;
        
        vm.instanceInfos = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceInfo.query(function(result) {
                vm.instanceInfos = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceInfoSearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceInfos = result;
            });
        }    }
})();
