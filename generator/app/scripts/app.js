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
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .otherwise({
        redirectTo: '/'
      });
  })
  .value('url', 'http://pawserver.it.itba.edu.ar/paw-2018b-11/api');
