'use strict';

describe('Controller Tests', function() {

    describe('InstanceInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceInfo, MockInstanceInfoType, MockInstanceTur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceInfo = jasmine.createSpy('MockInstanceInfo');
            MockInstanceInfoType = jasmine.createSpy('MockInstanceInfoType');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceInfo': MockInstanceInfo,
                'InstanceInfoType': MockInstanceInfoType,
                'InstanceTur': MockInstanceTur
            };
            createController = function() {
                $injector.get('$controller')("InstanceInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:instanceInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
