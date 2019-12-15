import React, { Component } from 'react';
import {AuthService} from "../../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";
import SearchIcon from "@material-ui/icons/Search"
import InputAdornment from '@material-ui/core/InputAdornment';
import PropTypes from "prop-types";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import {Config} from "../../../services/Config";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import Link from "@material-ui/core/Link";
import {Link as LinkDom} from "react-router-dom";
import Box from '@material-ui/core/Box';
import Paper from "@material-ui/core/Paper";
import { ValidatorForm, TextValidator} from 'react-material-ui-form-validator';
import TextField from "@material-ui/core/TextField";
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import DateFnsUtils from '@date-io/date-fns';
import {
    MuiPickersUtilsProvider,
    KeyboardTimePicker,
    KeyboardDatePicker,
} from '@material-ui/pickers';
import {Chip} from "@material-ui/core";


import 'react-google-places-autocomplete/dist/assets/index.css';
import GoogleMapsSearchPicker from "../../GoogleMaps/GoogleMapsSearchPicker";
import {RestService} from "../../../services/RestService";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});
const mapStyles = {
    width: '100%',
    height: '400px',
};
class PostABM extends Component {

    constructor(props, context) {
        super(props, context);
        const category = props.match.params.category.toLowerCase();
        this.state = {
            'category': category,
            'title': "",
            'description':"",
            'contact_phone':"",
            'latitude':"",
            'longitude':"",
            'event_date': new Date(),
            'pet': Config.PET[0],
            'gender': Config.GENDER[0],
            'images': [],
            'labelWidth': 0,
            'show_error': false,
            'show_error_server': false,
            'marker': undefined,
        };
    }

    componentDidMount() {
        console.log(this.state.category);

        if (!AuthService().isLoggedIn())
            this.props.history.push('/login');

        if(!Config.CATEGORIES.includes(this.state.category))
            this.props.history.push('/');

    }

    change(e){
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    onFileChange = (i) => (e) => {
        const file_input = e.target.files[0];
        let images = this.state.images;
        images[i] = file_input;
        this.setState({images:  images});
    };

    renderSelect(name, values){
        const { classes,t } =  this.props;
        const {labelWidth} = this.state;
        let inputLabel;
        return(<FormControl fullWidth variant="outlined" className={classes.formControl}>
            <InputLabel ref={inputLabel} id="demo-simple-select-outlined-label">
                {t(name)}
            </InputLabel>
            <Select
                labelId="demo-simple-select-outlined-label"
                id="demo-simple-select-outlined"
                value={this.state[name]}
                onChange={event => this.setState({[name]:event.target.value})}
                labelWidth={labelWidth}
            >
                {values.map(item => {return <MenuItem value={item}>{t(item)}</MenuItem>})}
            </Select>
        </FormControl>);
    }

    handleMarker (m) {
        // Set new location
        this.setState({'marker':m, 'latitude': m.latitude, 'longitude': m.longitude});
    }

    renderImageInputs(){
        let table = [];
        for(let i =0;i<Config.MAX_UPLOAD_IMAGE;i++){
            table.push(<TextField type="file" onChange={this.onFileChange(i)} />)
        }
        return table;
    }

    submitPost(e){
        e.preventDefault();

        const postData = {title: this.state.title,
            description: this.state.description,
            contact_phone: this.state.contact_phone,
            event_date: this.state.event_date,
            category: this.state.category,
            pet: this.state.pet,
            is_male: (this.state.gender === Config.GENDER[0]),
            latitude: this.state.latitude,
            longitude: this.state.longitude,
            images: this.state.images};

        RestService().createPost(postData).then(
            r=>{
                this.props.history.push('/post/'+r.id);
            }
        ).catch( r=>{
            console.log(r);
            this.setState({show_error:false,show_error_server:true});
        })
    }

    render() {
        const { classes,t } =  this.props;
        const category = this.props.match.params.category;

        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"} spacing={2}>
            <Grid item xs={10} sm={10}>
                <Typography variant="h4" gutterBottom>
                    {t('create-post')}
                </Typography>
            </Grid>
            <Grid item xs={10} sm={10}>
                <Paper>
                    <Box p={2}>

                <ValidatorForm
                    className={classes.form}
                    autoComplete="off"
                    ref="form"
                    onSubmit={e => this.submitPost(e)}
                    onError={errors => {console.log(errors);this.setState({show_error:true})}}
                >
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={12}>
                            <Typography variant="h4" align={"right"} gutterBottom>
                                <Chip
                                    label={t(this.state.category)}
                                    variant="outlined"
                                />
                            </Typography>
                            <Typography component="h1" variant="h5" color={"secondary"} hidden={!this.state.show_error}>
                                {t('complete-all-fields')}
                            </Typography>
                            <Typography component="h1" variant="h5" color={"secondary"} hidden={!this.state.show_error_server}>
                                {t('error-server')}
                            </Typography>
                        </Grid>
                        <Grid item xs={12} sm={12}>
                            <TextValidator
                                autoComplete="fname"
                                name="title"
                                variant="outlined"
                                fullWidth
                                id="title"
                                label={t("title")}
                                autoFocus
                                onChange={e => this.change(e)}
                                validators={['minStringLength:3','maxStringLength:64']}
                                errorMessages={[t('title-min-str-length'), t('title-max-str-length')]}
                                value={this.state.title}
                            />

                        </Grid>
                        <Grid item xs={12} sm={12}>
                            <TextValidator
                                name="description"
                                variant="outlined"
                                fullWidth
                                multiline
                                rows={4}
                                rowsMax={4}
                                id="description"
                                label={t("description")}
                                onChange={e => this.change(e)}
                                validators={['minStringLength:5','maxStringLength:2048']}
                                errorMessages={[t('description-min-str-length'),t('description-max-str-length')]}
                                value={this.state.description}
                            />

                        </Grid>
                        <Grid item xs={12} sm={8}>
                            <TextValidator
                                autoComplete="phone"
                                name="contact_phone"
                                variant="outlined"
                                fullWidth
                                id="contact_phone"
                                label={t("contact-phone")}
                                onChange={e => this.change(e)}
                                validators={['minStringLength:5','maxStringLength:32','matchRegexp:^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$']}
                                errorMessages={[t('contact-min-str-length'),t('contact-max-str-length'),t('contact-not-valid')]}
                                value={this.state.contact_phone}
                            />
                        </Grid>
                        <Grid item xs={12} sm={4}>
                            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                <Grid container justify="flex-end">
                                    <KeyboardDatePicker
                                        disableToolbar
                                        inputVariant={"outlined"}
                                        variant="outlined"
                                        format="MM/dd/yyyy"
                                        id="date-picker-inline"
                                        label={t('event-date')}
                                        value={this.state.event_date}
                                        onChange={date => {this.setState({'event_date':date})}}
                                        KeyboardButtonProps={{
                                            'aria-label':t('change-date'),
                                        }}
                                    />
                                </Grid>
                            </MuiPickersUtilsProvider>

                        </Grid>
                        <Grid item xs={12} sm={6}>
                            {this.renderSelect('pet',Config.PET)}
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            {this.renderSelect('gender',Config.GENDER)}
                        </Grid>
                        <Grid item xs={12} sm={12}>
                            {this.renderImageInputs()}
                        </Grid>
                        <Grid item xs={12} sm={12} style={{position: 'relative', height: '50vh'}}>
                            <GoogleMapsSearchPicker marker={this.state.marker} callback={(m) => this.handleMarker(m)}/>
                            <br/>
                        </Grid>
                        <Grid item xs={12} sm={6} hidden={true}>

                            <TextValidator
                                name="latitude"
                                variant="outlined"
                                id="latitude"
                                label={t("latitude")}
                                onChange={e => this.change(e)}
                                validators={['required']}
                                errorMessages={[t('no-location')]}
                                value={this.state.latitude}
                            />
                            <TextValidator
                                name="longitude"
                                variant="outlined"
                                id="longitude"
                                label={t("longitude")}
                                onChange={e => this.change(e)}
                                validators={['required']}
                                errorMessages={[t('no-location')]}
                                value={this.state.longitude}
                            />
                        </Grid>

                        <Grid item xs={12} sm={12} />
                        <Grid item xs={12} sm={12} />


                    </Grid>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.submit}
                    >
                        {t("create-post")}
                    </Button>
                </ValidatorForm>
                </Box>
                </Paper>
            </Grid>
        </Grid>);
    }
}
PostABM.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostABM));