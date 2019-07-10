(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('BookingDialogController', BookingDialogController);

    BookingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Booking', 'InstanceFacility', 'Tourist', 'GuestTourist', 'InstanceTur', 'InstanceRoomType', 'BookingPayment'];

    function BookingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Booking, InstanceFacility, Tourist, GuestTourist, InstanceTur, InstanceRoomType, BookingPayment) {
        var vm = this;

        vm.booking = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.instancefacilities = InstanceFacility.query();
        vm.tourists = Tourist.query();
        vm.guesttourists = GuestTourist.query();
        vm.instanceturs = InstanceTur.query();
        vm.instanceroomtypes = InstanceRoomType.query();
        vm.bookingpayments = BookingPayment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.booking.id !== null) {
                Booking.update(vm.booking, onSaveSuccess, onSaveError);
            } else {
                Booking.save(vm.booking, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:bookingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.checkIn = false;
        vm.datePickerOpenStatus.checkOut = false;
        vm.datePickerOpenStatus.createDate = false;
        vm.datePickerOpenStatus.editDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
