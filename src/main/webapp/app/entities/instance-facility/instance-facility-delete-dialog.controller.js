(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceFacilityDeleteController',InstanceFacilityDeleteController);

    InstanceFacilityDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceFacility'];

    function InstanceFacilityDeleteController($uibModalInstance, entity, InstanceFacility) {
        var vm = this;

        vm.instanceFacility = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceFacility.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
