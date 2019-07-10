(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('DetailController', DetailController);

    DetailController.$inject = ['$scope', '$stateParams', 'entity', 'InstanceTur', 'Picture', 'InstanceRoomType', 'InstanceActivity', 'InstanceReview', 'InstanceTurType', 'InstanceContact'];

    function DetailController ($scope, $stateParams, entity, InstanceTur, Picture, InstanceRoomType, InstanceActivity, InstanceReview, InstanceTurType, InstanceContact) {
        var vm = this;

        vm.instanceTur = entity;
        vm.pictures = Picture.query();
        vm.instanceroomtypes = InstanceRoomType.query();
        vm.activities = InstanceActivity.query();
        vm.reviews = InstanceReview.query();

        vm.instanceID = $stateParams.instanceID;
        vm.rooms = $stateParams.rooms;
        vm.adults = $stateParams.adults;
        vm.childs = $stateParams.childs;
        vm.checkIn = $stateParams.checkIn;
        vm.checkOut = $stateParams.checkOut;

        vm.load = function (id) {
          InstanceTur.get({id: id}, function(result) {
            vm.instanceTur = result;
          });
        };

        vm.dates = {
           startDate: vm.checkIn,
           endDate: vm.checkOut
        };

      vm.minDate =  moment();

      vm.tabData   = [
        {
          heading: 'Summary',
          route:   'detail.summary'
        }
      ];

      vm.update = function ()
      {
        //update params
        console.log("UPDATE - ROOM TYPES");
      }

      vm.mainImageUrl = vm.instanceTur.photoPrincipal;

      vm.setImage = function(imageUrl) {
        vm.mainImageUrl = imageUrl.picture;
      };

    }
})();
