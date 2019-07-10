(function() {
    'use strict';
    angular
        .module('mozhotelsbookingApp')
        .factory('Booking', Booking);

    Booking.$inject = ['$resource', 'DateUtils'];

    function Booking ($resource, DateUtils) {
        var resourceUrl =  'api/bookings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.checkIn = DateUtils.convertLocalDateFromServer(data.checkIn);
                        data.checkOut = DateUtils.convertLocalDateFromServer(data.checkOut);
                        data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                        data.editDate = DateUtils.convertDateTimeFromServer(data.editDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.checkIn = DateUtils.convertLocalDateToServer(data.checkIn);
                    data.checkOut = DateUtils.convertLocalDateToServer(data.checkOut);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.checkIn = DateUtils.convertLocalDateToServer(data.checkIn);
                    data.checkOut = DateUtils.convertLocalDateToServer(data.checkOut);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
