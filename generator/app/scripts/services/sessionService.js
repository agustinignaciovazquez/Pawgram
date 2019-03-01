'use strict';

/**
 * @ngdoc function
 * @name pawgramApp.services:sessionService
 * @description
 * # sessionService
 * SessionService of the pawgramApp
 */
var app=angular.module("pawgramApp",[]);
app.factory('sessionService', function($window) {
		var Session = {};

		Session._user = JSON.parse($window.localStorage.getItem('session.user')) || JSON.parse($window.sessionStorage.getItem('session.user'));
		
		Session._accessToken = JSON.parse($window.localStorage.getItem('session.accessToken')) || JSON.parse($window.sessionStorage.getItem('session.accessToken'));
		
		Session.getUser = function(){
			return this._user;
		};

		Session.setUser = function(user, isLocalStorage){
			this._user = user;
			
			if (isLocalStorage){
				$window.localStorage.setItem('session.user', JSON.stringify(user));
			}else{
				$window.sessionStorage.setItem('session.user', JSON.stringify(user));
			}
			return this;
		};

		Session.getAccessToken = function(){
			return this._accessToken;
		};

		Session.setAccessToken = function(token, isLocalStorage){
			this._accessToken = token;
			if (isLocalStorage){
				$window.localStorage.setItem('session.accessToken', JSON.stringify(token));
			}else{
				$window.sessionStorage.setItem('session.accessToken', JSON.stringify(token));
			}
			return this;
		};

		Session.destroy = function destroy(){
			this.setUser(null);
			this.setUser(null, true);
			this.setAccessToken(null);
			this.setAccessToken(null, true);
		};
		
		return Session;
	});