(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('UserProfileController', UserProfileController);

    UserProfileController.$inject = ['$scope'];

    function UserProfileController ($scope) {
        var vm = this;

        vm.dates = {
       startDate: moment().subtract(1, "days"),
       endDate: moment()
      };

      vm.minDate =  moment().subtract(1, "days");

    }
})();
