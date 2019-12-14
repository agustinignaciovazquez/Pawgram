import React, { Component } from 'react'
import {BrowserRouter as Router, Switch, Route, Link, Redirect} from "react-router-dom";
import {AuthService} from "../../services/AuthService";
import NavBar from '../NavBar/NavBar'
import Login from '../Login/Login'
import Register from '../Register/Register'
import Main from '../Main/Main'
import PostComplete from "../Post/PostComplete/PostComplete";
import Logout from "../Logout/Logout";
import PostSearchContainer from "../Post/PostSearch/PostSearchContainer";
class App extends Component {
  render() {
    return (
        <Router>
            <div>
              <NavBar />
                <Switch>
                    <Route path="/login" component={Login} />
                    <Route path="/search/:query"  component={PostSearchContainer} />
                    <Route path="/category/:category/search/:query"  component={PostSearchContainer} />
                    <Route path="/register" component={Register} />
                    <Route path="/main" component={Main} />
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