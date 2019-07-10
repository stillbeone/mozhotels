(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceTurTypeDetailController', InstanceTurTypeDetailController);

    InstanceTurTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceTurType', 'InstanceTur'];

    function InstanceTurTypeDetailController($scope, $rootScope, $stateParams, entity, InstanceTurType, InstanceTur) {
        var vm = this;

        vm.instanceTurType = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:instanceTurTypeUpdate', function(event, result) {
            vm.instanceTurType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
