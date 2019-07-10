(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('PaymentController', PaymentController);

    PaymentController.$inject = ['$scope'];

    function PaymentController ($scope, Principal) {
        var vm = this;

    }
})();
