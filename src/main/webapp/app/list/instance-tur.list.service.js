(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('InstanceTurList', InstanceTurList);

    InstanceTurList.$inject = ['$resource'];

    function InstanceTurList($resource) {
        //var resourceUrl =  'api/_list/instance-turs/:place/:rooms';
        //var resourceUrl =  'api/instance-turs';
        //var resourceUrl =  'api/_list/instance-turs/:id';
        //var resourceUrl =  'api/instance-turs';
        // var resourceUrl =  'api/instance-turs/:id';
        var service = $resource('/api/list/:id', {}, {
          'query': {
                method: 'GET',
                isArray: true,
                params: {place: null, rooms: null}
            }
        });

        return service;
    }

                // params:{
                //       place: 'place',
                //       rooms: 'rooms',
                //       adults: 'adults',
                //       childs: 'childs',
                //       checkIn: 'checkIn',
                //       checkOut: 'checkOut'
                //   }

                // controller: function($scope, $stateParams) {
                //        $scope.portfolioId = $stateParams.portfolioId;
                //        $scope.param1 = $stateParams.param1;
                //        $scope.param2 = $stateParams.param2;
                //     }
        })
();
