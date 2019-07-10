'use strict';

describe('Controller Tests', function() {

    describe('Picture Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPicture, MockProvince, MockLocalTur, MockInstanceTur, MockInstanceRoomType, MockInstanceActivity;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPicture = jasmine.createSpy('MockPicture');
            MockProvince = jasmine.createSpy('MockProvince');
            MockLocalTur = jasmine.createSpy('MockLocalTur');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            MockInstanceRoomType = jasmine.createSpy('MockInstanceRoomType');
            MockInstanceActivity = jasmine.createSpy('MockInstanceActivity');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Picture': MockPicture,
                'Province': MockProvince,
                'LocalTur': MockLocalTur,
                'InstanceTur': MockInstanceTur,
                'InstanceRoomType': MockInstanceRoomType,
                'InstanceActivity': MockInstanceActivity
            };
            createController = function() {
                $injector.get('$controller')("PictureDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:pictureUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
