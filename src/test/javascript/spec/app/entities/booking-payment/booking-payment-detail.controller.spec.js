'use strict';

describe('Controller Tests', function() {

    describe('BookingPayment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBookingPayment, MockBooking;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBookingPayment = jasmine.createSpy('MockBookingPayment');
            MockBooking = jasmine.createSpy('MockBooking');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BookingPayment': MockBookingPayment,
                'Booking': MockBooking
            };
            createController = function() {
                $injector.get('$controller')("BookingPaymentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:bookingPaymentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
