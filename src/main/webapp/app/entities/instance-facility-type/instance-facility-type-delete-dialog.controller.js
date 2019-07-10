(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceFacilityTypeDeleteController',InstanceFacilityTypeDeleteController);

    InstanceFacilityTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceFacilityType'];

    function InstanceFacilityTypeDeleteController($uibModalInstance, entity, InstanceFacilityType) {
        var vm = this;

        vm.instanceFacilityType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceFacilityType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
