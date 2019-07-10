(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('TouristDetailController', TouristDetailController);

    TouristDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Tourist', 'Booking', 'InstanceReview', 'GuestTourist', 'User', 'Favorite'];

    function TouristDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Tourist, Booking, InstanceReview, GuestTourist, User, Favorite) {
        var vm = this;

        vm.tourist = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:touristUpdate', function(event, result) {
            vm.tourist = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
