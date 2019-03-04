'use strict';

/**
 * @ngdoc directive
 * @name pawgramApp.directive:validFile
 * @description
 * # validFile
 */
angular.module('pawgramApp')
  .directive('validFile', function(){
		return {
			require: 'ngModel',
			link: function(scope, el, attrs, ngModel) {
				//change event is fired when file is selected
				el.bind('change',function() {
					scope.$apply(function() {
						ngModel.$setViewValue(el.val());
						ngModel.$render();
					});
				});
			}
		};
	});
