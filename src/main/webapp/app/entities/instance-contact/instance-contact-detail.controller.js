(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceContactDetailController', InstanceContactDetailController);

    InstanceContactDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceContact', 'InstanceTur'];

    function InstanceContactDetailController($scope, $rootScope, $stateParams, entity, InstanceContact, InstanceTur) {
        var vm = this;

        vm.instanceContact = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:instanceContactUpdate', function(event, result) {
            vm.instanceContact = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
