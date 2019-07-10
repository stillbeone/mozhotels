(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceInfoTypeDeleteController',InstanceInfoTypeDeleteController);

    InstanceInfoTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceInfoType'];

    function InstanceInfoTypeDeleteController($uibModalInstance, entity, InstanceInfoType) {
        var vm = this;

        vm.instanceInfoType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceInfoType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
