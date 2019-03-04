'use strict';

/**
 * @ngdoc directive
 * @name pawgramApp.directive:loading
 * @description
 * # loading
 */
angular.module('pawgramApp')
  .directive('loading', ['$http', function($http) {
		return {
			restrict: 'A',
			link: function (scope, element, attrs) {
				scope.isLoading = function () {
					return $http.pendingRequests.length > 0;
				};

				scope.$watch(scope.isLoading, function (isLoading) {
					if(isLoading){
						element.show();
					}else{
						element.hide();
					}
				});
			}
		};
	}]);
