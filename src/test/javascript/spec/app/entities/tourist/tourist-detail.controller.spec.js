'use strict';

describe('Controller Tests', function() {

    describe('Tourist Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTourist, MockBooking, MockInstanceReview, MockGuestTourist, MockUser, MockFavorite;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTourist = jasmine.createSpy('MockTourist');
            MockBooking = jasmine.createSpy('MockBooking');
            MockInstanceReview = jasmine.createSpy('MockInstanceReview');
            MockGuestTourist = jasmine.createSpy('MockGuestTourist');
            MockUser = jasmine.createSpy('MockUser');
            MockFavorite = jasmine.createSpy('MockFavorite');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Tourist': MockTourist,
                'Booking': MockBooking,
                'InstanceReview': MockInstanceReview,
                'GuestTourist': MockGuestTourist,
                'User': MockUser,
                'Favorite': MockFavorite
            };
            createController = function() {
                $injector.get('$controller')("TouristDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:touristUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
