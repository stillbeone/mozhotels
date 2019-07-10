'use strict';

describe('Controller Tests', function() {

    describe('InstanceActivityType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceActivityType, MockInstanceActivity, MockInstanceTur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceActivityType = jasmine.createSpy('MockInstanceActivityType');
            MockInstanceActivity = jasmine.createSpy('MockInstanceActivity');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceActivityType': MockInstanceActivityType,
                'InstanceActivity': MockInstanceActivity,
                'InstanceTur': MockInstanceTur
            };
            createController = function() {
                $injector.get('$controller')("InstanceActivityTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:instanceActivityTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
