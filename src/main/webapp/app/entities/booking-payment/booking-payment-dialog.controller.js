(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('BookingPaymentDialogController', BookingPaymentDialogController);

    BookingPaymentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'BookingPayment', 'Booking'];

    function BookingPaymentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, BookingPayment, Booking) {
        var vm = this;

        vm.bookingPayment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.bookings = Booking.query({filter: 'bookingpayment-is-null'});
        $q.all([vm.bookingPayment.$promise, vm.bookings.$promise]).then(function() {
            if (!vm.bookingPayment.booking || !vm.bookingPayment.booking.id) {
                return $q.reject();
            }
            return Booking.get({id : vm.bookingPayment.booking.id}).$promise;
        }).then(function(booking) {
            vm.bookings.push(booking);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bookingPayment.id !== null) {
                BookingPayment.update(vm.bookingPayment, onSaveSuccess, onSaveError);
            } else {
                BookingPayment.save(vm.bookingPayment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:bookingPaymentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.cardExpiry = false;
        vm.datePickerOpenStatus.createDate = false;
        vm.datePickerOpenStatus.editDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
