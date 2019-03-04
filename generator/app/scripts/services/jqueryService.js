'use strict';

/**
 * @ngdoc service
 * @name pawgramApp.jQueryService
 * @description
 * # jQueryService
 * Factory in the pawgramApp.
 */
angular.module('pawgramApp')
  .factory('jQuery', ['$window', function ($window) {
    return $window.jQuery;
  }]);
