(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceRoomTypeDialogController', InstanceRoomTypeDialogController);

    InstanceRoomTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'InstanceRoomType', 'Picture', 'InstanceFacility', 'Booking', 'InstanceTur'];

    function InstanceRoomTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, InstanceRoomType, Picture, InstanceFacility, Booking, InstanceTur) {
        var vm = this;

        vm.instanceRoomType = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.pictures = Picture.query();
        vm.instancefacilities = InstanceFacility.query();
        vm.bookings = Booking.query();
        vm.instanceturs = InstanceTur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.instanceRoomType.id !== null) {
                InstanceRoomType.update(vm.instanceRoomType, onSaveSuccess, onSaveError);
            } else {
                InstanceRoomType.save(vm.instanceRoomType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:instanceRoomTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPhotoPrincipal = function ($file, instanceRoomType) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        instanceRoomType.photoPrincipal = base64Data;
                        instanceRoomType.photoPrincipalContentType = $file.type;
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
