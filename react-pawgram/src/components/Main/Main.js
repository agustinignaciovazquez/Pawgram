import React, { Component } from 'react';
import { AuthService } from "../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";
import SearchIcon from "@material-ui/icons/Search"
import InputAdornment from '@material-ui/core/InputAdornment';
import {ValidatorForm, TextValidator} from "react-material-ui-form-validator";
import PropTypes from "prop-types";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import PostLocation from "../Post/PostLocation/PostLocation";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class PostLocationContainer extends Component {

    constructor(props, context) {
        super(props, context);
        const query = props.match.params.query;
        this.state = {
            query: query,
            query_input: query,
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }

    }

    render() {
        const { classes,t } =  this.props;
        const category = this.props.match.params.category;
        const location = {'latitude': -34.6037618, 'longitude': -58.381715, 'range': 1000000}
        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
            <Grid item xs={10} sm={10}><PostLocation location={location} category={category}/></Grid>

        </Grid>);
    }
}
PostLocationContainer.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostLocationContainer));