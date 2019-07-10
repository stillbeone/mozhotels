(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceInfoTypeDetailController', InstanceInfoTypeDetailController);

    InstanceInfoTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceInfoType', 'InstanceInfo'];

    function InstanceInfoTypeDetailController($scope, $rootScope, $stateParams, entity, InstanceInfoType, InstanceInfo) {
        var vm = this;

        vm.instanceInfoType = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:instanceInfoTypeUpdate', function(event, result) {
            vm.instanceInfoType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
