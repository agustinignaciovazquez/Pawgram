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
import AddIcon from "@material-ui/icons/Add";
import {Config} from "../../../services/Config";
import Paper from "@material-ui/core/Paper";
import Box from "@material-ui/core/Box";
import {Avatar} from "@material-ui/core";
import {RestService} from "../../../services/RestService";
import PostProfile from "./PostProfile";
import LinearProgress from "@material-ui/core/LinearProgress";
const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
    biggerAvatar: {
        width: 175,
        height: 175,
    },
});

class PostOwn extends Component {

    constructor(props, context) {
        super(props, context);
        const user_id = AuthService().getLoggedUser().id;
        this.state = {
            'user_id': user_id,
            'user':undefined
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        RestService().getUser(this.state.user_id).then(r=>{
            this.setState({user:r})
        }).catch(err=>{
            //TODO show error
        });
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
        const location = {latitude:null,longitude:null};
        if(this.state.user === undefined)
            return <LinearProgress />;
        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
            <Grid item xs={10} sm={10}>
                <Typography variant="h4" display="block" align={"left"} gutterBottom>
                    {t('user-my-posts')}
                </Typography>
            </Grid>
            <Grid container alignContent={"center"} justify={"center"} spacing={2}>
                <Grid item xs={10} sm={10}>
                    <Paper>
                        <Box p={2}>
                            <Grid container spacing={4} justify={"center"} alignContent={"flex-end"} alignItems={"flex-end"}>
                                <Grid item xs={10} sm={10}>
                                    <Typography variant="overline" display="block" gutterBottom>
                                        {t('posts')}
                                    </Typography>
                                    <PostProfile location={location} category={null} user_id={this.state.user_id}/>
                                </Grid>
                            </Grid>
                        </Box>
                    </Paper>
                </Grid>
            </Grid>
        </Grid>);
    }
}
PostOwn.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostOwn));