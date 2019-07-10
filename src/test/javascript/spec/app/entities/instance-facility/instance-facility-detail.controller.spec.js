'use strict';

describe('Controller Tests', function() {

    describe('InstanceFacility Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceFacility, MockInstanceFacilityType, MockInstanceRoomType, MockInstanceTur, MockBooking;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceFacility = jasmine.createSpy('MockInstanceFacility');
            MockInstanceFacilityType = jasmine.createSpy('MockInstanceFacilityType');
            MockInstanceRoomType = jasmine.createSpy('MockInstanceRoomType');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            MockBooking = jasmine.createSpy('MockBooking');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceFacility': MockInstanceFacility,
                'InstanceFacilityType': MockInstanceFacilityType,
                'InstanceRoomType': MockInstanceRoomType,
                'InstanceTur': MockInstanceTur,
                'Booking': MockBooking
            };
            createController = function() {
                $injector.get('$controller')("InstanceFacilityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:instanceFacilityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
