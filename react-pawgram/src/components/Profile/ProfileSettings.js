import React, { Component } from 'react';
import {AuthService} from "../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";
import SearchIcon from "@material-ui/icons/Search"
import InputAdornment from '@material-ui/core/InputAdornment';
import {ValidatorForm, TextValidator} from "react-material-ui-form-validator";
import PropTypes from "prop-types";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import {Link as LinkDom, Redirect} from "react-router-dom";
import Paper from "@material-ui/core/Paper";
import Box from "@material-ui/core/Box";
import Link from "@material-ui/core/Link";
import ErrorIcon from "@material-ui/icons/Error"
import PostEditUser from "./PostEditUser";
const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class NotFoundComponent extends Component {

    constructor(props, context) {
        super(props, context);
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
        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
            <Grid container alignContent={"center"} justify={"center"} spacing={2}>
                <Grid item xs={7} sm={7}>
                    <Paper>
                        <Box p={2}>
                            <Grid container alignContent={"center"} justify={"center"} spacing={2} spacing={4}>
                                <Grid item xs={6} sm={6}>
                                    <Typography variant="h4" display="block" align={"center"} gutterBottom>
                                        {t('settings')}
                                    </Typography>
                                </Grid>
                                <Grid item xs={10} sm={10}>
                                    <PostEditUser />
                                </Grid>

                            </Grid>
                        </Box>
                    </Paper>
                </Grid>
            </Grid>
        </Grid>);
    }
}

NotFoundComponent.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(NotFoundComponent));