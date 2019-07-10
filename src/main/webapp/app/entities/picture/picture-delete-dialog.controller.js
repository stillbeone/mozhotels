(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('PictureDeleteController',PictureDeleteController);

    PictureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Picture'];

    function PictureDeleteController($uibModalInstance, entity, Picture) {
        var vm = this;

        vm.picture = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Picture.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
