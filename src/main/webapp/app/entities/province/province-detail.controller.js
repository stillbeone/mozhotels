(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('ProvinceDetailController', ProvinceDetailController);

    ProvinceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Province', 'LocalTur', 'Picture', 'Region'];

    function ProvinceDetailController($scope, $rootScope, $stateParams, entity, Province, LocalTur, Picture, Region) {
        var vm = this;

        vm.province = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:provinceUpdate', function(event, result) {
            vm.province = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
