(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceReviewDeleteController',InstanceReviewDeleteController);

    InstanceReviewDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceReview'];

    function InstanceReviewDeleteController($uibModalInstance, entity, InstanceReview) {
        var vm = this;

        vm.instanceReview = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstanceReview.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
