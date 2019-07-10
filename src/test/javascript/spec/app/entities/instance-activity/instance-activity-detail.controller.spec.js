'use strict';

describe('Controller Tests', function() {

    describe('InstanceActivity Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceActivity, MockPicture, MockInstanceActivityType, MockInstanceTur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceActivity = jasmine.createSpy('MockInstanceActivity');
            MockPicture = jasmine.createSpy('MockPicture');
            MockInstanceActivityType = jasmine.createSpy('MockInstanceActivityType');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceActivity': MockInstanceActivity,
                'Picture': MockPicture,
                'InstanceActivityType': MockInstanceActivityType,
                'InstanceTur': MockInstanceTur
            };
            createController = function() {
                $injector.get('$controller')("InstanceActivityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:instanceActivityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
