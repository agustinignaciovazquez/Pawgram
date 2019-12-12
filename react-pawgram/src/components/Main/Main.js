import React, { Component } from 'react';
import {AuthService} from "../../services/AuthService";
import {RestService} from "../../services/RestService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next/src/index';
import PostDataGrid from '../Post/PostCardsGrid/PostCardsGrid'

class Main extends Component {
    state = {posts: undefined};

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        RestService().getPosts({'latitude': -34.6037618, 'longitude': -58.381715, 'range': 1000000})
            .then(r=>{
                this.setState({'posts': r});
            }).catch(r=>{
                //TODO SHOW ERROR
             });
    }

    render() {
        if(this.state.posts === undefined)
            return ("LOADING");
        return(<PostDataGrid posts={this.state.posts} />);
    }
}

export default withTranslation()(Main);