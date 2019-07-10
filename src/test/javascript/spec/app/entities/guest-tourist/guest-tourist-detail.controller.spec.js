'use strict';

describe('Controller Tests', function() {

    describe('GuestTourist Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockGuestTourist, MockBooking, MockTourist;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockGuestTourist = jasmine.createSpy('MockGuestTourist');
            MockBooking = jasmine.createSpy('MockBooking');
            MockTourist = jasmine.createSpy('MockTourist');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'GuestTourist': MockGuestTourist,
                'Booking': MockBooking,
                'Tourist': MockTourist
            };
            createController = function() {
                $injector.get('$controller')("GuestTouristDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:guestTouristUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
