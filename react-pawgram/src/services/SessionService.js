export const SessionService = () =>{
    const Session = {};

    Session._user = JSON.parse(localStorage.getItem('session.user')) || JSON.parse(sessionStorage.getItem('session.user'));

    Session._accessToken = JSON.parse(localStorage.getItem('session.accessToken')) || JSON.parse(sessionStorage.getItem('session.accessToken'));

    Session.getUser = function(){
        return this._user;
    };

    Session.setUser = function(user, isLocalStorage){
        this._user = user;

        if (isLocalStorage){
            localStorage.setItem('session.user', JSON.stringify(user));
        }else{
            sessionStorage.setItem('session.user', JSON.stringify(user));
        }
        return this;
    };

    Session.isLoggedIn = function() {
        return !!this._user && !!this._accessToken;
    };

    Session.getAccessToken = function(){
        return this._accessToken;
    };

    Session.isLocalSaved = function(){
        return !!(this.isLoggedIn() && JSON.parse(localStorage.getItem('session.user')));

    };

    Session.setAccessToken = function(token, isLocalStorage){
        this._accessToken = token;
        if (isLocalStorage){
            localStorage.setItem('session.accessToken', JSON.stringify(token));
        }else{
            sessionStorage.setItem('session.accessToken', JSON.stringify(token));
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
}