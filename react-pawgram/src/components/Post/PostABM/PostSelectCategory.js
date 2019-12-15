import React, { Component } from 'react';
import {AuthService} from "../../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";
import SearchIcon from "@material-ui/icons/Search"
import InputAdornment from '@material-ui/core/InputAdornment';
import {ValidatorForm, TextValidator} from "react-material-ui-form-validator";
import PropTypes from "prop-types";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import {Redirect} from "react-router-dom";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class PostSelectCategory extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            'category': undefined
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }

    }

    redirectToUrl(){
        if(this.state.redirectUrl){
            const redirectUrl = this.state.redirectUrl;
            this.setState({'redirectUrl': null});
            return ( <Redirect to={redirectUrl} />);
        }
    }

    handleClick(category){
        this.setState({'redirectUrl': '/create/category/'+category})
    }

    render() {
        const { classes,t } =  this.props;
        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
            <h2>Select category</h2>
        </Grid>);
    }
}
PostSelectCategory.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostSelectCategory));