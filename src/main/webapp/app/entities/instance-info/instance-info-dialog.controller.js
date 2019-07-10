(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceInfoDialogController', InstanceInfoDialogController);

    InstanceInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceInfo', 'InstanceInfoType', 'InstanceTur'];

    function InstanceInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InstanceInfo, InstanceInfoType, InstanceTur) {
        var vm = this;

        vm.instanceInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.instanceinfotypes = InstanceInfoType.query();
        vm.instanceturs = InstanceTur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.instanceInfo.id !== null) {
                InstanceInfo.update(vm.instanceInfo, onSaveSuccess, onSaveError);
            } else {
                InstanceInfo.save(vm.instanceInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:instanceInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
