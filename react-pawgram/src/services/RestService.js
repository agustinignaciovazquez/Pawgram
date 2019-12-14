import {SessionService} from "./SessionService";
import axios from 'axios';
import param from 'jquery-param';
import {Config} from "./Config";

export const RestService = () => {
    const url = Config.API_URL;
    //const param = require('jquery-param');

    const translateTable = {
        category: 'category',
        latitude: 'latitude',
        longitude: 'longitude',
        range: 'range',
        page: 'page',
        pageSize: 'per_page',
        orderBy: 'sorted_by',
        order: 'order',
        query: 'keyword'
    };

    function translate(params) {
        let translated = {};

        if (params) {
            Object.keys(params).forEach(function (key) {
                let value = params[key];
                if (value && translateTable[key]){
                    translated[translateTable[key]] = value;
                }
            });
        }

        return translated;
    }

    function authHeaders() {
        const accessToken = SessionService().getAccessToken();
        return accessToken ? {headers: {'X-AUTH-TOKEN': accessToken}} : {};
    }

    function multipartMetadata() {
        var accessToken = SessionService().getAccessToken();
        var metadata = {
            headers: {
                //'Content-Type': undefined
                'content-type': 'multipart/form-data'
            }
        };

        if (accessToken){
            metadata.headers['X-AUTH-TOKEN'] = accessToken;
        }

        return metadata;
    }

    function dataURItoBlob(dataURI) {
        // convert base64 to raw binary data held in a string
        let byteString = atob(dataURI.split(',')[1]);

        // separate out the mime component
        let mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

        // write the bytes of the string to an ArrayBuffer
        let ab = new ArrayBuffer(byteString.length);

        // create a view into the buffer
        let ia = new Uint8Array(ab);

        // set the bytes of the buffer to the correct values
        for (let i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }

        // write the ArrayBuffer to a blob, and you're done
        let blob = new Blob([ab], {type: mimeString});
        return blob;

    }

    function doPost(baseUrl, data, params, ignoreLoadingBar) {
        var paramsPost = translate(params);
        paramsPost = Object.keys(paramsPost).length ? '?' + param(paramsPost) : '';
        var config = authHeaders();
        config['ignoreLoadingBar'] = !!ignoreLoadingBar;

        return axios.post(baseUrl + paramsPost, data, config)
            .then(function(response) {
                return response.data;
            })
            .catch(function(response) {
                if(response.response && response.response.status === 401){
                    SessionService().destroy();
                }
                return Promise.reject(response);
            });
    }

    function doGet(baseUrl, params, ignoreLoadingBar) {
        var paramsGet = translate(params);

        paramsGet = Object.keys(paramsGet).length ? '?' + param(paramsGet) : '';
        var config = authHeaders();
        config['ignoreLoadingBar'] = !!ignoreLoadingBar;

        return  axios.get(baseUrl + paramsGet, config)
            .then(function(response) {
                return response.data;
            })
            .catch(function(response) {
                if(response.response && response.response.status === 401){
                    SessionService().destroy();
                }
                return Promise.reject(response);
            });
    }

    function doPut(baseUrl, data, params, ignoreLoadingBar) {
        var paramsPut = translate(params);
        paramsPut = Object.keys(paramsPut).length ? '?' + param(paramsPut) : '';
        var config = authHeaders();
        config['ignoreLoadingBar'] = !!ignoreLoadingBar;

        return axios.put(baseUrl + paramsPut, data, config)
            .then(function(response) {
                return response.data;
            })
            .catch(function(response) {
                if(response.response && response.response.status === 401){
                    SessionService().destroy();
                }
                return Promise.reject(response);
            });
    }

    function doDelete(baseUrl, params, ignoreLoadingBar) {
        var paramsDelete = translate(params);
        paramsDelete = Object.keys(paramsDelete).length ? '?' + param(paramsDelete) : '';
        var config = authHeaders();
        config['ignoreLoadingBar'] = !!ignoreLoadingBar;

        return axios.delete(baseUrl + paramsDelete, config)
            .then(function(response) {
                return response.data;
            })
            .catch(function(response) {
                if(response.response && response.response.status === 401){
                    SessionService().destroy();
                }
                return Promise.reject(response);
            });
    }
    
    return {
        getPosts: function(params) {
            return doGet(url + '/posts/', params);
        },

        getPost: function(id, params) {
            return doGet(url + '/posts/' + id, params);
        },

        getPostByZoneId: function(id, params) {
            return doGet(url + '/posts/zone/' + id, params);
        },

        searchPost: function(query, category=null, orderBy=null, order=null, page=1, pageSize=Config.PAGE_SIZE, latitude=null, longitude=null) {

            const params_search = {
                'query': query,
                'latitude': latitude,
                'longitude': longitude,
                'category': category,
                'order': order,
                'orderBy': orderBy,
                'page': page,
                'pageSize': pageSize
            };
            return doGet(url + '/posts/search/', params_search);
        },

        deletePost: function(id) {
            return doDelete(url + '/posts/' + id);
        },

        deletePostImage: function(postId,postImageId) {
            return doDelete(url + '/posts/' + postId + '/image/'+ postImageId);
        },

        //Not used
        getComments: function(id, params) {
            return doGet(url + '/post/' + id + '/comments', params);
        },

        commentPost: function(id, comment) {
            return doPost(url + '/posts/' + id + '/comments', {content: comment}, null, true);
        },

        commentParentPost: function(id, comment, parentCommentId) {
            if (parentCommentId === 0)
                return this.commentPost(id, comment);
            return doPost(url + '/posts/' + id + '/comments', {content: comment, parent_id: parentCommentId}, null, true);
        },

        createPost: function(data) {
            var postData = {title: data.title, description: data.description, contact_phone: data.contact_phone, event_date: data.event_date, category: data.category,
                pet: data.pet, is_male: data.is_male, latitude: data.latitude, longitude: data.longitude};
            var images = data.images;
            var formData = new FormData();

            images.forEach(function (img) {
                if (img){
                    formData.append('picture', dataURItoBlob(img));
                }
            });


            formData.append('post', new Blob([JSON.stringify(postData)], {type: "application/json"}));

            return axios.post(url + '/posts/', formData, multipartMetadata())
                .then(function(response) {
                    return response.data;
                })
                .catch(function(response) {
                    if(response.response && response.response.status === 401){
                        SessionService().destroy();
                    }
                    return Promise.reject(response);
                });
        },

        modifyPost: function(id,data) {
            var postData = {title: data.title, description: data.description, contact_phone: data.contact_phone, event_date: data.event_date,
                category: data.category, pet: data.pet, is_male: data.is_male, latitude: data.latitude, longitude: data.longitude};
            var images = data.images;
            var formData = new FormData();

            images.forEach(function (img) {
                if (img){
                    formData.append('picture', dataURItoBlob(img));
                }
            });


            formData.append('post', new Blob([JSON.stringify(postData)], {type: "application/json"}));

            return axios.put(url + '/posts/' + id, formData, multipartMetadata())
                .then(function(response) {
                    return response.data;
                })
                .catch(function(response) {
                    if(response.response && response.response.status === 401){
                        SessionService().destroy();
                    }
                    return Promise.reject(response);
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

            return axios.post(url + '/users/', formData, multipartMetadata())
                .then(function(response) {
                    return response.data;
                })
                .catch(function(response) {
                    if(response.response && response.response.status === 401){
                        SessionService().destroy();
                    }
                    return Promise.reject(response);
                });
        },

        checkDuplicatedMail: function(mail) {
            return doGet(url + '/users/check/'+mail);
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
            return axios.put(url + '/users/picture', formData, multipartMetadata())
                .then(function(response){
                    return response.data;
                })
                .catch(function(response){
                    if(response.response && response.response.status === 401){
                        SessionService().destroy();
                    }
                    return Promise.reject(response);
                });
        },

        getSearchZones: function() {
            return doGet(url + '/sz/');
        },

        createSearchZone: function(data) {
            var szData = {latitude: data.latitude, longitude: data.longitude, range: data.range};
            var formData = new FormData();
            formData.append('sz', new Blob([JSON.stringify(szData)], {type: "application/json"}));

            return axios.post(url + '/sz/create', formData, multipartMetadata())
                .then(function(response) {
                    return response.data;
                })
                .catch(function(response) {
                    if(response.response && response.response.status === 401){
                        SessionService().destroy();
                    }
                    return Promise.reject(response);
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

            return axios.post(url + '/messages/'+ id + '/send', formData, multipartMetadata())
                .then(function(response) {
                    return response.data;
                })
                .catch(function(response) {
                    if(response.response && response.response.status === 401){
                        SessionService().destroy();
                    }
                    return Promise.reject(response);
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
};