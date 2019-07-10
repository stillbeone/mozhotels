(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceFacilityDetailController', InstanceFacilityDetailController);

    InstanceFacilityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceFacility', 'InstanceFacilityType', 'InstanceRoomType', 'InstanceTur', 'Booking'];

    function InstanceFacilityDetailController($scope, $rootScope, $stateParams, entity, InstanceFacility, InstanceFacilityType, InstanceRoomType, InstanceTur, Booking) {
        var vm = this;

        vm.instanceFacility = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:instanceFacilityUpdate', function(event, result) {
            vm.instanceFacility = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
