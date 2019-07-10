(function() {
    'use strict';

    angular
        .module('mozhotelsbookingApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'daterangepicker',
            'checklist-model',
            'autocomplete',
            'rzModule'
            // 'ngMessages'
            // ,'slickCarousel'
            // ,'ngMaterial'
            // 'datePicker'
            // 'lumx'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
