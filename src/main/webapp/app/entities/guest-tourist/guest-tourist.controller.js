(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('GuestTouristController', GuestTouristController);

    GuestTouristController.$inject = ['$scope', '$state', 'GuestTourist', 'GuestTouristSearch'];

    function GuestTouristController ($scope, $state, GuestTourist, GuestTouristSearch) {
        var vm = this;
        
        vm.guestTourists = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            GuestTourist.query(function(result) {
                vm.guestTourists = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            GuestTouristSearch.query({query: vm.searchQuery}, function(result) {
                vm.guestTourists = result;
            });
        }    }
})();
