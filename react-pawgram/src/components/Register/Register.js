import React, {Component} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { withStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import { withTranslation } from 'react-i18next';
import {Link as LinkDom} from "react-router-dom";
import PropTypes from "prop-types";
import {RestService} from "../../services/RestService";
import {AuthService} from "../../services/AuthService";
import { ValidatorForm, TextValidator} from 'react-material-ui-form-validator';
import {ValidateEmail,PasswordMatchValidation,DuplicateMailValidation} from "../../services/Utils";
import {Copyright} from "../../services/Utils";

const styles = theme => ({
    paper: {
        marginTop: theme.spacing(8),
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
        marginTop: theme.spacing(3),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
});

//TODO put this function in utils

class Register extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            'firstName': "",
            'lastName': "",
            'email': "",
            'password': "",
            'repeatpassword':"",
        };

        this.change = this.change.bind(this);
        this.submit = this.submit.bind(this);
    }

    componentDidMount() {
        if (AuthService().isLoggedIn()){
            this.props.history.push('/ui/main');
        }
        //TODO put this function in utils
        ValidatorForm.addValidationRule('isPasswordMatch', PasswordMatchValidation(this));
        ValidatorForm.addValidationRule('isDuplicateMail', DuplicateMailValidation);
    }

    componentWillUnmount() {
        // remove rule when it is not needed
        ValidatorForm.removeValidationRule('isPasswordMatch');
        ValidatorForm.removeValidationRule('isDuplicateMail');
    }

    change(e){
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    submit(e){
        e.preventDefault();

        const data = {"name": this.state.firstName,"surname": this.state.lastName,"mail": this.state.email, "password":this.state.password};

        RestService().createUser(data)
            .then(r => {
                AuthService(this.props).logIn(data.mail, data.password,true)
                    .then(response =>{
                    this.props.callback(response);
                    this.props.history.push('/main');
                }).catch(res =>{
                    //Show error
                    console.log(res);
                })
            }).catch(response => {
                console.log(response);
                //Show error
        });
    }

    render() {
        const { classes, t } = this.props;
        return (
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <div className={classes.paper}>
                    <Avatar className={classes.avatar}>
                        <LockOutlinedIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        {t("registermain")}
                    </Typography>
                    <ValidatorForm
                        className={classes.form}
                        autoComplete="off"
                        ref="form"
                        onSubmit={e => this.submit(e)}
                        onError={errors => console.log(errors)}
                    >
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={6}>
                                <TextValidator
                                    autoComplete="fname"
                                    name="firstName"
                                    variant="outlined"
                                    fullWidth
                                    id="firstName"
                                    label={t("name")}
                                    autoFocus
                                    onChange={e => this.change(e)}
                                    validators={['required','matchRegexp:^.{3,50}$','matchRegexp:^[a-z,A-Z,á,é,í,ó,ú,â,ê,ô,ã,õ,ç,Á,É,Í,Ó,Ú,Â,Ê,Ô,Ã,Õ,Ç,ü,ñ,Ü,Ñ," "]{3,50}$']}
                                    errorMessages={[t('name-required'), t('name-short'), t('name-invalid')]}
                                    value={this.state.firstName}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextValidator
                                    variant="outlined"
                                    fullWidth
                                    id="lastName"
                                    label={t("surname")}
                                    name="lastName"
                                    autoComplete="lname"
                                    validators={['required','matchRegexp:^.{3,50}$','matchRegexp:^[a-z,A-Z,á,é,í,ó,ú,â,ê,ô,ã,õ,ç,Á,É,Í,Ó,Ú,Â,Ê,Ô,Ã,Õ,Ç,ü,ñ,Ü,Ñ," "]{3,50}$']}
                                    errorMessages={[t('name-required'), t('name-short'), t('name-invalid')]}
                                    onChange={e => this.change(e)}
                                    value={this.state.lastName}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextValidator
                                    variant="outlined"
                                    fullWidth
                                    id="email"
                                    label={t("email")}
                                    name="email"
                                    autoComplete="email"
                                    validators={['required', 'isEmail','isDuplicateMail']}
                                    errorMessages={[t('email-required'), t('email-invalid'), t('duplicate-mail')]}
                                    onChange={e => this.change(e)}
                                    value={this.state.email}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextValidator
                                    variant="outlined"
                                    fullWidth
                                    name="password"
                                    label={t("password")}
                                    type="password"
                                    id="password"
                                    autoComplete="current-password"
                                    validators={['matchRegexp:^.{6,60}$', 'required']}
                                    errorMessages={[t('password-short'), t('password-required')]}
                                    onChange={e => this.change(e)}
                                    value={this.state.password}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextValidator
                                    variant="outlined"
                                    fullWidth
                                    name="repeatpassword"
                                    label={t("repeat-password")}
                                    type="password"
                                    id="repeatpassword"
                                    autoComplete="current-password"
                                    validators={['isPasswordMatch', 'required']}
                                    errorMessages={[t('password-mismatch'), t('password-required')]}
                                    onChange={e => this.change(e)}
                                    value={this.state.repeatpassword}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <FormControlLabel
                                    control={<Checkbox value="allowExtraEmails" color="primary" />}
                                    label={t("registercheckbox")}
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            className={classes.submit}
                        >
                            {t("register")}
                        </Button>
                        <Grid container justify="flex-end">
                            <Grid item>
                                <Link component={ LinkDom } to="/login" variant="body2">
                                    {t("alredyregister")}
                                </Link>
                            </Grid>
                        </Grid>
                    </ValidatorForm>
                </div>

            </Container>
        );
    }
}

Register.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(Register));