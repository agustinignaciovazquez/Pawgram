import React, { Component } from 'react'
import {BrowserRouter as Router, Switch, Route, Link, Redirect} from "react-router-dom";

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

class App extends Component {
  render() {
    return (
        <Router>
            <div>
              <NavBar />
                <Switch>
                    <Route path="/login" component={Login} />
                    <Route path="/register" component={Register} />

                    <Route path="/searchzones/create" component={SearchZoneAdd}/>
                    <Route path="/searchzones" component={UserSearchZones}/>

                    <Route path="/category/:category/search/:query" component={PostSearchContainer} />
                    <Route path="/search/:query" component={PostSearchContainer} />

                    <Route path="/main" component={Main} />

                    <Route path="/post/create/category/:category" component={PostABM} />
                    <Route path="/post/create" component={PostSelectCategory} />
                    <Route path="/post/edit/:id" component={PostEdit}/>
                    <Route path="/post/:id" component={PostComplete}/>

                    <Route path="/logout" component={Logout} />
                    <Route path="/" render={(props) => <Redirect {...props} to={'/login'} />} />
                </Switch>
            </div>
        </Router>
    )
  }
}
export default App