(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('GuestTouristDeleteController',GuestTouristDeleteController);

    GuestTouristDeleteController.$inject = ['$uibModalInstance', 'entity', 'GuestTourist'];

    function GuestTouristDeleteController($uibModalInstance, entity, GuestTourist) {
        var vm = this;

        vm.guestTourist = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GuestTourist.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
