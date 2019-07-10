(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('LocalTurDetailController', LocalTurDetailController);

    LocalTurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'LocalTur', 'InstanceTur', 'Picture', 'Province'];

    function LocalTurDetailController($scope, $rootScope, $stateParams, DataUtils, entity, LocalTur, InstanceTur, Picture, Province) {
        var vm = this;

        vm.localTur = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:localTurUpdate', function(event, result) {
            vm.localTur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
