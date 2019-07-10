(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$scope','$rootScope','$location'];

    function DashboardController ($scope, $rootScope,$location) {
        var vm = this;

        vm.backLinkClick = function () {
            // window.location.reload(false);
            window.location.reload();
        };

        $rootScope.$on('$locationChangeSuccess', function() {
        $rootScope.actualLocation = $location.path();
    });

   $rootScope.$watch(function () {return $location.path()}, function (newLocation, oldLocation) {
        if($rootScope.actualLocation === newLocation) {
            console.log("Why did you use history back?");
            window.location.reload();
        }
    });

    }
})();
