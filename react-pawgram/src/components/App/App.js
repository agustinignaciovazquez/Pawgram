import React, { Component } from 'react'
import {HashRouter as Router, Switch, Route, Link, Redirect} from "react-router-dom";

import NavBar from '../NavBar/NavBar'
import Login from '../Login/Login'
import Register from '../Register/Register'
import Main from '../Main/Main'
import PostComplete from "../Post/PostComplete/PostComplete";
import Logout from "../Logout/Logout";
import PostSearchContainer from "../Post/PostSearch/PostSearchContainer";
import PostABM from "../Post/PostABM/PostABM";
import PostSelectCategory from "../Post/PostABM/PostSelectCategory";
import PostEdit from "../Post/PostABM/PostEdit";
import UserSearchZones from "../SearchZone/UserSearchZones";
import SearchZoneAdd from "../SearchZone/SearchZoneAdd";
import NotificationCardsGrid from "../Notification/NotificationCardsGrid";
import {Config} from "../../services/Config";
import {AuthService} from "../../services/AuthService";
import Box from "@material-ui/core/Box";
import {Copyright} from "../../services/Utils";
import Container from "@material-ui/core/Container";

class App extends Component {
    constructor(props, context) {
        super(props, context);
        const userSaved = AuthService().getLoggedUser();
        this.state = {
            user: userSaved,
        };
    }

    updateUser(user){
        const userSaved = AuthService().getLoggedUser();
        this.setState({user:user});
    }

    render() {
        return (
            <Router>
                <div>
                  <NavBar user={this.state.user}/>
                    <Switch>
                        <Route path="/login" render={(props) => <Login {...props} callback={e=>{this.updateUser(e)}} />} />
                        <Route path="/register" render={(props) => <Register {...props} callback={e=>{this.updateUser(e)}} />} />
                        <Route path="/logout" render={(props) => <Logout {...props} callback={e=>{this.updateUser(e)}} />} />

                        <Route path='/notifications/all' render={(props) => <NotificationCardsGrid {...props} all={true} />}/>
                        <Route path='/notifications' render={(props) => <NotificationCardsGrid {...props} all={false} />}/>

                        <Route path="/searchzones/create" component={SearchZoneAdd}/>
                        <Route path="/searchzones" component={UserSearchZones}/>

                        <Route path="/category/:category/search/:query" component={PostSearchContainer} />
                        <Route path="/search/:query" component={PostSearchContainer} />

                        <Route path="/main" component={Main} />

                        <Route path="/post/create/category/:category" component={PostABM} />
                        <Route path="/post/create" component={PostSelectCategory} />
                        <Route path="/post/edit/:id" component={PostEdit}/>
                        <Route path="/post/:id" component={PostComplete}/>


                        <Route path="/" render={(props) => <Redirect {...props} to={'/login'} />} />
                    </Switch>
                </div>
                <Box mt={5}>
                    <Copyright />
                </Box>
            </Router>
    )
  }
}
export default App