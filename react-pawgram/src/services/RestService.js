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

    function doPost(baseUrl, data, params) {
        var paramsPost = translate(params);
        paramsPost = Object.keys(paramsPost).length ? '?' + param(paramsPost) : '';
        var config = authHeaders();


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

    function doGet(baseUrl, params) {
        var paramsGet = translate(params);

        paramsGet = Object.keys(paramsGet).length ? '?' + param(paramsGet) : '';
        var config = authHeaders();

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

    function doPut(baseUrl, data={}, params={}) {
        var paramsPut = translate(params);
        paramsPut = Object.keys(paramsPut).length ? '?' + param(paramsPut) : '';
        var config = authHeaders();

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

    function doDelete(baseUrl, params) {
        var paramsDelete = translate(params);
        paramsDelete = Object.keys(paramsDelete).length ? '?' + param(paramsDelete) : '';
        var config = authHeaders();

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
        getPosts: function(latitude, longitude, range, category=null, orderBy=null, order=null, page=1, pageSize=Config.PAGE_SIZE) {
            const params_posts = {
                'latitude': latitude,
                'longitude': longitude,
                'range': range,
                'category': category,
                'order': order,
                'orderBy': orderBy,
                'page': page,
                'pageSize': pageSize
            };
            return doGet(url + '/posts/', params_posts);
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
            const postData = {title: data.title, description: data.description, contact_phone: data.contact_phone, event_date: data.event_date, category: data.category,
                pet: data.pet, is_male: data.is_male, latitude: data.latitude, longitude: data.longitude};
            const images = data.images;
            const formData = new FormData();

            images.forEach(function (img) {
                if (img){
                    formData.append('picture', img);
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
            const postData = {title: data.title, description: data.description, contact_phone: data.contact_phone, event_date: data.event_date,
                category: data.category, pet: data.pet, is_male: data.is_male, latitude: data.latitude, longitude: data.longitude};
            const images = data.images;
            const formData = new FormData();

            images.forEach(function (img) {
                if (img){
                    formData.append('picture', img);
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

        getUserSubscriptions: function(params) {
            const user = SessionService().getUser();
            return doGet(url + '/users/' + user.id + '/subscriptions', params);
        },

        getPostedByUser: function(id, latitude=null, longitude=null, category=null, page=1, pageSize=Config.PAGE_SIZE) {
            const params_posts = {
                'latitude': latitude,
                'longitude': longitude,
                'category': category,
                'page': page,
                'pageSize': pageSize
            };
            return doGet(url + '/users/' + id + '/user_posts', params_posts);
        },

        createUser: function(data) {
            const userData = {name: data.name, surname: data.surname, password: data.password, mail: data.mail};
            const picture = data.picture;
            const formData = new FormData();

            if(picture !== undefined && picture !== null){
                formData.append('picture', picture);
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
            const picture = data.picture;
            const formData = new FormData();

            formData.append('picture', picture);
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

        getSearchZone: function(id) {
            return doGet(url + '/sz/'+id);
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
            return doPut(url + '/notifications/' + id +'/seen',{});
        },

        getNotifications: function(params) {
            return doGet(url + '/notifications/', params);
        },

        getAllNotifications: function(params) {
            return doGet(url + '/notifications/all', params);
        }

    };
};