(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('TouristDeleteController',TouristDeleteController);

    TouristDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tourist'];

    function TouristDeleteController($uibModalInstance, entity, Tourist) {
        var vm = this;

        vm.tourist = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tourist.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
