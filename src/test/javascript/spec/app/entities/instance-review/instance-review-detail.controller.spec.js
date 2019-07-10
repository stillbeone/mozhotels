'use strict';

describe('Controller Tests', function() {

    describe('InstanceReview Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceReview, MockInstanceTur, MockTourist;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceReview = jasmine.createSpy('MockInstanceReview');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            MockTourist = jasmine.createSpy('MockTourist');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceReview': MockInstanceReview,
                'InstanceTur': MockInstanceTur,
                'Tourist': MockTourist
            };
            createController = function() {
                $injector.get('$controller')("InstanceReviewDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:instanceReviewUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
