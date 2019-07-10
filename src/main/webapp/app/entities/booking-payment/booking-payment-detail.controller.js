(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('BookingPaymentDetailController', BookingPaymentDetailController);

    BookingPaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'BookingPayment', 'Booking'];

    function BookingPaymentDetailController($scope, $rootScope, $stateParams, entity, BookingPayment, Booking) {
        var vm = this;

        vm.bookingPayment = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:bookingPaymentUpdate', function(event, result) {
            vm.bookingPayment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
