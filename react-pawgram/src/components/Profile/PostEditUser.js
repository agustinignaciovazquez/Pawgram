import React from 'react'
import { Redirect } from 'react-router';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import NoSsr from '@material-ui/core/NoSsr';
import Tab from '@material-ui/core/Tab';
import Grid from "@material-ui/core/Grid/Grid";
import Typography from '@material-ui/core/Typography';
import Button from "@material-ui/core/Button/Button";
import { ValidatorForm, TextValidator} from 'react-material-ui-form-validator';
import {red} from '@material-ui/core/colors';
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogActions from "@material-ui/core/DialogActions";
import Dialog from "@material-ui/core/Dialog";
import TextField from "@material-ui/core/TextField";
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import IconButton from '@material-ui/core/IconButton';
import InputAdornment from '@material-ui/core/InputAdornment';
import Fab from "@material-ui/core/Fab";
import AddIcon from '@material-ui/icons/Add';
import MinusIcon from '@material-ui/icons/Remove';
import {AuthService} from "../../services/AuthService";
import {withTranslation} from "react-i18next";
import {DuplicateMailValidation, PasswordMatchValidation} from "../../services/Utils";
import {RestService} from "../../services/RestService";
import {SessionService} from "../../services/SessionService";
import {Avatar} from "@material-ui/core";

function TabContainer(props) {
    return (
        <Typography component="div" style={{ padding: 8 * 3 }}>
            {props.children}
        </Typography>
    );
}

TabContainer.propTypes = {
    children: PropTypes.node.isRequired,
};

function LinkTab(props) {
    return <Tab component="a" onClick={event => event.preventDefault()} {...props} />;
}

const styles = theme => ({
    root: {
        flexGrow: 1,
        backgroundColor: theme.palette.background.paper,
    },
    container: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    textField: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,
    },
    dense: {
        marginTop: 16,
    },
    menu: {
        width: 200,
    },
    biggerAvatar: {
        width: 175,
        height: 175,
    },
});

class PostEditUser extends React.Component {
    constructor(props) {
        super(props);
        const user = AuthService().getLoggedUser();

        this.state ={
            user: user,
            name: user.name,
            surname: user.surname,
            mail: user.email,
            old_password: "",
            password: "",
            new_password2: "",
            error: false,
            error2: false,
            error3: false,
            error_detail: "",
            error2_detail: "",
            error3_detail: "",
            modified: null,
            modified2: null,
            modified3: null,
            openDialogPasswordConfirm: false,
            value: 0,
            showPassword: false,
            showPassword1: false,
            showPassword2: false,
            showPasswordConfirm: false,
            profile_pic: null,
        };

        this.onFormSubmitEdit = this.onFormSubmitEdit.bind(this);
        this.onFormSubmitPassword = this.onFormSubmitPassword.bind(this);
        this.onFormSubmitProfile = this.onFormSubmitProfile.bind(this);
        this.onChange = this.onChange.bind(this);
    }
    componentDidMount() {
        ValidatorForm.addValidationRule('isPasswordMatch', PasswordMatchValidation(this));
        ValidatorForm.addValidationRule('isDuplicateMail', DuplicateMailValidation);

    }

    componentWillUnmount() {
        // remove rule when it is not needed
        // remove rule when it is not needed
        ValidatorForm.removeValidationRule('isPasswordMatch');
        ValidatorForm.removeValidationRule('isDuplicateMail');
    }

    onFormSubmitEdit(e){
        e.preventDefault(); // Stop form submit
        this.setState({openDialogPasswordConfirm:true});
    }

    onFormSubmitPassword(e){
        e.preventDefault(); // Stop form submit

        RestService().changePassword(this.state.old_password,this.state.password).then(r=>{
            this.setState({'modified2': true, 'error2': false, 'password': "", 'old_password': "", 'new_password2': ""});
        }).catch(err=>{
            this.setState({'modified2': null, 'error2': true, 'error2_detail': this.renderError(err.response)});
        });
    }

    onFormSubmitProfile(e){
        e.preventDefault(); // Stop form submit
        console.log(this.state);
        const data = {picture: this.state.profile_pic};

        RestService().changeProfilePicture(data).then(r=>{
            this.setState({'modified3': true, 'error3': false});
            RestService().getUser(this.state.user.id).then( r=>{
                SessionService().setUser(r, SessionService().isLocalSaved());
                this.setState({user:r});
            }).catch(err=>{

            });
        }).catch(err=>{
            this.setState({'modified3': null, 'error3': true, 'error3_detail': this.renderError(err.response)});
        });
    }

    editUser(){
        const data = {
            name: this.state.name,
            surname: this.state.surname,
            mail: this.state.mail,
            password: this.state.old_password,
        };

        this.handleClosePasswordConfirm();
        RestService().changeInfo(data).then(r=>{
            this.setState({'modified': true, 'error': false});
            RestService().getUser(this.state.user.id).then( r=>{
                SessionService().setUser(r, SessionService().isLocalSaved());
                this.setState({user:r});
            }).catch(err=>{

            });
        }).catch(err => {
            this.setState({'modified': null, 'error': true, 'error_detail': this.renderError(err.response)});
        })
    }

    renderError(response){
        const {t} = this.props;
        if(!response){
            return t('error-server')
        }
        if(response.status === 422){
            return t('incorrect-password');
        }
        return t('error-server')
    }

    onChange(e) {
        const file_input = e.target.files[0];
        this.setState({file:file_input});
    }

    handleChangeText = name => event => {
        this.setState({[name]: event.target.value});
    };

    handleClosePasswordConfirm = () => {
        this.setState({ openDialogPasswordConfirm: false });
    };

    handleClickShowPassword = (state_password) => () => {
        this.setState({[state_password]: !this.state[state_password]});
    };

    handleChange = (event, value) => {
        this.setState({ value });
    };

    renderUploadPhoto(){
        const {classes,t} = this.props;
        return ( <ValidatorForm
            instantValidate={false}
            ref="form" onSubmit={this.onFormSubmitProfile} onError={errors => console.log(errors)}>
            <Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
            <Grid item xs={12} sm={12}>
                { (this.state.modified3 !== null) && <h3 color={"primary"}> {t('update-success')}</h3>}
                {this.state.error3 && <h3 style={{color: 'red'}}> {this.state.error_detail3}</h3>}
            </Grid>
            <Grid item xs={3} sm={3}>
                <Avatar alt={this.state.user.name} src={this.state.user.profile_picture} className={classes.biggerAvatar} />
            </Grid>
            <Grid item xs={12} sm={12}/>
            <Grid item xs={4} sm={4}>
                <TextField required type="file" onChange={this.onFileChange} />
            </Grid>
            <Grid item xs={12} sm={12}/>
            <Grid item xs={4} sm={4}>
                <Button fullWidth variant="contained" color="primary" type="submit">{t('save')}</Button>
            </Grid>
            </Grid></ValidatorForm>);
    }

    onFileChange = (e) => {
        const file_input = e.target.files[0];
        this.setState({'profile_pic':  file_input});
    };

    renderEditUser(){
        const user = this.state.user;
        const error = this.state.error;
        const { classes,t } = this.props;
        return( <ValidatorForm
            instantValidate={false}
            ref="form" onSubmit={this.onFormSubmitEdit} onError={errors => console.log(errors)}>
            <Grid container spacing={2}>
                <Grid item xs={12} sm={12}>
                    { (this.state.modified !== null) && <h3 color={"primary"}> {t('update-success')}</h3>}
                    {error && <h3 style={{color: 'red'}}> {this.state.error_detail}</h3>}
                </Grid>

                <Grid item xs={6} sm={6}>
                    <TextValidator
                        fullWidth
                        autoFocus
                        id="outlined-uncontrolled"
                        label={t("name")}
                        value={this.state.name}
                        onChange={this.handleChangeText('name')}
                        margin="normal"
                        variant="outlined"
                        validators={['required','matchRegexp:^.{3,50}$','matchRegexp:^[a-z,A-Z,á,é,í,ó,ú,â,ê,ô,ã,õ,ç,Á,É,Í,Ó,Ú,Â,Ê,Ô,Ã,Õ,Ç,ü,ñ,Ü,Ñ," "]{3,50}$']}
                        errorMessages={[t('name-required'), t('name-short'), t('name-invalid')]}/>
                </Grid>

                <Grid item xs={6} sm={6}>
                    <TextValidator
                        fullWidth
                        autoFocus
                        id="outlined-uncontrolled"
                        label={t("surname")}
                        value={this.state.surname}
                        onChange={this.handleChangeText('surname')}
                        margin="normal"
                        variant="outlined"
                        validators={['required','matchRegexp:^.{3,50}$','matchRegexp:^[a-z,A-Z,á,é,í,ó,ú,â,ê,ô,ã,õ,ç,Á,É,Í,Ó,Ú,Â,Ê,Ô,Ã,Õ,Ç,ü,ñ,Ü,Ñ," "]{3,50}$']}
                        errorMessages={[t('name-required'), t('name-short'), t('name-invalid')]}/>
                </Grid>



                <Grid item xs={12} sm={12}>
                    <TextValidator
                        fullWidth
                        autoFocus
                        disabled
                        id="outlined-uncontrolled"
                        label={t("mail")}
                        value={this.state.mail}
                        onChange={this.handleChangeText('mail')}
                        margin="normal"
                        variant="outlined"
                        validators={['required', 'isEmail']}
                        errorMessages={[t('email-required'), t('email-invalid') ]}
                        />
                </Grid>


                <Grid item xs={12} sm={12}>
                    <Button fullWidth variant="contained" color="primary" type="submit">{t('save')}</Button>
                </Grid>
            </Grid>
            <Dialog
                open={this.state.openDialogPasswordConfirm}
                onClose={()=>{this.handleClosePasswordConfirm()}}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description">
                <DialogTitle id="alert-dialog-title">{t('password-confirm')}</DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        <TextField fullWidth type={this.state.showPasswordConfirm ? 'text' : 'password'} id="outlined-uncontrolled" label={t('password')} value={this.state.old_password} onChange={this.handleChangeText('old_password')}  margin="normal" variant="outlined" InputProps={{
                            endAdornment: (
                                <InputAdornment position="end">
                                    <IconButton
                                        aria-label={t('password-visible')}
                                        onClick={this.handleClickShowPassword('showPasswordConfirm')}
                                    >
                                        {this.state.showPasswordConfirm ? <VisibilityOff /> : <Visibility />}
                                    </IconButton>
                                </InputAdornment>
                            ),
                        }}/>
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    {this.state.old_password !== "" && <Button onClick={()=>{this.editUser()}} color="primary">
                        {t('accept')}
                    </Button>}
                    <Button onClick={()=>{this.handleClosePasswordConfirm()}} color="primary" autoFocus>
                        {t('disagree')}
                    </Button>
                </DialogActions>
            </Dialog>
        </ValidatorForm>);
    }

    renderEditPassword(){
        const error = this.state.error2;
        const {t} = this.props;
        return(<ValidatorForm ref="form" onSubmit={this.onFormSubmitPassword} onError={errors => console.log(errors)}>
            <Grid container spacing={24}>
                <Grid item xs={12} sm={12}>
                    { (this.state.modified2 !== null) && <h3 color={"primary"}> {t('update-success')}</h3>}
                    {error && <h3 style={{color: 'red'}}> {this.state.error2_detail}</h3>}
                </Grid>
                <Grid item xs={12} sm={12}>
                    <TextValidator fullWidth type={this.state.showPassword ? 'text' : 'password'} id="outlined-uncontrolled" label={t('old-password')} value={this.state.old_password} onChange={this.handleChangeText('old_password')}  margin="normal" variant="outlined" validators={['required']} errorMessages={[t('password-required')]} InputProps={{
                        endAdornment: (
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="Toggle password visibility"
                                    onClick={this.handleClickShowPassword('showPassword')}
                                >
                                    {this.state.showPassword ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        ),
                    }}/>
                </Grid>

                <Grid item xs={12} sm={12}>
                    <TextValidator fullWidth
                                   type={this.state.showPassword1 ? 'text' : 'password'}
                                   id="outlined-uncontrolled"
                                   label={t('new-password')}
                                   value={this.state.password}
                                   onChange={this.handleChangeText('password')}
                                   margin="normal"
                                   variant="outlined"
                                   validators={['matchRegexp:^.{6,60}$', 'required']}
                                   errorMessages={[t('password-short'), t('password-required')]}
                                   InputProps={{
                        endAdornment: (
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="Toggle password visibility"
                                    onClick={this.handleClickShowPassword('showPassword1')}
                                >
                                    {this.state.showPassword1 ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        ),
                    }}/>
                </Grid>

                <Grid item xs={12} sm={12}>
                    <TextValidator fullWidth
                                   type={this.state.showPassword2 ? 'text' : 'password'}
                                   id="outlined-uncontrolled"
                                   label={t('new-repeat-password')}
                                   value={this.state.new_password2}
                                   onChange={this.handleChangeText('new_password2')}
                                   margin="normal"
                                   variant="outlined"
                                   validators={['isPasswordMatch', 'required']}
                                   errorMessages={[t('password-mismatch'), t('password-required')]}
                                   InputProps={{
                        endAdornment: (
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="Toggle password visibility"
                                    onClick={this.handleClickShowPassword('showPassword2')}
                                >
                                    {this.state.showPassword2 ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        ),
                    }}/>
                </Grid>

                <Grid item xs={12} sm={12}>
                    <Button fullWidth variant="contained" color="primary" type="submit">{t('change-password-s')}</Button>
                </Grid>
            </Grid>
        </ValidatorForm>);
    }

    render() {

        const { classes,t } = this.props;

        const user = this.state.user;
        const { value , error} = this.state;

        return (
            <div className={classes.root}>
                <AppBar position="static" color="default">
                    <Tabs variant="fullWidth" indicatorColor="primary" value={value} onChange={this.handleChange}>
                        <LinkTab label={t("change-info")} href="page1" />
                        <LinkTab label={t("change-password")} href="page2" />
                        <LinkTab label={t("change-profile-pic")} href="page3" />
                    </Tabs>
                </AppBar>
                {value === 0 && <TabContainer>{this.renderEditUser()}</TabContainer>}
                {value === 1 && <TabContainer>{this.renderEditPassword()}</TabContainer>}
                {value === 2 && <TabContainer>{this.renderUploadPhoto()}</TabContainer>}
            </div>
        )
    }
}

PostEditUser.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostEditUser));