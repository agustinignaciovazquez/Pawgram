'use strict';

/**
 * @ngdoc function
 * @name pawgramApp.services:AuthService
 * @description
 * # AuthService
 * AuthService of the pawgramApp
 */

define(['pawgramApp', 'services/sessionService', 'services/restService'], function(pawgramApp){
		return pawgramApp.factory('authService', ['$http', 'url', 'sessionService', '$q', '$location', '$rootScope', function($http, url, session, $q, $location, $rootScope) {
			
			var AuthService = {};
			AuthService.loggedUser = session.getUser();
			var redirectToUrlAfterLogin = '/'; //DEFAULT

			AuthService.logIn = function(username, password, saveToSession) {
				//var credentials = { j_username: username, j_password: password };
				var self = this;
				return $http({
						method: 'POST',
						url: url + '/login',
						headers: {'Content-Type': 'application/x-www-form-urlencoded'},
						transformRequest: function(obj) {
							var str = [];
							for (var p in obj){
								str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
							}
							return str.join("&");
						},
						data: {j_username: username, j_password: password}
					})
					.then(function(response) {
						session.setAccessToken(response.headers('X-AUTH-TOKEN'), saveToSession);
						return $http.get(url + '/users/', {headers: {'X-AUTH-TOKEN': session.getAccessToken()}});
					})
					.then(function(response) {
						return response.data;
					})
					.then(function(data) {
						session.setUser(data, saveToSession);
						self.loggedUser = data;
						$rootScope.$broadcast('user:updated');
						return data;
					})
					.catch(function(response) {
						self.logOut();
						return $q.reject(response);
					});
			};

			AuthService.isLoggedIn = function() {
				return !!this.loggedUser;
			};

			AuthService.logOut = function() {
				session.destroy();
				this.loggedUser = null;
				$rootScope.$broadcast('user:updated');
			};

			AuthService.getLoggedUser = function() {
				return this.loggedUser;
			};

			AuthService.saveAttemptUrl = function(){
				if($location.path().toLowerCase() !== '/login') {
		        	redirectToUrlAfterLogin = $location.path();
		    	} 
			};

			AuthService.redirectToAttemptUrl = function(){
				 $location.path(redirectToUrlAfterLogin);
			};

			return AuthService;
		}]);
});
