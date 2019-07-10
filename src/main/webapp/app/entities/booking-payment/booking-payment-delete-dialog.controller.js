(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('BookingPaymentDeleteController',BookingPaymentDeleteController);

    BookingPaymentDeleteController.$inject = ['$uibModalInstance', 'entity', 'BookingPayment'];

    function BookingPaymentDeleteController($uibModalInstance, entity, BookingPayment) {
        var vm = this;

        vm.bookingPayment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BookingPayment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
