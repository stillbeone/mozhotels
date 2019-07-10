(function () {
    'use strict';

    angular
        .module('mozhotelsbookingApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
