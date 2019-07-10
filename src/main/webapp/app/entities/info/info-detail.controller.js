(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InfoDetailController', InfoDetailController);

    InfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Info'];

    function InfoDetailController($scope, $rootScope, $stateParams, entity, Info) {
        var vm = this;

        vm.info = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:infoUpdate', function(event, result) {
            vm.info = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
