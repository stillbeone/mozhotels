(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('FavoriteDeleteController',FavoriteDeleteController);

    FavoriteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Favorite'];

    function FavoriteDeleteController($uibModalInstance, entity, Favorite) {
        var vm = this;

        vm.favorite = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Favorite.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
