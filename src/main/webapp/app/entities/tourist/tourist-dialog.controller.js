(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('TouristDialogController', TouristDialogController);

    TouristDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Tourist', 'Booking', 'InstanceReview', 'GuestTourist', 'User', 'Favorite'];

    function TouristDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Tourist, Booking, InstanceReview, GuestTourist, User, Favorite) {
        var vm = this;

        vm.tourist = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.bookings = Booking.query();
        vm.instancereviews = InstanceReview.query();
        vm.guesttourists = GuestTourist.query();
        vm.users = User.query();
        vm.favorites = Favorite.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tourist.id !== null) {
                Tourist.update(vm.tourist, onSaveSuccess, onSaveError);
            } else {
                Tourist.save(vm.tourist, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:touristUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPhotoPrincipal = function ($file, tourist) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        tourist.photoPrincipal = base64Data;
                        tourist.photoPrincipalContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.createDate = false;
        vm.datePickerOpenStatus.editDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
