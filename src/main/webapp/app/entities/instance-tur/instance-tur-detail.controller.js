(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceTurDetailController', InstanceTurDetailController);

    InstanceTurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'InstanceTur', 'Picture', 'InstanceFacilityType', 'InstanceActivityType', 'InstanceFacility', 'InstanceActivity', 'InstanceRoomType', 'InstanceInfo', 'InstanceReview', 'Booking', 'Favorite', 'LocalTur', 'InstanceTurType', 'InstanceContact'];

    function InstanceTurDetailController($scope, $rootScope, $stateParams, DataUtils, entity, InstanceTur, Picture, InstanceFacilityType, InstanceActivityType, InstanceFacility, InstanceActivity, InstanceRoomType, InstanceInfo, InstanceReview, Booking, Favorite, LocalTur, InstanceTurType, InstanceContact) {
        var vm = this;

        vm.instanceTur = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:instanceTurUpdate', function(event, result) {
            vm.instanceTur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
