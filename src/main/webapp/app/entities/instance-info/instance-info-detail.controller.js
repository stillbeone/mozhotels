(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceInfoDetailController', InstanceInfoDetailController);

    InstanceInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceInfo', 'InstanceInfoType', 'InstanceTur'];

    function InstanceInfoDetailController($scope, $rootScope, $stateParams, entity, InstanceInfo, InstanceInfoType, InstanceTur) {
        var vm = this;

        vm.instanceInfo = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:instanceInfoUpdate', function(event, result) {
            vm.instanceInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
