'use strict';

/**
 * @ngdoc function
 * @name pawgramApp.controller:RegisterCtrl
 * @description
 * # RegisterCtrl
 * Controller of the pawgramApp
 */
 define(['pawgramApp', 'directives/validFile', 'directives/ngFileRead', 'services/restService', 'services/authService', 'services/snackbarService'], function(pawgramApp) {
 	
  pawgramApp.controller('RegisterCtrl', ['$scope', 'authService', 'restService', 'snackbarService', 'jQuery', function($scope, authService, restService, snackbarService, jQuery) {
		
		$scope.user = {};
		$scope.duplicateEmailError = false;
		
		$scope.emailPattern = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
		$scope.namePattern = /^[a-z,A-Z,á,é,í,ó,ú,â,ê,ô,ã,õ,ç,Á,É,Í,Ó,Ú,Â,Ê,Ô,Ã,Õ,Ç,ü,ñ,Ü,Ñ,' ']+$/i;

		$scope.isLoggedIn = authService.isLoggedIn();
		if($scope.isLoggedIn){
				authService.redirectToAttemptUrl();
		}
		
		$scope.$on('user:updated', function() {
			$scope.isLoggedIn = authService.isLoggedIn();
			if($scope.isLoggedIn){
				authService.redirectToAttemptUrl();
			}
		});	

		var checkPasswordsMatch = function() {
			$scope.passwordsMatch = $scope.user.password === $scope.user.passwordConf;
		};
		
		$scope.$watch('user.password', checkPasswordsMatch);
		$scope.$watch('user.passwordConf', checkPasswordsMatch);
		$scope.$watch('user.email', function() {
			$scope.duplicateEmailError = false;
		});
		
		$scope.signUpSubmit = function() {
			checkPasswordsMatch();
			if ($scope.signUpForm.$valid) {
				$scope.duplicateEmailError = false;
				$scope.loggingIn = true;

				restService.createUser($scope.user)
				.then(function(data) {
					return authService.logIn($scope.user.mail, $scope.user.password, true);
				})
				.then(function() {
					$scope.loggingIn = false;
				})
				.catch(function(error) {
					$scope.loggingIn = false;
					switch (error.status) {
						case -1:
							snackbarService.showNoConnection();
							break;
						case 409:
							$scope.duplicateEmailError = true;
							$scope.signUpForm.email.$invalid = true;
							break;
					}
				});
			}
		};
		
		$scope.deletePicture = function() {
			$scope.user.picture = null;
			jQuery('#pictureUpload:input[type=file]').val('');
		};
		
		
	}]);
 });
