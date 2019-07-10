(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InfoDeleteController',InfoDeleteController);

    InfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Info'];

    function InfoDeleteController($uibModalInstance, entity, Info) {
        var vm = this;

        vm.info = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Info.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
