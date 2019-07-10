(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('BookingPaymentController', BookingPaymentController);

    BookingPaymentController.$inject = ['$scope', '$state', 'BookingPayment', 'BookingPaymentSearch'];

    function BookingPaymentController ($scope, $state, BookingPayment, BookingPaymentSearch) {
        var vm = this;
        
        vm.bookingPayments = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            BookingPayment.query(function(result) {
                vm.bookingPayments = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BookingPaymentSearch.query({query: vm.searchQuery}, function(result) {
                vm.bookingPayments = result;
            });
        }    }
})();
