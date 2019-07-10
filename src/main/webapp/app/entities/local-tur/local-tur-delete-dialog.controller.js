(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('LocalTurDeleteController',LocalTurDeleteController);

    LocalTurDeleteController.$inject = ['$uibModalInstance', 'entity', 'LocalTur'];

    function LocalTurDeleteController($uibModalInstance, entity, LocalTur) {
        var vm = this;

        vm.localTur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LocalTur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
