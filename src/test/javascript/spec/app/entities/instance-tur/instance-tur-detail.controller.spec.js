'use strict';

describe('Controller Tests', function() {

    describe('InstanceTur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceTur, MockPicture, MockInstanceFacilityType, MockInstanceActivityType, MockInstanceFacility, MockInstanceActivity, MockInstanceRoomType, MockInstanceInfo, MockInstanceReview, MockBooking, MockFavorite, MockLocalTur, MockInstanceTurType, MockInstanceContact;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            MockPicture = jasmine.createSpy('MockPicture');
            MockInstanceFacilityType = jasmine.createSpy('MockInstanceFacilityType');
            MockInstanceActivityType = jasmine.createSpy('MockInstanceActivityType');
            MockInstanceFacility = jasmine.createSpy('MockInstanceFacility');
            MockInstanceActivity = jasmine.createSpy('MockInstanceActivity');
            MockInstanceRoomType = jasmine.createSpy('MockInstanceRoomType');
            MockInstanceInfo = jasmine.createSpy('MockInstanceInfo');
            MockInstanceReview = jasmine.createSpy('MockInstanceReview');
            MockBooking = jasmine.createSpy('MockBooking');
            MockFavorite = jasmine.createSpy('MockFavorite');
            MockLocalTur = jasmine.createSpy('MockLocalTur');
            MockInstanceTurType = jasmine.createSpy('MockInstanceTurType');
            MockInstanceContact = jasmine.createSpy('MockInstanceContact');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceTur': MockInstanceTur,
                'Picture': MockPicture,
                'InstanceFacilityType': MockInstanceFacilityType,
                'InstanceActivityType': MockInstanceActivityType,
                'InstanceFacility': MockInstanceFacility,
                'InstanceActivity': MockInstanceActivity,
                'InstanceRoomType': MockInstanceRoomType,
                'InstanceInfo': MockInstanceInfo,
                'InstanceReview': MockInstanceReview,
                'Booking': MockBooking,
                'Favorite': MockFavorite,
                'LocalTur': MockLocalTur,
                'InstanceTurType': MockInstanceTurType,
                'InstanceContact': MockInstanceContact
            };
            createController = function() {
                $injector.get('$controller')("InstanceTurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:instanceTurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
