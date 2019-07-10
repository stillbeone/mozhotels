(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceFacilityTypeDetailController', InstanceFacilityTypeDetailController);

    InstanceFacilityTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceFacilityType', 'InstanceFacility', 'InstanceTur'];

    function InstanceFacilityTypeDetailController($scope, $rootScope, $stateParams, entity, InstanceFacilityType, InstanceFacility, InstanceTur) {
        var vm = this;

        vm.instanceFacilityType = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:instanceFacilityTypeUpdate', function(event, result) {
            vm.instanceFacilityType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
