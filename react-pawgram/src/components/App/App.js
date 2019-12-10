import React, { Component } from 'react'
import {BrowserRouter as Router, Switch, Route, Link, Redirect} from "react-router-dom";
import NavBar from '../NavBar/NavBar'
import Login from '../Login/Login'
import Register from '../Register/Register'
import Main from '../Main/Main'
class App extends Component {
  render() {
    return (
        <Router>
            <div>
              <NavBar />
                <Switch>
                    <Route path="/login" component={Login} />
                    <Route path="/register" component={Register} />
                    <Route path="/main" component={Main} />
                    <Route path="/" render={(props) => <Redirect {...props} to={'/login'} />} />
                </Switch>
            </div>
        </Router>
    )
  }
}
export default App