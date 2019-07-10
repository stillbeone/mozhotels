(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceTurTypeDeleteController',InstanceTurTypeDeleteController);

    InstanceTurTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceTurType'];

    function InstanceTurTypeDeleteController($uibModalInstance, entity, InstanceTurType) {
        var vm = this;

        vm.instanceTurType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceTurType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
