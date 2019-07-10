(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceTurDeleteController',InstanceTurDeleteController);

    InstanceTurDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceTur'];

    function InstanceTurDeleteController($uibModalInstance, entity, InstanceTur) {
        var vm = this;

        vm.instanceTur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceTur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
