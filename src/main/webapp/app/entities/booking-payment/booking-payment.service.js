(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('BookingPayment', BookingPayment);

    BookingPayment.$inject = ['$resource', 'DateUtils'];

    function BookingPayment ($resource, DateUtils) {
        var resourceUrl =  'api/booking-payments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.cardExpiry = DateUtils.convertLocalDateFromServer(data.cardExpiry);
                        data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                        data.editDate = DateUtils.convertDateTimeFromServer(data.editDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.cardExpiry = DateUtils.convertLocalDateToServer(data.cardExpiry);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.cardExpiry = DateUtils.convertLocalDateToServer(data.cardExpiry);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
