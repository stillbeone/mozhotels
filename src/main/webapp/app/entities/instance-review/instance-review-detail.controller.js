(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .controller('InstanceReviewDetailController', InstanceReviewDetailController);

    InstanceReviewDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceReview', 'InstanceTur', 'Tourist'];

    function InstanceReviewDetailController($scope, $rootScope, $stateParams, entity, InstanceReview, InstanceTur, Tourist) {
        var vm = this;

        vm.instanceReview = entity;

        var unsubscribe = $rootScope.$on('mozhotelsbookingApp:instanceReviewUpdate', function(event, result) {
            vm.instanceReview = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
