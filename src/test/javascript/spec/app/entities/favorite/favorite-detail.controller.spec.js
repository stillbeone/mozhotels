'use strict';

describe('Controller Tests', function() {

    describe('Favorite Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFavorite, MockInstanceTur, MockTourist;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFavorite = jasmine.createSpy('MockFavorite');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            MockTourist = jasmine.createSpy('MockTourist');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Favorite': MockFavorite,
                'InstanceTur': MockInstanceTur,
                'Tourist': MockTourist
            };
            createController = function() {
                $injector.get('$controller')("FavoriteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsbookingApp:favoriteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
