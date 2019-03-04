'use strict';

/**
 * @ngdoc function
 * @name pawgramApp.services:restService
 * @description
 * # RestService
 * RestService of the pawgramApp
 */
angular.module('pawgramApp')
.factory('restService', ['$http', '$q', 'url', 'sessionService','jQuery', function($http, $q, url, session,jQuery) {
			
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
			  var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

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
						if (value){
							translated[translateTable[key]] = value;
						}
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
				
				if (accessToken){
					metadata.headers['X-AUTH-TOKEN'] = session.getAccessToken();
				}
				
				return metadata;
			}
			
			function doPost(baseUrl, data, params, ignoreLoadingBar) {
				var paramsPost = translate(params);
				paramsPost = Object.keys(paramsPost).length ? '?' + jQuery.param(paramsPost) : '';
				var config = authHeaders();
				config['ignoreLoadingBar'] = !!ignoreLoadingBar;

				return $http.post(baseUrl + paramsPost, JSON.stringify(data), config)
						.then(function(response) {
							return response.data;
						})
						.catch(function(response) {
							return $q.reject(response);
						});   
			}
			
			function doGet(baseUrl, params, ignoreLoadingBar) {
				var paramsGet = translate(params);
				paramsGet = Object.keys(paramsGet).length ? '?' + jQuery.param(paramsGet) : '';
				var config = authHeaders();
				config['ignoreLoadingBar'] = !!ignoreLoadingBar;

				return  $http.get(baseUrl + paramsGet, config)
						.then(function(response) {
							return response.data;
						})
						.catch(function(response) {
							return $q.reject(response);
						});
			}
			
			function doPut(baseUrl, data, params, ignoreLoadingBar) {
				var paramsPut = translate(params);
				paramsPut = Object.keys(paramsPut).length ? '?' + jQuery.param(paramsPut) : '';
				var config = authHeaders();
				config['ignoreLoadingBar'] = !!ignoreLoadingBar;

				return $http.put(baseUrl + paramsPut, JSON.stringify(data), config)
						.then(function(response) {
							return response.data;
						})
						.catch(function(response) {
							return $q.reject(response);
						});
			}

			function doDelete(baseUrl, params, ignoreLoadingBar) {
				var paramsDelete = translate(params);
				paramsDelete = Object.keys(paramsDelete).length ? '?' + jQuery.param(paramsDelete) : '';
				var config = authHeaders();
				config['ignoreLoadingBar'] = !!ignoreLoadingBar;

				return $http.delete(baseUrl + paramsDelete, config)
						.then(function(response) {
							return response.data;
						})
						.catch(function(response) {
							return $q.reject(response);
						});
			}            
			return {
				getPosts: function(params) {
					return doGet(url + '/posts/', params);
				},
				
				getPost: function(id) {
					return doGet(url + '/posts/' + id);
				},

				getPostByZoneId: function(id, params) {
					return doGet(url + '/posts/zone/' + id, params);
				},

				searchPost: function(query, page, pageSize) {
					return doGet(url + '/posts/search/', {query: query, page: page, pageSize: pageSize});
				},

				deletePost: function(id) {
					return doDelete(url + '/posts/' + id);
				},

				deletePostImage: function(postId,postImageId) {
					return doDelete(url + '/posts/' + postId + '/image/'+ postImageId);
				},

				getComments: function(id, params) {
					return doGet(url + '/post/' + id + '/comments', params);
				},

				commentPost: function(id, comment) {
					return doPost(url + '/posts/' + id + '/comments', {content: comment}, null, true);
				},
				  
				commentParentPost: function(id, comment, parentCommentId) {
					return doPost(url + '/posts/' + id + '/comments', {content: comment, parent_id: parentCommentId}, null, true);
				},

				createPost: function(data) {
					var postData = {title: data.title, description: data.description, contact_phone: data.contact_phone, event_date: data.event_date, category: data.category,
						pet: data.pet, is_male: data.is_male, latitude: data.latitude, longitude: data.longitude};
					var images = data.images;
					var formData = new FormData();
					
					angular.forEach(images, function(img) {
						if (img){
							formData.append('picture', dataURItoBlob(img));
						}
					});
										
					
					formData.append('post', new Blob([JSON.stringify(postData)], {type: "application/json"}));
					
					return $http.post(url + '/posts/', formData, multipartMetadata())
					.then(function(response) {
						return response.data;
					})
					.catch(function(response) {
						return $q.reject(response);
					});
				},

				modifyPost: function(id,data) {
					var postData = {title: data.title, description: data.description, contact_phone: data.contact_phone, event_date: data.event_date, 
						category: data.category, pet: data.pet, is_male: data.is_male, latitude: data.latitude, longitude: data.longitude};
					var images = data.images;
					var formData = new FormData();
					
					angular.forEach(images, function(img) {
						if (img){
							formData.append('picture', dataURItoBlob(img));
						}
					});
										
					formData.append('post', new Blob([JSON.stringify(postData)], {type: "application/json"}));
					
					return $http.put(url + '/posts/' + id, formData, multipartMetadata())
					.then(function(response) {
						return response.data;
					})
					.catch(function(response) {
						return $q.reject(response);
					});
				},

				subscribePost: function(id) {
					return doPut(url + '/posts/' + id + '/subscriptions', null, null, true);
				},
				
				unsubscribePost: function(id) {
					return doDelete(url + '/posts/' + id + '/subscriptions', null, true);
				},

				getUser: function(id) {
					return doGet(url + '/users/' + id);
				},

				getUserSubscriptions: function(id, params) {
					return doGet(url + '/users/' + id + '/subscriptions', params);
				},	

				getPostedByUser: function(id, params) {
					return doGet(url + '/users/' + id + '/user_posts', params);
				},

				createUser: function(data) {
					var userData = {name: data.name, surname: data.surname, password: data.password, mail: data.mail};
					var picture = data.picture;
					var formData = new FormData();
					if(picture !== undefined && picture !== null){
						formData.append('picture', dataURItoBlob(picture));
					}
					formData.append('user', new Blob([JSON.stringify(userData)], {type: "application/json"}));
					
					return $http.post(url + '/users/', formData, multipartMetadata())
					.then(function(response) {
						return response.data;
					})
					.catch(function(response) {
						return $q.reject(response);
					});
				},

				changePassword: function(currentPass, newPass) {
					return doPut(url + '/users/password', 
							{'current_password': currentPass, 'new_password': newPass});
				},

				getRecoverToken: function(mail) {
					return doPut(url + '/users/get_recover_token', 
							{'mail': mail});
				},

				recoverPassword: function(mail, token, newPass) {
					return doPut(url + '/users/recover_password', 
							{'mail': mail,'token': token, 'newPassword': newPass});
				},

				changeProfilePicture: function(data) {
					var picture = data.picture;
					var formData = new FormData();

					formData.append('picture', dataURItoBlob(picture));
					return $http.put(url + '/users/picture', formData, multipartMetadata())
					.then(function(response){
						return response.data;
					})
					.catch(function(response){
						return $q.reject(response.data);
					});
				},

				getSearchZones: function() {
					return doGet(url + '/sz/');
				},

				createSearchZone: function(data) {
					var szData = {latitude: data.latitude, longitude: data.longitude, range: data.range};
					var formData = new FormData();
					formData.append('sz', new Blob([JSON.stringify(szData)], {type: "application/json"}));
					
					return $http.post(url + '/sz/create', formData, multipartMetadata())
					.then(function(response) {
						return response.data;
					})
					.catch(function(response) {
						return $q.reject(response);
					});
				},

				deleteSearchZone: function(id) {
					return doDelete(url + '/sz/' + id);
				},	

				getMessages: function(id,params) {
					return doGet(url + '/messages/'+id, params);
				},	

				sendMessage: function(id,data) {
					var messageData = {message: data.message};
					var formData = new FormData();
					formData.append('message', new Blob([JSON.stringify(messageData)], {type: "application/json"}));
					
					return $http.post(url + '/messages/'+ id + '/send', formData, multipartMetadata())
					.then(function(response) {
						return response.data;
					})
					.catch(function(response) {
						return $q.reject(response);
					});
				},

				getConversations: function() {
					return doGet(url + '/messages/conversations/');
				},	

				getNotification: function(id) {
					return doGet(url + '/notifications/'+ id);
				},

				markNotificationAsSeen: function(id) {
					return doPut(url + '/notifications/' + id, 
							{});
				},

				getNotifications: function(params) {
					return doGet(url + '/notifications/', params);
				},

				getAllNotifications: function(params) {
					return doGet(url + '/notifications/all', params);
				}
				
			};
		}]);