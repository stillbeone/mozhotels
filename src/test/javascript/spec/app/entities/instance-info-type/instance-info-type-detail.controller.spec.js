'use strict';

describe('Controller Tests', function() {

    describe('InstanceInfoType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceInfoType, MockInstanceInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceInfoType = jasmine.createSpy('MockInstanceInfoType');
            MockInstanceInfo = jasmine.createSpy('MockInstanceInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceInfoType': MockInstanceInfoType,
                'InstanceInfo': MockInstanceInfo
            };
            createController = function() {
                $injector.get('$controller')("InstanceInfoTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:instanceInfoTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
