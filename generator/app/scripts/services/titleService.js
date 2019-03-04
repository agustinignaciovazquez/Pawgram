'use strict';

/**
 * @ngdoc service
 * @name pawgramApp.titleService
 * @description
 * # titleService
 * Service in the pawgramApp.
 */
angular.module('pawgramApp')
  .service('titleService', ['$window', function($window) {
		this.setTitle = function(title) {
			$window.document.title = title + ' - Pawgram';
		};

		this.setDefaultTitle = function() {
			$window.document.title = 'Pawgram';
		};
	}]);
