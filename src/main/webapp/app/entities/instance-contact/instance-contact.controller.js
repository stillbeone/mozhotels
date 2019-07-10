(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceContactController', InstanceContactController);

    InstanceContactController.$inject = ['$scope', '$state', 'InstanceContact', 'InstanceContactSearch'];

    function InstanceContactController ($scope, $state, InstanceContact, InstanceContactSearch) {
        var vm = this;
        
        vm.instanceContacts = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InstanceContact.query(function(result) {
                vm.instanceContacts = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstanceContactSearch.query({query: vm.searchQuery}, function(result) {
                vm.instanceContacts = result;
            });
        }    }
})();
