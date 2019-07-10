(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceActivityTypeDeleteController',InstanceActivityTypeDeleteController);

    InstanceActivityTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceActivityType'];

    function InstanceActivityTypeDeleteController($uibModalInstance, entity, InstanceActivityType) {
        var vm = this;

        vm.instanceActivityType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceActivityType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
