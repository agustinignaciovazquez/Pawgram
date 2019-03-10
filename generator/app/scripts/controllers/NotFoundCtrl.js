'use strict';

/**
 * @ngdoc function
 * @name pawgramApp.controller:NotfoundctrlCtrl
 * @description
 * # NotfoundctrlCtrl
 * Controller of the pawgramApp
 */
 define(['pawgramApp','services/titleService'],function(pawgramApp){
 	pawgramApp.controller('NotFoundCtrl',['$translate', 'titleService', function($translate, titleService) {
		$translate('error.title').then(function(title) {
			titleService.setTitle(title);
		});
	}]);
 });

