'use strict';

/**
 * @ngdoc overview
 * @name pawgramApp
 * @description
 * # pawgramApp
 *
 * Main module of the application.
 */
define(['configs',
  'angular',
  'angular-route',
  'angular-bootstrap',
  'angular-sanitize',
  'bootstrap',
  'angular-translate',
  'angular-translate-loader-static-files',
  'angular-loading-bar',
  'angular-animate',
  'angular-cookies',
  'angular-resource',
  'angular-touch'],
  function(configs){
      var pawgramApp = angular.module('pawgramApp', [
            'ngAnimate',
            'ngCookies',
            'ngResource',
            'ngRoute',
            'ui.bootstrap',
            'ngSanitize',
            'ngTouch',
            'pascalprecht.translate',
            'angular-loading-bar'
          ]);

      pawgramApp.config(['$routeProvider',
        '$controllerProvider',
        '$compileProvider',
        '$filterProvider',
        '$provide',
        '$qProvider',
        '$translateProvider',
        '$locationProvider',
        'cfpLoadingBarProvider', function ($routeProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $qProvider, $translateProvider, $locationProvider,cfpLoadingBarProvider) {
          
          pawgramApp.controller = $controllerProvider.register;
          pawgramApp.directive = $compileProvider.directive;
          pawgramApp.filter = $filterProvider.register;
          pawgramApp.factory = $provide.factory;
          pawgramApp.service = $provide.service;

          
          configs.route($routeProvider);
          configs.translate($translateProvider);
          configs.essential($qProvider, $locationProvider, cfpLoadingBarProvider);
      
      }])

      .run(['$rootScope', '$location', 'authService', function($rootScope, $location,authService) {
         configs.run($rootScope, $location,authService);
       }])
      .value('url', 'http://localhost:8080/api')
      .value('searchMinLength', 3)
      .value('searchMaxLength', 64)
      .value('pageSize', 20)
      .value('categories', ['adopt', 'lost', 'found', 'emergency'])
      .value('categoriesImage', {adopt: 'images/adopt.svg', lost: 'images/lost.svg', found: 'images/found.svg', emergency: 'images/emergency.svg'})
      .value('sortCriterias', [{orderBy: 'date', order: 'desc'}, {orderBy: 'id', order: 'asc'}, {orderBy: 'alpha', order: 'asc'}])
      .value('defaultSortCriteria', {orderBy: 'distance', order: 'asc'})
      .filter('urlencode', function() {
            return function(input) {
              return window.encodeURIComponent(input);
            };
      });
      return pawgramApp;
  });



