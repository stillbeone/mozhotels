(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('FavoriteDialogController', FavoriteDialogController);

    FavoriteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Favorite', 'InstanceTur', 'Tourist'];

    function FavoriteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Favorite, InstanceTur, Tourist) {
        var vm = this;

        vm.favorite = entity;
        vm.clear = clear;
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
            if (vm.favorite.id !== null) {
                Favorite.update(vm.favorite, onSaveSuccess, onSaveError);
            } else {
                Favorite.save(vm.favorite, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:favoriteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
