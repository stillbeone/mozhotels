(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceReviewDialogController', InstanceReviewDialogController);

    InstanceReviewDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceReview', 'InstanceTur', 'Tourist'];

    function InstanceReviewDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InstanceReview, InstanceTur, Tourist) {
        var vm = this;

        vm.instanceReview = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.instanceturs = InstanceTur.query();
        vm.tourists = Tourist.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.instanceReview.id !== null) {
                InstanceReview.update(vm.instanceReview, onSaveSuccess, onSaveError);
            } else {
                InstanceReview.save(vm.instanceReview, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:instanceReviewUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;
        vm.datePickerOpenStatus.editDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
