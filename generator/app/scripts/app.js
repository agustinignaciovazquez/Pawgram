'use strict';

/**
 * @ngdoc overview
 * @name pawgramApp
 * @description
 * # pawgramApp
 *
 * Main module of the application.
 */

angular
  .module('pawgramApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'pascalprecht.translate',
    'angular-loading-bar'
  ])
  .config(['$routeProvider','$translateProvider','$qProvider','$locationProvider','cfpLoadingBarProvider', function ($routeProvider,$translateProvider,$qProvider,$locationProvider,cfpLoadingBarProvider) {
      routesInitializer($routeProvider);
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


