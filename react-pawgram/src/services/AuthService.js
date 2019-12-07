import {SessionService} from "./SessionService";
import axios from 'axios';
import qs from 'qs'
export const AuthService = (url, props) =>{

    const AuthService = {};
    AuthService.loggedUser = SessionService().getUser();
    let redirectToUrlAfterLogin = '/'; //DEFAULT

    AuthService.logIn = function(username, password, saveToSession) {
        let self = this;
        return axios.post(url + '/login', qs.stringify({
            j_username: username,
            j_password: password
        }, {headers: {
            'content-type': 'application/x-www-form-urlencoded;charset=utf-8'
        }}))
            .then(function(response) {
                SessionService().setAccessToken(response.headers['x-auth-token'], saveToSession);
                return axios.get(url + '/users/', {headers: {'x-auth-token': SessionService().getAccessToken()}});
            })
            .then(function(response) {
                return response.data;
            })
            .then(function(data) {
                SessionService().setUser(data, saveToSession);
                self.loggedUser = data;
                return data;
            })
            .catch(function(response) {
                console.log(response);
                return Promise.reject(response);
            });
    };

    AuthService.isLoggedIn = function() {
        return !!this.loggedUser;
    }

    AuthService.getLoggedUser = function() {
        return this.loggedUser;
    };

    AuthService.logOut = function() {
        SessionService().destroy();
        this.loggedUser = null;
        props.history.push('/');
    };

    AuthService.saveAttemptUrl = function(){
        if(props.location.pathname.toLowerCase() !== '/login') {
            redirectToUrlAfterLogin = props.location.pathname;
        }
    };

    AuthService.redirectToAttemptUrl = function(){
        props.history.push(redirectToUrlAfterLogin);
    };

    return AuthService;
}