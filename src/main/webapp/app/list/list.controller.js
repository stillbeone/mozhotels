(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('ListController', ListController);

    ListController.$inject = ['$scope','$http', '$stateParams', 'InstanceTur', 'Picture','InstanceRoomType', 'InstanceFacility','InstanceFacilityType', 'InstanceTurType', 'InstanceTurList', 'InstanceTurSearch'];

    function ListController ($scope, $http, $stateParams, InstanceTur, Picture,InstanceRoomType, InstanceFacility, InstanceFacilityType, InstanceTurType, InstanceTurList, InstanceTurSearch) {
        var vm = this;

        vm.instanceturs = [];
        vm.pictures = Picture.query();
        vm.instanceroomtypes = [];
        vm.instancefacilitytypes = [];
        vm.instanceturtypes = [];
        vm.instancefacilitys = [];
        vm.priceMin = "";
        vm.princeMax = "";
        vm.filterItems = {};
        vm.filterFacility = {};
        vm.counted = "";
        vm.instanceturautocomplete =[];

        vm.place = $stateParams.place;
        vm.rooms = $stateParams.rooms;
        vm.adults = $stateParams.adults;
        vm.childs = $stateParams.childs;
        vm.checkIn = $stateParams.checkIn;
        vm.checkOut = $stateParams.checkOut;

        $scope.$watch('vm.test.single', function (newVal, oldVal) {
            console.log('Value has changed', newVal, oldVal);
        });


        loadAll();

        function loadAll() {
          console.log("THIS ROUTE PARAMS PLACE: "+$stateParams.place);
          console.log("THIS ROUTE PARAMS ROOMS: "+$stateParams.rooms);
          console.log("THIS ROUTE PARAMS ADULTS: "+$stateParams.adults);
          console.log("THIS ROUTE PARAMS CHILDS: "+$stateParams.childs);
          console.log("THIS ROUTE PARAMS CHECKIN: "+$stateParams.checkIn);
          console.log("THIS ROUTE PARAMS CHECKOUT: "+$stateParams.checkOut);

          InstanceRoomType.query().$promise.then(function(result) {
            // angular.forEach(result, function(eachService) {
            //
            //   //console.log(eachService);
            //   //vm.localTursData.push(eachService);
            // });
            vm.avgPrice = function(instanceTurId) {
              return alasql('SELECT VALUE AVG(onlinePrice), instanceTur->id as instanceTur FROM ? where (instanceTur->id)= ?'
              ,[result,instanceTurId]);
            };

            vm.priceMin =  alasql('SELECT VALUE MIN(onlinePrice) FROM ?',[result]);

            vm.priceMax =  alasql('SELECT VALUE MAX(onlinePrice) FROM ?',[result]);

          });

          InstanceTurType.query().$promise.then(function(result) {
            vm.instanceturtypes = alasql('SELECT * FROM ?  ORDER BY instanceFacilityTypeName'
            ,[result]);
             //console.log(vm.instanceturtypes);
            });

          InstanceFacilityType.query().$promise.then(function(result) {
            vm.instancefacilitytypes = alasql('SELECT * FROM ? ORDER BY instanceFacilityTypeName'
            ,[result]);
             console.log(vm.instancefacilitytypes);
            });

            InstanceFacility.query().$promise.then(function(result) {
              vm.instancefacilitys = alasql('SELECT * FROM ? ORDER BY instanceFacilityName'
              ,[result]);
               console.log(vm.instancefacilitys);
              });

              InstanceTur.query().$promise.then(function(result) {
                vm.instanceturs = alasql('SELECT * FROM ? ORDER BY RANDOM()'
                ,[result]);
                vm.counted = vm.instanceturs.length;

                loadAutoComplete(vm.instanceturs);

                });



        }

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

        vm.hotelFilter = function ()
        {

          if (vm.place)
          {
             vm.place = vm.place.split(",");
             vm.place = vm.place[0];
          }

          else
            vm.place="Mocambique";

          vm.rooms = vm.rooms;
          vm.adults = vm.adults;
          vm.childs = vm.childs;

          vm.checkIn = vm.dates.startDate.format('YYYY-MM-DD');
          vm.checkOut = vm.dates.endDate.format('YYYY-MM-DD');

          // $scope.place=place;
          // $scope.rooms=rooms;
          // $scope.adults=adults;
          // $scope.childs=childs;
          // $scope.checkIn=checkIn;
          // $scope.checkOut=checkOut;

          InstanceTurList.query({place: vm.place, rooms: vm.rooms, adults: vm.adults,
            checkIn: vm.checkIn, checkOut: vm.checkOut},
            function(result, headers){
                console.log(result);
          });

          // InstanceTur.query()
          // .$promise.then(function(result) {
          //
          //         console.log(result);
          //
          //   });
          // console.log(checkOut);
          // InstanceTur.query().$promise.then(function(result) {
          //   vm.instanceturs = alasql('SELECT * FROM ? ORDER BY RANDOM()'
          //   ,[result,vm.instancefacilitys]);
          //   vm.counted = vm.instanceturs.length;
          //   //console.log(vm.instanceturs);
          //   });
        }



        function noFilter(filterObj) {
          return Object.
            keys(filterObj).
            every(function (key) { return !filterObj[key]; });
        }

          //filter
          vm.testFilter = function (instance) {
            return vm.filterItems[instance.rating] || noFilter(vm.filterItems);
            //console.log(result);
          };


          function checkSuperset (superset, subset) {
            return subset.every(function (value) {
              return (superset.indexOf(value) >= 0);
            });
          }

          var filteredFacility = [];
          // Functions - Definitions
          vm.filterByFacility = function (instance) {

                var names = instance.instanceFacilityTypes.map(function(item) {
                    return item.id;
                });

                var result = checkSuperset(names,filteredFacility);

                //console.log(result);
                return result || noFilter(vm.filterFacility);

          };

          vm.filterCheck = function(value){

          //console.log(Object.keys(value).length === 0);
            var keys = Object.keys(vm.filterFacility);
            var filtered = keys.filter(function(key) {
                return vm.filterFacility[key];
            });

            filteredFacility= filtered.map(function (x) {
                return parseInt(x, 10);
            });
            //
             //console.log(filteredFacility);

        }

        vm.filterItem= function(value){
          //console.log(vm.filterItems);
        }

        $scope.$on('$viewContentLoaded', function() {
            //call it here
            //reloadPage();
             //console.log("LOAD LIST CONTROLLER");
             //$state.reload();
        })

        vm.dates = {
       startDate: $stateParams.checkIn,
       endDate: $stateParams.checkOut
      };

      vm.minDate =  moment();

      vm.slider = {
      options: {
          floor: 0,
          ceil: 1000,
          step: 10
        }
      };

      $scope.$on("slideEnded", function() {
           // user finished sliding a handle
           console.log("Min: "+vm.priceMin);
           console.log("Max: "+vm.priceMax);


      });


    }
})();
