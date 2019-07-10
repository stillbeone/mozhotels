(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InfoDialogController', InfoDialogController);

    InfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Info'];

    function InfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Info) {
        var vm = this;

        vm.info = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.info.id !== null) {
                Info.update(vm.info, onSaveSuccess, onSaveError);
            } else {
                Info.save(vm.info, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:infoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
