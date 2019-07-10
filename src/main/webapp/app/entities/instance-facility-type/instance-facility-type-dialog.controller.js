(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceFacilityTypeDialogController', InstanceFacilityTypeDialogController);

    InstanceFacilityTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceFacilityType', 'InstanceFacility', 'InstanceTur'];

    function InstanceFacilityTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InstanceFacilityType, InstanceFacility, InstanceTur) {
        var vm = this;

        vm.instanceFacilityType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.instancefacilities = InstanceFacility.query();
        vm.instanceturs = InstanceTur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.instanceFacilityType.id !== null) {
                InstanceFacilityType.update(vm.instanceFacilityType, onSaveSuccess, onSaveError);
            } else {
                InstanceFacilityType.save(vm.instanceFacilityType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:instanceFacilityTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
