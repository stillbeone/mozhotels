(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceRoomTypeDetailController', InstanceRoomTypeDetailController);

    InstanceRoomTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'InstanceRoomType', 'Picture', 'InstanceFacility', 'Booking', 'InstanceTur'];

    function InstanceRoomTypeDetailController($scope, $rootScope, $stateParams, DataUtils, entity, InstanceRoomType, Picture, InstanceFacility, Booking, InstanceTur) {
        var vm = this;

        vm.instanceRoomType = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:instanceRoomTypeUpdate', function(event, result) {
            vm.instanceRoomType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
