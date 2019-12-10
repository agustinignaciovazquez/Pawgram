import React, { Component } from 'react';
import {AuthService} from "../../services/AuthService";
import {RestService} from "../../services/RestService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next/src/index';
import PostDataGrid from '../../Post/PostCardsGrid/PostCardsGrid'

class Main extends Component {
    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        RestService().getPosts({'latitude': '-34.6037618', 'longitude': '-58.381715', 'range': '10'});
    }

    render() {
        return("MAIN HERE");
    }
}

export default withTranslation()(Main);