(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceTurTypeDialogController', InstanceTurTypeDialogController);

    InstanceTurTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceTurType', 'InstanceTur'];

    function InstanceTurTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InstanceTurType, InstanceTur) {
        var vm = this;

        vm.instanceTurType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.instanceturs = InstanceTur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.instanceTurType.id !== null) {
                InstanceTurType.update(vm.instanceTurType, onSaveSuccess, onSaveError);
            } else {
                InstanceTurType.save(vm.instanceTurType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:instanceTurTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
