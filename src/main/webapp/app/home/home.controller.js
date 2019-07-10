(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'LocalTur', '$timeout', 'orderByFilter'];

    function HomeController ($scope, Principal, LoginService, $state, LocalTur, $timeout, orderByFilter) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        vm.loadAll = loadAll;
        vm.localTursData = [];

        loadAll();

        function loadAll() {
          LocalTur.query().$promise.then(function(result) {
            // angular.forEach(result, function(eachService) {
            //
            //   //console.log(eachService);
            //   //vm.localTursData.push(eachService);
            // });
            vm.localTurs = alasql('SELECT * FROM ? ORDER BY RANDOM() limit 5',[result]);
            //console.log(vm.sqlID);
          });
        }

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        // vm.datePickerOpenStatus.checkIn = false;
        // vm.datePickerOpenStatus.checkOut = false;
        //
        // function openCalendar (date) {
        //     vm.datePickerOpenStatus[date] = true;
        // }
    }
})();
