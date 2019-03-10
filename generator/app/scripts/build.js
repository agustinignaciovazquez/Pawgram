/* global paths */
'use strict';
require.config({
	baseUrl: 'scripts',
	paths: {
		angular: '../../bower_components/angular/angular',
		'angular-route': '../../bower_components/angular-route/angular-route',
		'angular-translate': '../../bower_components/angular-translate/angular-translate',
		bootstrap: '../../bower_components/bootstrap/dist/js/bootstrap',
		jquery: '../../bower_components/jquery/dist/jquery',
		requirejs: '../../bower_components/requirejs/require',
		'angular-mocks': '../../bower_components/angular-mocks/angular-mocks',
		'angular-bootstrap': '../../bower_components/angular-bootstrap/ui-bootstrap-tpls',
		'angular-sanitize': '../../bower_components/angular-sanitize/angular-sanitize',
		'angular-loading-bar': '../../bower_components/angular-loading-bar/build/loading-bar',
		'angular-animate': '../../bower_components/angular-animate/angular-animate',
		'angular-cookies': '../../bower_components/angular-cookies/angular-cookies',
		'angular-resource': '../../bower_components/angular-resource/angular-resource',
		'angular-touch': '../../bower_components/angular-touch/angular-touch',
		'angular-translate-loader-static-files': '../../bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files'
	},
	shim: {
		angular: {
			deps: [
				'jquery'
			]
		},
		'angular-route': {
			deps: [
				'angular'
			]
		},
		bootstrap: {
			deps: [
				'jquery'
			]
		},
		'angular-translate': {
			deps: [
				'angular',
			]
		},
		'angular-translate-loader-static-files': {
			deps: [
				'angular-translate'
			]
		},
		'angular-sanitize': {
			deps: [
				'angular'
			]
		},
		'angular-bootstrap': {
			deps: [
				'angular'
			]
		},
		'angular-loading-bar': {
			deps: [
				'angular'
			]
		},
		'angular-animate': {
			deps: [
				'angular'
			]
		},
		'angular-cookies': {
			deps: [
				'angular'
			]
		},
		'angular-resource': {
			deps: [
				'angular'
			]
		},
		'angular-touch': {
			deps: [
				'angular'
			]
		},

	},
	packages: [

	]
});

if (paths) {
	require.config({
		paths: paths
	});
}

require([
		'angular',
		'pawgramApp',
		'controllers/MainCtrl'
	],
	function() {
		angular.bootstrap(document, ['pawgramApp']);
	}
);