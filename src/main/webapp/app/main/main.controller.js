(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('MainController', MainController);

    MainController.$inject = ['$scope', '$rootScope', '$anchorScroll', 'Principal', 'LoginService', '$state', 'LocalTur', '$timeout', 'orderByFilter', 'InstanceTur'];

    function MainController ($scope, $rootScope, $anchorScroll, Principal, LoginService, $state, LocalTur, $timeout, orderByFilter, InstanceTur) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        vm.loadAll = loadAll;
        vm.localTursData = [];

        //vm.place = "Mocambique";
        vm.rooms = 1;
        vm.adults = 1;
        vm.childs = 0;

        vm.dates = {
          startDate: moment(),
          endDate: moment().add(1, "days")
        };



      vm.minDate =  moment();

      $rootScope.$on("$locationChangeSuccess", function() {
        console.log("CHANGE VIEW");
                $anchorScroll();
    });

    $scope.scrollTo = function (id) {
      $anchorScroll(id);
    }






    //   vm.opts = {
    //     locale: {
    //         applyClass: 'btn-green',
    //         applyLabel: "Použít",
    //         fromLabel: "Od",
    //         toLabel: "Do",
    //         cancelLabel: 'Zrušit',
    //         customRangeLabel: 'Vlastní rozsah',
    //         daysOfWeek: ['Ne', 'Po', 'Út', 'St', 'Čt', 'Pá', 'So'],
    //         firstDay: 1,
    //         monthNames: ['Leden', 'Únor', 'Březen', 'Duben', 'Květen', 'Červen', 'Červenec', 'Srpen', 'Září',
    //             'Říjen', 'Listopad', 'Prosinec'
    //         ]
    //     },
    //     ranges: {
    //         'Last 7 Days': [moment().subtract(6, 'days'), moment()],
    //         'Last 30 Days': [moment().subtract(29, 'days'), moment()]
    //     }
    // };

    //     vm.datePickerCallback = datePickerCallback;
    //  vm.openDatePicker = openDatePicker;
     //
    //  vm.locale = 'en';
    //  vm.datePicker = {
    //      basic:
    //      {
    //          date: new Date(),
    //          dateFormatted: moment().locale(vm.locale).format('LL'),
    //          minDate: new Date(new Date().getFullYear(), new Date().getMonth() - 2, new Date().getDate()),
    //          maxDate: new Date(new Date().getFullYear(), new Date().getMonth() + 2, new Date().getDate())
    //      },
    //      input:
    //      {
    //          date: new Date(),
    //          dateFormatted: moment().locale(vm.locale).format('LL')
    //      }
    //  };
    //  vm.datePickerId = 'date-picker';
     //
    //  ////////////
     //
    //  function datePickerCallback(_newdate)
    //  {
    //      vm.datePicker.basic.date = _newdate;
    //      vm.datePicker.basic.dateFormatted = moment(_newdate).locale(vm.locale).format('LL');
    //  }
     //
    //  function openDatePicker(_pickerId)
    //  {
    //      LxDatePickerService.open(_pickerId);
    //  }

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

    //     var init = function () {
    //         // do something
    //         console.log("INIT");
    //         $timeout(function () {
    //   reloadPage();
    // }, 10000);
    //     }
    //
    //     init();
    //
    //     function reloadPage()
    //     {
    //       window.location.reload();
    //       //$state.reload();
    //     }

    $scope.$on('$viewContentLoaded', function() {
        //call it here
        //reloadPage();

         console.log("LOAD MAIN CONTROLLER");
        //  $timeout(function () {
        //    $state.reload();
        //  }, 10000);
         //;

    })



    //Watch for date changes
    $scope.$watch('vm.dates', function(newDate) {
        console.log('End Date: ', newDate.startDate._d);
        console.log('End Date: ', newDate.endDate._d);
        vm.checkIn = vm.dates.startDate.format('YYYY-MM-DD');
        vm.checkOut = vm.dates.endDate.format('YYYY-MM-DD');

    }, false);


    //$state.reload();
    //window.location.href = '/';

    InstanceTur.query().$promise.then(function(result) {
      vm.instanceturs = alasql('SELECT * FROM ? ORDER BY RANDOM()'
      ,[result]);
      vm.counted = vm.instanceturs.length;

      loadAutoComplete(vm.instanceturs);

      });

    function loadAutoComplete(instanceturs){
      // var instanceturscountry = instanceturs.map(function(item) {
      //         return item.localTur.province.region.country.countryName;
      // });

      // var instancetursregion = instanceturs.map(function(item) {
      //         return item.localTur.province.region.regionName+","+item.localTur.province.region.country.countryName;
      // });

      var instancetursprovince = instanceturs.map(function(item) {
              return item.localTur.province.provinceName+","+item.localTur.province.region.regionName+","+item.localTur.province.region.country.countryName;
      });

      var instanceturslocaltur = instanceturs.map(function(item) {
            return item.localTur.localTurName+","+item.localTur.province.provinceName+","+item.localTur.province.region.regionName+","+item.localTur.province.region.country.countryName;
      });

      var instancetursname = instanceturs.map(function(item) {
          return item.instanceTurName+","+item.localTur.localTurName+","+item.localTur.province.provinceName+","+item.localTur.province.region.regionName+","+item.localTur.province.region.country.countryName;
      });

       vm.instanceturautocomplete = instancetursname.concat(instanceturslocaltur.concat(instancetursprovince));
       //console.log( vm.instanceturautocomplete);
    }

    }
})();
