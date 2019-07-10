(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('LocalTurDialogController', LocalTurDialogController);

    LocalTurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'LocalTur', 'InstanceTur', 'Picture', 'Province'];

    function LocalTurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, LocalTur, InstanceTur, Picture, Province) {
        var vm = this;

        vm.localTur = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.instanceturs = InstanceTur.query();
        vm.pictures = Picture.query();
        vm.provinces = Province.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.localTur.id !== null) {
                LocalTur.update(vm.localTur, onSaveSuccess, onSaveError);
            } else {
                LocalTur.save(vm.localTur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:localTurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPhotoPrincipal = function ($file, localTur) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        localTur.photoPrincipal = base64Data;
                        localTur.photoPrincipalContentType = $file.type;
                    });
                });
            }
        };

    }
})();
