(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceRoomTypeDeleteController',InstanceRoomTypeDeleteController);

    InstanceRoomTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceRoomType'];

    function InstanceRoomTypeDeleteController($uibModalInstance, entity, InstanceRoomType) {
        var vm = this;

        vm.instanceRoomType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceRoomType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
