(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('GuestTouristDetailController', GuestTouristDetailController);

    GuestTouristDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'GuestTourist', 'Booking', 'Tourist'];

    function GuestTouristDetailController($scope, $rootScope, $stateParams, entity, GuestTourist, Booking, Tourist) {
        var vm = this;

        vm.guestTourist = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:guestTouristUpdate', function(event, result) {
            vm.guestTourist = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
