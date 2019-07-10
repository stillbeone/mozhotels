(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceContactDeleteController',InstanceContactDeleteController);

    InstanceContactDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceContact'];

    function InstanceContactDeleteController($uibModalInstance, entity, InstanceContact) {
        var vm = this;

        vm.instanceContact = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceContact.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
