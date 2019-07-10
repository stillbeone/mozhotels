(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('PictureController', PictureController);

    PictureController.$inject = ['$scope', '$state', 'DataUtils', 'Picture', 'PictureSearch'];

    function PictureController ($scope, $state, DataUtils, Picture, PictureSearch) {
        var vm = this;
        
        vm.pictures = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Picture.query(function(result) {
                vm.pictures = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PictureSearch.query({query: vm.searchQuery}, function(result) {
                vm.pictures = result;
            });
        }    }
})();
