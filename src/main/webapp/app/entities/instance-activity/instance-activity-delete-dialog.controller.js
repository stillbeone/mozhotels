(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceActivityDeleteController',InstanceActivityDeleteController);

    InstanceActivityDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceActivity'];

    function InstanceActivityDeleteController($uibModalInstance, entity, InstanceActivity) {
        var vm = this;

        vm.instanceActivity = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceActivity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
