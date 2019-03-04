'use strict';

/**
 * @ngdoc directive
 * @name pawgramApp.directive:snackbar
 * @description
 * # snackbar
 */
angular.module('pawgramApp')
  .directive('snackbar', function () {
    return {
			restrict: 'E',
			replace: true,
			transclude: true,
			template: '<div class="snackbar" ng-transclude></div>',
			scope: {id: '@'},
			link: function(scope, element, attrs) {
				var id = attrs.id;
				
				element.find('.snackbar').attr('id', id);
			}
		};
  });
