'use strict';

describe('Controller Tests', function() {

    describe('Province Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProvince, MockLocalTur, MockPicture, MockRegion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProvince = jasmine.createSpy('MockProvince');
            MockLocalTur = jasmine.createSpy('MockLocalTur');
            MockPicture = jasmine.createSpy('MockPicture');
            MockRegion = jasmine.createSpy('MockRegion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Province': MockProvince,
                'LocalTur': MockLocalTur,
                'Picture': MockPicture,
                'Region': MockRegion
            };
            createController = function() {
                $injector.get('$controller')("ProvinceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:provinceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
