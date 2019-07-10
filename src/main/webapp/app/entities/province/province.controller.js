(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('ProvinceController', ProvinceController);

    ProvinceController.$inject = ['$scope', '$state', 'Province', 'ProvinceSearch'];

    function ProvinceController ($scope, $state, Province, ProvinceSearch) {
        var vm = this;
        
        vm.provinces = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Province.query(function(result) {
                vm.provinces = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ProvinceSearch.query({query: vm.searchQuery}, function(result) {
                vm.provinces = result;
            });
        }    }
})();
