'use strict';

/**
 * @ngdoc directive
 * @name pawgramApp.directive:ngFileRead
 * @description
 * # ngFileRead
 */
define(['pawgramApp'], function(pawgramApp) {
	pawgramApp.directive('ngFileRead', function() {
		return {
			scope: {
				ngFileRead: '='
			},
			link: function(scope, element, attrs) {
				element.bind("click", function (click) {
					// Sin esto no se triggerea el fileRead cuando 
					// se selecciona la misma imagen y anda mal el delete.
					click.target.value = null;
				});
				element.bind("change", function (changeEvent) {
					var reader = new FileReader();
					reader.onload = function (loadEvent) {
						scope.$apply(function () {
							scope.ngFileRead = loadEvent.target.result;
						});
					};
					reader.readAsDataURL(changeEvent.target.files[0]);
				});
			}
		}
	});
});