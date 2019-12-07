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
import {SessionService} from "../../services/SessionService";

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            <Link color="inherit" href="https://pawgram.org/">
                Pawgram
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

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
        const jwt = SessionService().getAccessToken();
        const user = SessionService().getUser();

        if (jwt && user){
            this.props.history.push('/ui/main');
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

        const as = RestService("/api/").createUser({"name": this.state.firstName,"surname": this.state.lastName,"mail": this.state.email,"password":this.state.password});
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
                    <form className={classes.form} noValidate autoComplete="off" onSubmit={e => this.submit(e)}>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    autoComplete="fname"
                                    name="firstName"
                                    variant="outlined"
                                    required
                                    fullWidth
                                    id="firstName"
                                    label={t("name")}
                                    autoFocus
                                    onChange={e => this.change(e)}
                                    value={this.state.firstName}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    variant="outlined"
                                    required
                                    fullWidth
                                    id="lastName"
                                    label={t("surname")}
                                    name="lastName"
                                    autoComplete="lname"
                                    onChange={e => this.change(e)}
                                    value={this.state.lastName}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    required
                                    fullWidth
                                    id="email"
                                    label={t("email")}
                                    name="email"
                                    autoComplete="email"
                                    onChange={e => this.change(e)}
                                    value={this.state.email}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    required
                                    fullWidth
                                    name="password"
                                    label={t("password")}
                                    type="password"
                                    id="password"
                                    autoComplete="current-password"
                                    onChange={e => this.change(e)}
                                    value={this.state.password}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined"
                                    required
                                    fullWidth
                                    name="repeatpassword"
                                    label={t("repeat-password")}
                                    type="password"
                                    id="repeatpassword"
                                    autoComplete="current-password"
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
                    </form>
                </div>
                <Box mt={5}>
                    <Copyright />
                </Box>
            </Container>
        );
    }
}

Register.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(Register));