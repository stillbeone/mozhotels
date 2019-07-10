(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceInfoDeleteController',InstanceInfoDeleteController);

    InstanceInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceInfo'];

    function InstanceInfoDeleteController($uibModalInstance, entity, InstanceInfo) {
        var vm = this;

        vm.instanceInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
