(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('GuestTouristDialogController', GuestTouristDialogController);

    GuestTouristDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GuestTourist', 'Booking', 'Tourist'];

    function GuestTouristDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GuestTourist, Booking, Tourist) {
        var vm = this;

        vm.guestTourist = entity;
        vm.clear = clear;
        vm.save = save;
        vm.bookings = Booking.query();
        vm.tourists = Tourist.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.guestTourist.id !== null) {
                GuestTourist.update(vm.guestTourist, onSaveSuccess, onSaveError);
            } else {
                GuestTourist.save(vm.guestTourist, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:guestTouristUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
