'use strict';

describe('Controller Tests', function() {

    describe('InstanceFacilityType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceFacilityType, MockInstanceFacility, MockInstanceTur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceFacilityType = jasmine.createSpy('MockInstanceFacilityType');
            MockInstanceFacility = jasmine.createSpy('MockInstanceFacility');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceFacilityType': MockInstanceFacilityType,
                'InstanceFacility': MockInstanceFacility,
                'InstanceTur': MockInstanceTur
            };
            createController = function() {
                $injector.get('$controller')("InstanceFacilityTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:instanceFacilityTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
