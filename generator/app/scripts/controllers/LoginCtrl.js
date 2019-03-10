'use strict';

/**
 * @ngdoc function
 * @name pawgramApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the pawgramApp
 */
 define(['pawgramApp', 'services/authService', 'services/snackbarService'], function(pawgramApp) {
 	
 	pawgramApp.controller('LoginCtrl',  ['authService', '$scope', 'snackbarService', function(auth, $scope, snackbarService) {
		$scope.loginForm = {};
		
		$scope.loginForm.username = {};
		$scope.loginForm.password = {};

		$scope.invalidUser = false;
		$scope.loggingIn = false;
		$scope.isLoggedIn = auth.isLoggedIn();
		if($scope.isLoggedIn){
				auth.redirectToAttemptUrl();
		}
		
		$scope.$on('user:updated', function() {
			$scope.isLoggedIn = auth.isLoggedIn();
			if($scope.isLoggedIn){
				auth.redirectToAttemptUrl();
			}
		});	

		$scope.loginSubmit = function() {
			if($scope.form.$valid) {
				$scope.loggingIn = true;
				
				auth.logIn($scope.loginForm.username.text, $scope.loginForm.password.text, $scope.loginForm.rememberMe)
				.then(function(response) {
					$scope.invalidUser = false;
					$scope.loggingIn = false;
				})
				.catch(function(error) {
					$scope.loggingIn = false;
					switch (error.status) {
						case -1: // Sin conexión
							snackbarService.showNoConnection();
							break;
						case 401: // Conflict - 
							$scope.invalidUser = true;
							break;
					}
				});
			}
		};
	}]);

 });
