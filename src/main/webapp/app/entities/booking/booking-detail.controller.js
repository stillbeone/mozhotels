(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('BookingDetailController', BookingDetailController);

    BookingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Booking', 'InstanceFacility', 'Tourist', 'GuestTourist', 'InstanceTur', 'InstanceRoomType', 'BookingPayment'];

    function BookingDetailController($scope, $rootScope, $stateParams, entity, Booking, InstanceFacility, Tourist, GuestTourist, InstanceTur, InstanceRoomType, BookingPayment) {
        var vm = this;

        vm.booking = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:bookingUpdate', function(event, result) {
            vm.booking = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
