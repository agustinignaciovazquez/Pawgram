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
import PostSubscriptions from "./PostSubscriptions";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class PostSubscriptionsContainer extends Component {

    constructor(props, context) {
        super(props, context);
        const query = props.match.params.query;
        this.state = {
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }

    }

    render() {
        const { classes,t } =  this.props;
        let query = this.state.query;
        const category = this.props.match.params.category;
        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
                <Grid item xs={10} sm={10}>
                    <Typography variant="h4" display="block" align={"left"} gutterBottom>
                        {t('my-subscriptions')}
                    </Typography>
                </Grid>
            <Grid item xs={10} sm={10}><PostSubscriptions category={category}/></Grid>

        </Grid>);
    }
}
PostSubscriptionsContainer.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostSubscriptionsContainer));