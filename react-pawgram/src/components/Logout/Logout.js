import React, { Component } from 'react';
import {AuthService} from "../../services/AuthService";
import { withTranslation } from 'react-i18next/src/index';
import {Redirect} from "react-router-dom";

class Logout extends Component {

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        AuthService().logOut();
    }

    render() {
        return(<Redirect to={'/login'} />);
    }
}

export default withTranslation()(Logout);