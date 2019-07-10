(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InfoController', InfoController);

    InfoController.$inject = ['$scope', '$state', 'Info', 'InfoSearch'];

    function InfoController ($scope, $state, Info, InfoSearch) {
        var vm = this;
        
        vm.infos = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Info.query(function(result) {
                vm.infos = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InfoSearch.query({query: vm.searchQuery}, function(result) {
                vm.infos = result;
            });
        }    }
})();
