(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('PictureDetailController', PictureDetailController);

    PictureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Picture', 'Province', 'LocalTur', 'InstanceTur', 'InstanceRoomType', 'InstanceActivity'];

    function PictureDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Picture, Province, LocalTur, InstanceTur, InstanceRoomType, InstanceActivity) {
        var vm = this;

        vm.picture = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:pictureUpdate', function(event, result) {
            vm.picture = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
