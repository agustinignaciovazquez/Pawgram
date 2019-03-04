'use strict';

/**
 * @ngdoc service
 * @name pawgramApp.snackbarService
 * @description
 * # snackbarService
 * Service in the pawgramApp.
 */
angular.module('pawgramApp')
  .service('snackbarService',['jQuery', function(jQuery) {
		this.showSnackbar = function(id, timeout, wait) {
			timeout = timeout || 3000;
			wait = wait || 0;
			
			var x = document.getElementById(id);
			var elem = jQuery('#' + id);
			var parent = elem.parent();
			elem.appendTo('body');
			
			// Add the "show" class
			setTimeout(function(){ x.className = 'show snackbar'; }, wait);

			// Remove the show class from DIV
			setTimeout(function() { 
				x.className = x.className.replace("show", "snackbar"); 
				elem.appendTo(parent);
			}, timeout);
		};
		
		this.showSnackbars = function(ids, timeout, wait) {
			timeout = timeout || 3000;
			wait = wait || 500;            
			
			for (var i = 0; i < ids.length; i++){
				this.showSnackbar(ids[i], timeout * (i+1) + wait * i, timeout * i + wait * i);
			}
		};

		this.showNoConnection = function(timeout, wait) {
			this.showSnackbar('noConnection', timeout, wait);
		};
	}]);
