(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceActivityDetailController', InstanceActivityDetailController);

    InstanceActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'InstanceActivity', 'Picture', 'InstanceActivityType', 'InstanceTur'];

    function InstanceActivityDetailController($scope, $rootScope, $stateParams, DataUtils, entity, InstanceActivity, Picture, InstanceActivityType, InstanceTur) {
        var vm = this;

        vm.instanceActivity = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:instanceActivityUpdate', function(event, result) {
            vm.instanceActivity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
