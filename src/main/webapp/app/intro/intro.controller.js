(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('IntroController', IntroController);

    IntroController.$inject = ['$scope','LocalTur','$timeout', '$compile', '$rootScope'];

    function IntroController ($scope, LocalTur, $timeout, $compile, $rootScope) {
        var vm = this;
        vm.localtursData = LocalTur.query();
        vm.localturs = [];

        vm.getIds = LocalTur.query().$promise.then(function(result) {
          angular.forEach(result, function(eachService) {

            //console.log(eachService);
            //vm.localturs.push(eachService);
          });
          vm.localturs =alasql('SELECT * FROM ? ORDER BY RANDOM()',[vm.localturs]);
          console.log(vm.sqlID);
        });
        //console.log(vm.localturs);
        //vm.getIds();
        //console.log(vm.getIds());

          // vm.data = [
          //     [2014, 1, 1], [2015, 2, 1],
          //     [2016, 3, 1], [2017, 4, 2],
          //     [2018, 5, 3], [2019, 6, 3]
          // ];








    }


})();
