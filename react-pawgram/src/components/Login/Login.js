import React, {Component} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Link from '@material-ui/core/Link';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { Link as LinkDom} from 'react-router-dom';
import { withStyles } from '@material-ui/core/styles';
import { withTranslation } from 'react-i18next';
import {AuthService} from "../../services/AuthService";
import {SessionService} from "../../services/SessionService";
import PropTypes from 'prop-types';
import LoginBackgroundImage from '../../resources/images/login_bg.jpg'
import { ValidatorForm, TextValidator} from 'react-material-ui-form-validator';
import {Copyright} from "../../services/Utils";

const styles = theme => ({
    root: {
        height: '100vh',
    },
    image: {
        backgroundImage: `url(${LoginBackgroundImage})`,
        backgroundRepeat: 'no-repeat',
        backgroundColor:
            theme.palette.type === 'dark' ? theme.palette.grey[900] : theme.palette.grey[50],
        backgroundSize: 'cover',
        backgroundPosition: 'center',
    },
    paper: {
        margin: theme.spacing(8, 4),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(1),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
});

class Login extends Component {
    static contextTypes = {
        router: PropTypes.object
    };

    constructor(props, context) {
        super(props, context);
        this.state = {
            'email': "",
            'password': "",
            'remember':false,
            'show_error':false
        };
        this.change = this.change.bind(this);
        this.submit = this.submit.bind(this);
    }

    componentDidMount() {
        if (SessionService().isLoggedIn()){
            this.props.history.push('/main');
        }
    }

    change(e){
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    submit(e){
        e.preventDefault();
        console.log(this.state.email);
        const authService = AuthService(this.props);
        //TODO saveToSession with remember me
        authService.logIn(this.state.email,this.state.password,this.state.remember)
        .then(r =>{
            this.setState({'show_error': false});
            this.props.callback(r);
            this.props.history.push('/main')
        })
        .catch(e =>{
            console.log("ERROR Loggin In");
            this.setState({'show_error': true});
            if(e.response && e.response.status === 401){

            }
        })
    }
    toggleRemember(){
        const r = !this.state.remember;
        this.setState({remember:r})
    }
    render(){
        const { classes, t } = this.props;

        return (
            <Grid container component="main" className={classes.root}>
                <CssBaseline/>
                <Grid item xs={false} sm={4} md={7} className={classes.image}/>
                <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
                    <div className={classes.paper}>
                        <Avatar className={classes.avatar}>
                            <LockOutlinedIcon/>
                        </Avatar>
                        <Typography component="h1" variant="h5">
                            Sign in
                        </Typography>
                        <Typography component="h1" variant="h5" color={"secondary"} hidden={!this.state.show_error}>
                            {t('error-login')}
                        </Typography>
                        <ValidatorForm className={classes.form} noValidate autoComplete="off" onSubmit={e => this.submit(e)} onError={errors => console.log(errors)}>
                            <TextValidator
                                variant="outlined"
                                margin="normal"
                                required
                                fullWidth
                                id="email"
                                label={t('email')}
                                name="email"
                                autoComplete="email"
                                autoFocus
                                onChange={e => this.change(e)}
                                value={this.state.email}
                                validators={['required', 'isEmail']}
                                errorMessages={[t('email-required'), t('email-invalid')]}
                            />
                            <TextValidator
                                variant="outlined"
                                margin="normal"
                                required
                                fullWidth
                                name="password"
                                label={t('password')}
                                type="password"
                                id="password"
                                autoComplete="current-password"
                                onChange={e => this.change(e)}
                                value={this.state.password}
                                validators={['required']}
                                errorMessages={[t('password-required')]}
                            />
                            <FormControlLabel
                                control={<Checkbox value="remember" color="primary"/>}
                                onChange={event => {this.toggleRemember()}}
                                label={t('remember')}
                            />
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                color="primary"
                                className={classes.submit}
                            >
                                {t('login')}
                            </Button>
                            <Grid container>
                                <Grid item xs>
                                    <Link href="#" variant="body2">
                                        {t('forgetpass')}
                                    </Link>
                                </Grid>
                                <Grid item>

                                    <Link component={ LinkDom } to="/register" variant="body2">

                                        {t('register')}

                                    </Link>

                                </Grid>
                            </Grid>

                        </ValidatorForm>
                    </div>
                </Grid>
            </Grid>
        );
    }
}

Login.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(Login));