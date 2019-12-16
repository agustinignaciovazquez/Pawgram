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
import {Redirect} from "react-router-dom";
import AddIcon from "@material-ui/icons/Add";
import {Config} from "../../services/Config";
import Paper from "@material-ui/core/Paper";
import Box from "@material-ui/core/Box";
import {Avatar} from "@material-ui/core";
import {RestService} from "../../services/RestService";
import PostLocation from "../Post/PostLocation/PostLocation";
import PostProfile from "../Post/PostProfile/PostProfile";
import LinearProgress from "@material-ui/core/LinearProgress";
import EditIcon from "@material-ui/icons/Edit";
import MailIcon from "@material-ui/icons/Mail";
const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
    biggerAvatar: {
        width: 175,
        height: 175,
    },
});

class Profile extends Component {

    constructor(props, context) {
        super(props, context);
        const user_id = props.match.params.id;
        this.state = {
            'user_id': user_id,
            'user':undefined,
            show_error:false
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        this.getUser();
    }

    getUser(){
        RestService().getUser(this.state.user_id).then(r=>{
            this.setState({user:r, show_error:false})
        }).catch(err=>{
            this.setState({show_error:true})
            if(err.response.status === 404){
                this.props.history.push('/404');
            }
        });
    }

    componentDidUpdate(prevProps,prevState){
        if(prevProps.match.params.id !== this.props.match.params.id){
            this.setState({user: undefined,user_id:this.props.match.params.id});
        }
        if(prevState.user_id !== this.state.user_id){
            this.getUser();
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

    renderButton(){
        const {classes, t} = this.props;
        let icon = <MailIcon />;
        let url = '/message/'+this.state.user.id;
        let type = 'message';
        const me = AuthService().getLoggedUser();
        console.log(this.state.user)
        if(me.id === this.state.user.id) {
            icon = <EditIcon/>;
            url = '/settings'
            type = 'edit'
        }
        return (<Button
            fullWidth
            variant="contained"
            color="primary"
            size="small"
            className={classes.button}
            startIcon={icon}
            onClick={e => {this.props.history.push(url)}}
        >
            {t(type)}
        </Button>)
    }

    render() {
        const { classes,t } =  this.props;
        const location = {latitude:null,longitude:null};

        if(this.state.user === undefined)
            return <LinearProgress />;

        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
                <Grid item xs={10} sm={10}>
                <Typography variant="h4" display="block" align={"left"} gutterBottom>
                    {t('user-profile')}
                </Typography>
            </Grid>
            <Grid container alignContent={"center"} justify={"center"} spacing={2}>
                <Grid item xs={10} sm={10}>
                    <Paper>
                        <Box p={2}>
                            <Grid container spacing={4} justify={"center"} alignContent={"flex-end"} alignItems={"flex-end"}>
                                <Grid item xs={9} sm={9}>
                                    <Grid item xs={12} sm={12} hidden={this.state.show_error === false}>
                                        <Typography color={"secondary"}>{t('error-server')}</Typography>
                                    </Grid>
                                    <Grid container spacing={2} alignItems={"flex-end"}>
                                        <Grid item xs={6} sm={6}>
                                            <Typography variant="overline" display="block" gutterBottom>
                                                {t('contact-name')}
                                            </Typography>
                                            <Typography variant="body1">
                                                {this.state.user.name}
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={6} sm={6}>
                                            <Typography variant="overline" display="block" gutterBottom>
                                                {t('surname')}
                                            </Typography>
                                            <Typography variant="body1">
                                                {this.state.user.surname}
                                            </Typography>
                                        </Grid>

                                        <Grid item xs={12} sm={12}>
                                            <Typography variant="overline" display="block" gutterBottom>
                                                {t('contact-mail')}
                                            </Typography>
                                            <Typography variant="body1">
                                                {this.state.user.email}
                                            </Typography>
                                        </Grid>
                                    </Grid>
                                </Grid>
                                <Grid item xs={3} sm={3}>
                                    <Avatar alt={this.state.user.name} src={this.state.user.profile_picture} className={classes.biggerAvatar} />
                                </Grid>
                                <Grid item xs={3} sm={3}>
                                    {this.renderButton()}
                                </Grid>
                                <Grid item xs={10} sm={10}>
                                    <Typography variant="overline" display="block" gutterBottom>
                                        {t('user-posts')}
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
Profile.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(Profile));