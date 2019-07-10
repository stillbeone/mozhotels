(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceInfoTypeDialogController', InstanceInfoTypeDialogController);

    InstanceInfoTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceInfoType', 'InstanceInfo'];

    function InstanceInfoTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InstanceInfoType, InstanceInfo) {
        var vm = this;

        vm.instanceInfoType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.instanceinfos = InstanceInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.instanceInfoType.id !== null) {
                InstanceInfoType.update(vm.instanceInfoType, onSaveSuccess, onSaveError);
            } else {
                InstanceInfoType.save(vm.instanceInfoType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:instanceInfoTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
