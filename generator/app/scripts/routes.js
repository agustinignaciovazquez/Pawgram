'use strict';
define(function() {
  return {
      defaultRoutePath: '/404',
      routes: {
        '/': {
          templateUrl: 'views/main.html',
          controller: 'MainCtrl',
          controllerAs: 'main'
        },
        '/register': {
          templateUrl: 'views/register.html',
          controller: 'RegisterCtrl'
        },
        '/about': {
          templateUrl: 'views/about.html',
          controller: 'AboutCtrl',
          controllerAs: 'about'
        },
        '/login':{
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
        },
        '/404':{
        templateUrl: '404.html',
        controller: 'NotFoundCtrl',
        }
      /* ===== yeoman hook ===== */
      /* Do not remove these commented lines! Needed for auto-generation */
      }
    }
  });