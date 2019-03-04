'use strict';
let routesInitializer = function($routeProvider){
  $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/register', {
        templateUrl: 'views/register.html',
        controller: 'RegisterCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .when('/login',{
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
      })
      .when('/404',{
        templateUrl: '404.html',
        controller: 'NotFoundCtrl',
      })
      .otherwise({
        redirectTo: '/404'
      });
    };