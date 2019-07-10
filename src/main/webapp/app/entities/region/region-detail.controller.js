(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('RegionDetailController', RegionDetailController);

    RegionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Region', 'Province', 'Country'];

    function RegionDetailController($scope, $rootScope, $stateParams, entity, Region, Province, Country) {
        var vm = this;

        vm.region = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:regionUpdate', function(event, result) {
            vm.region = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
