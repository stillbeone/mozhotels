(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('FavoriteController', FavoriteController);

    FavoriteController.$inject = ['$scope', '$state', 'Favorite', 'FavoriteSearch'];

    function FavoriteController ($scope, $state, Favorite, FavoriteSearch) {
        var vm = this;
        
        vm.favorites = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Favorite.query(function(result) {
                vm.favorites = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FavoriteSearch.query({query: vm.searchQuery}, function(result) {
                vm.favorites = result;
            });
        }    }
})();
