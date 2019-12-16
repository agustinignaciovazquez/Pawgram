import React, { Component } from 'react';
import {AuthService} from "../../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";
import PropTypes from "prop-types";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import {Config} from "../../../services/Config";
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
import CardMedia from "@material-ui/core/CardMedia";
import Card from "@material-ui/core/Card";
import CardHeader from "@material-ui/core/CardHeader";
import IconButton from '@material-ui/core/IconButton';
import RemoveIcon from '@material-ui/icons/Remove';

import {
    MuiPickersUtilsProvider,
    KeyboardTimePicker,
    KeyboardDatePicker,
} from '@material-ui/pickers';
import {Chip} from "@material-ui/core";

import GoogleMapsSearchPicker from "../../GoogleMaps/GoogleMapsSearchPicker";
import {RestService} from "../../../services/RestService";
import PostDeleteDialog from "./PostDeleteDialog";
import {Redirect} from "react-router-dom";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
    card: {
        maxWidth: 250,
    },
    media: {
        height: 0,
        paddingTop: '100%', // 16:9
    },
});

class PostABM extends Component {

    constructor(props, context) {
        super(props, context);
        let title = "", description = "", contact_phone = "", latitude="", longitude="", event_date=new Date();
        let pet = Config.PET[0], gender = Config.GENDER[0], modify = false, images_upload = [], post_id = null;

        let category = props.category;
        if(!category) {
            category = props.match.params.category;
        }
        category = category.toLowerCase();

        if(props.post){
            const post = props.post;
            modify = true;
            post_id = post.id;
            title = post.title;
            description = post.description;
            contact_phone = post.contact_phone;
            latitude = post.locationDTO.latitude;
            longitude = post.locationDTO.longitude;
            event_date = new Date(post.event_date);
            pet = post.pet;
            images_upload = post.image_urls.postImages;
            gender = (post.is_male)? Config.GENDER[0]:Config.GENDER[1];
        }

        this.state = {
            'post_id': post_id,
            'category': category,
            'title': title,
            'description': description,
            'contact_phone': contact_phone,
            'latitude': latitude,
            'longitude': longitude,
            'event_date': event_date,
            'pet': pet,
            'gender': gender,
            'images': [],
            'image_urls': images_upload,
            'marker': {'latitude': latitude, 'longitude': longitude},
            'show_error': false,
            'show_error_server': false,
            'post': props.post,
            'modify': modify,
            'redirectUrl': undefined,
            'submitting':false,
            'errors':[]
        };
    }

    componentDidMount() {
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

        return(<FormControl fullWidth variant="outlined" className={classes.formControl}>

            <Select
                labelId="demo-simple-select-outlined-label"
                id="demo-simple-select-outlined"
                value={this.state[name]}
                onChange={event => this.setState({[name]:event.target.value})}
                labelWidth={labelWidth}
            >
                {values.map((item,i) => {return <MenuItem key={i} value={item}>{t(item)}</MenuItem>})}
            </Select>
        </FormControl>);
    }

    handleMarker (m) {
        // Set new location
        this.setState({'marker':m, 'latitude': m.latitude, 'longitude': m.longitude});
    }

    renderImageInputs(){
        let table = [];
        const left_to_upload = Config.MAX_UPLOAD_IMAGE - this.state.image_urls.length;
        for(let i =0;i<left_to_upload;i++){
            table.push(<TextField key={i} type="file" onChange={this.onFileChange(i)} />)
        }
        return table;
    }

    removeImageHandler(r,i){
        r.then(res=>{
            const image_urls = this.state.image_urls;
            image_urls.splice(i, 1);
            this.setState({image_urls: image_urls});
        }).catch(err=> {
            this.setState({show_error:false, show_error_server:true});
        });
    }

    renderImages(){
        const {classes} = this.props;
        let table = [];

        this.state.image_urls.forEach(
            (value,i) => {
                table.push(
                    <Grid key={i} item xs={4} sm={4}>
                    <Card className={classes.card} >
                        <CardHeader
                            action={
                                <PostDeleteDialog post_id={this.state.post_id} image_id={value['postImageId']}
                                                  callback={(e) => {this.removeImageHandler(e,i)}}/>
                            }
                            />
                        <CardMedia
                    className={classes.media}
                    image={value['url']}
                    title="Paella dish"
                />
                    </Card>
                    </Grid>)
            }
        )
        return table;
    }
    renderServerErrors(){
        const {t} = this.props;
        const {errors} = this.state;
        let r = []
        if(errors && errors.length === 0) {

            return <Typography component="h1" variant="h5" color={"secondary"} hidden={!this.state.show_error_server}>
                { t('error-server') }
            </Typography>

        } else {
            errors.map(item => {
                r.push(<Typography component="h1" variant="h5" color={"secondary"} hidden={!this.state.show_error_server}>
                    { t(item.violation)}
                </Typography>)
            })
        }
        return r;
    }

    submitPost(e){
        e.preventDefault();
        let req;
        const postData = {title: this.state.title,
            description: this.state.description,
            contact_phone: this.state.contact_phone,
            event_date: this.state.event_date,
            category: this.state.category,
            pet: this.state.pet,
            is_male: (this.state.gender === Config.GENDER[0]),
            latitude: this.state.latitude,
            longitude: this.state.longitude,
            images: this.state.images,
            submitting:true,
        };

        if(this.state.modify)
            req = RestService().modifyPost(this.state.post_id, postData);
        else
            req = RestService().createPost(postData);

        req.then(
            r=>{
                this.setState({show_error:false,show_error_server:false, redirectUrl: '/post/'+r.id});
            }
        ).catch( err=>{
            const errors = (err.response.status !== 500 && err.response.data.errors)? err.response.data.errors:[];
            this.setState({show_error:false,show_error_server:true, errors: errors});
        });
    }

    render() {
        const { classes,t } =  this.props;
        const { category } = this.state;

        if(this.state.redirectUrl){
            const redirectUrl = this.state.redirectUrl;
            return ( <Redirect to={redirectUrl} />);
        }

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
                    onError={errors => {console.log(errors);this.setState({show_error:true,submitting:false})}}
                >
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={12}>
                            <Typography variant="h4" align={"right"} gutterBottom>
                                <Chip
                                    label={t(category)}
                                    variant="outlined"
                                />
                            </Typography>
                            <Typography component="h1" variant="h5" color={"secondary"} hidden={!this.state.show_error}>
                                {t('complete-all-fields')}
                            </Typography>
                            <Typography component="h1" variant="h5" color={"secondary"} hidden={!this.state.show_error_server}>
                                {this.renderServerErrors()}
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
                        <Grid item xs={12} sm={9}>
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

                        <Grid item xs={12} sm={3} hidden={this.state.category.toLowerCase() === 'adopt'}>
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
                            <Typography variant="overline" display="block" gutterBottom>
                                {t('pet')}
                            </Typography>
                            {this.renderSelect('pet',Config.PET)}
                        </Grid>

                        <Grid item xs={12} sm={6}>
                            <Typography variant="overline" display="block" gutterBottom>
                                {t('gender')}
                            </Typography>
                            {this.renderSelect('gender',Config.GENDER)}
                        </Grid>

                        <Grid item xs={4} sm={4} hidden={Config.MAX_UPLOAD_IMAGE - this.state.image_urls.length <= 0}>
                            <Typography variant="overline" display="block" gutterBottom>
                                {t('upload-images')}
                            </Typography>
                            {this.renderImageInputs()}
                        </Grid>

                        <Grid item xs={8} sm={8} hidden={this.state.image_urls.length === 0}>
                            <Typography variant="overline" display="block" gutterBottom>
                                {t('uploaded-images')}
                            </Typography>
                            <Grid container spacing={2}>
                                {this.renderImages()}
                            </Grid>
                        </Grid>

                        <Grid item xs={12} sm={12} style={{position: 'relative', height: '70vh'}}>
                            <Typography variant="overline" display="block" gutterBottom>
                                {t('location')}
                            </Typography>
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

                    </Grid>
                    <Button
                        type="submit"
                        fullWidth
                        disabled={this.state.submitting}
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