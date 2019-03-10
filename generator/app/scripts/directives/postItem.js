'use strict';

/**
 * @ngdoc directive
 * @name pawgramApp.directive:postItem
 * @description
 * # postItem
 */
 define(['pawgramApp', 'services/authService', 'services/snackbarService'], function(pawgramApp) {
    pawgramApp.directive('postItem', function () {
    return {
      restrict: 'E',
      templateUrl: 'views/postItem.html',
      scope: {product: '=', hideCategory: '=', hideDelete: '=', onVote: '&', onAdd: '&', onDelete: '&', borderHover: '=', order: '=', orderBy: '='},
      controller: ['$scope', '$location', '$route', 'authService', 'restService', 'snackbarService',
      function($scope, $location, $route, authService, restService, snackbarService, defaultSortCriteria) {

      }]
    };
  });
 });

  
