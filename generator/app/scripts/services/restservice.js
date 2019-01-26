'use strict';

/**
 * @ngdoc function
 * @name pawgramApp.services:restService
 * @description
 * # RestService
 * RestService of the pawgramApp
 */
angular.module('pawgramApp')
.factory('restService', ['$http', '$q', 'url', 'sessionService', function($http, $q, url, session) {
			
			var translateTable = {
					category: 'category',
					page: 'page',
					pageSize: 'per_page',
					orderBy: 'sorted_by',
					order: 'order',
					query: 'q'
			};
			
			function dataURItoBlob(dataURI) {
			  // convert base64 to raw binary data held in a string
			  var byteString = atob(dataURI.split(',')[1]);

			  // separate out the mime component
			  var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]

			  // write the bytes of the string to an ArrayBuffer
			  var ab = new ArrayBuffer(byteString.length);

			  // create a view into the buffer
			  var ia = new Uint8Array(ab);

			  // set the bytes of the buffer to the correct values
			  for (var i = 0; i < byteString.length; i++) {
				  ia[i] = byteString.charCodeAt(i);
			  }

			  // write the ArrayBuffer to a blob, and you're done
			  var blob = new Blob([ab], {type: mimeString});
			  return blob;

			}

			function translate(params) {
				var translated = {};
				
				if (params) {
					jQuery.each(params, function(key, value) {
						if (value)
							translated[translateTable[key]] = value;
					});
				}
				
				return translated;
			}
			
			function authHeaders() {
				var accessToken = session.getAccessToken();
				return accessToken ? {headers: {'X-AUTH-TOKEN': accessToken}} : {};
			}
			
			function multipartMetadata() {
				var accessToken = session.getAccessToken();
				var metadata = {
					transformRequest: angular.identity,
					headers: {
						'Content-Type': undefined
					}
				};
				
				if (accessToken)
					metadata.headers['X-AUTH-TOKEN'] = session.getAccessToken();
				
				return metadata;
			}
			
			function doPost(baseUrl, data, params, ignoreLoadingBar) {
				var params = translate(params);
				params = Object.keys(params).length ? '?' + jQuery.param(params) : '';
				var config = authHeaders();
				config['ignoreLoadingBar'] = !!ignoreLoadingBar;

				return $http.post(baseUrl + params, JSON.stringify(data), config)
						.then(function(response) {
							return response.data;
						})
						.catch(function(response) {
							return $q.reject(response);
						});   
			}
			
			function doGet(baseUrl, params, ignoreLoadingBar) {
				var params = translate(params);
				params = Object.keys(params).length ? '?' + jQuery.param(params) : '';
				var config = authHeaders();
				config['ignoreLoadingBar'] = !!ignoreLoadingBar;

				return  $http.get(baseUrl + params, config)
						.then(function(response) {
							return response.data;
						})
						.catch(function(response) {
							return $q.reject(response);
						});
			}
			
			function doPut(baseUrl, data, params, ignoreLoadingBar) {
				var params = translate(params);
				params = Object.keys(params).length ? '?' + jQuery.param(params) : '';
				var config = authHeaders();
				config['ignoreLoadingBar'] = !!ignoreLoadingBar;

				return $http.put(baseUrl + params, JSON.stringify(data), config)
						.then(function(response) {
							return response.data;
						})
						.catch(function(response) {
							return $q.reject(response);
						});
			}

			function doDelete(baseUrl, params, ignoreLoadingBar) {
				var params = translate(params);
				params = Object.keys(params).length ? '?' + jQuery.param(params) : '';
				var config = authHeaders();
				config['ignoreLoadingBar'] = !!ignoreLoadingBar;

				return $http.delete(baseUrl + params, config)
						.then(function(response) {
							return response.data;
						})
						.catch(function(response) {
							return $q.reject(response);
						});
			}            
			return {
				//TODO COMPLETE THIS WITH ALL API RESTFUL PATHS
				getPosts: function(params) {
					return doGet(url + '/post', params);
				},
				
				getPost: function(id) {
					return doGet(url + '/post/' + id);
				},
				
				getComments: function(id, params) {
					return doGet(url + '/post/' + id + '/comments', params);
				},
								
				getUser: function(id) {
					return doGet(url + '/users/' + id);
				},
								
				deletePost: function(id) {
					return doDelete(url + '/post/' + id);
				},
				
				changePassword: function(currentPass, newPass) {
					return doPut(url + '/user/password', 
							{'current_password': currentPass, 'new_password': newPass});
				},
				
				changeProfilePicture: function(data) {
					var picture = data.picture;
					var formData = new FormData();

					formData.append('picture', dataURItoBlob(picture));
					return $http.put(url + '/user/picture', formData, multipartMetadata())
					.then(function(response){
						return response.data;
					})
					.catch(function(response){
						return $q.reject(response.data);
					});
				},

				createUser: function(data) {
					var userData = {name: data.name, password: data.password, email: data.email};
					var picture = data.picture;
					var formData = new FormData();
					
					formData.append('picture', dataURItoBlob(picture));
					formData.append('user', new Blob([JSON.stringify(userData)], {type: "application/json"}));
					
					return $http.post(url + '/users', formData, multipartMetadata())
					.then(function(response) {
						return response.data;
					})
					.catch(function(response) {
						return $q.reject(response);
					});
				},
				
			
				commentPost: function(id, comment) {
					return doPost(url + '/post/' + id + '/comments', {content: comment}, null, true)
				},
				  
				commentParentPost: function(id, comment, parentCommentId) {
					return doPost(url + '/post/' + id + '/comments', {content: comment, parent_id: parentCommentId}, null, true)
				}
			}
		}]);