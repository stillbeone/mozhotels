(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('FavoriteDetailController', FavoriteDetailController);

    FavoriteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Favorite', 'InstanceTur', 'Tourist'];

    function FavoriteDetailController($scope, $rootScope, $stateParams, entity, Favorite, InstanceTur, Tourist) {
        var vm = this;

        vm.favorite = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:favoriteUpdate', function(event, result) {
            vm.favorite = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
