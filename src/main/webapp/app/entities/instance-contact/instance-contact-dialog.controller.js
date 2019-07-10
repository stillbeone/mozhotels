(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceContactDialogController', InstanceContactDialogController);

    InstanceContactDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'InstanceContact', 'InstanceTur'];

    function InstanceContactDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, InstanceContact, InstanceTur) {
        var vm = this;

        vm.instanceContact = entity;
        vm.clear = clear;
        vm.save = save;
        vm.instanceturs = InstanceTur.query({filter: 'instancecontact-is-null'});
        $q.all([vm.instanceContact.$promise, vm.instanceturs.$promise]).then(function() {
            if (!vm.instanceContact.instanceTur || !vm.instanceContact.instanceTur.id) {
                return $q.reject();
            }
            return InstanceTur.get({id : vm.instanceContact.instanceTur.id}).$promise;
        }).then(function(instanceTur) {
            vm.instanceturs.push(instanceTur);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.instanceContact.id !== null) {
                InstanceContact.update(vm.instanceContact, onSaveSuccess, onSaveError);
            } else {
                InstanceContact.save(vm.instanceContact, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mozhotelsbookingApp:instanceContactUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
