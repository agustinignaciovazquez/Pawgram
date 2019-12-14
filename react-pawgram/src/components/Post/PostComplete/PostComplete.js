import React, {Component} from 'react';
import { makeStyles } from '@material-ui/core/styles/index';
import Card from '@material-ui/core/Card/index';
import CardActionArea from '@material-ui/core/CardActionArea/index';
import CardHeader from '@material-ui/core/CardHeader';
import CardActions from '@material-ui/core/CardActions/index';
import CardContent from '@material-ui/core/CardContent/index';
import CardMedia from '@material-ui/core/CardMedia/index';
import Paper from "@material-ui/core/Paper";
import Button from '@material-ui/core/Button/index';
import {SubscribeToggle} from "../../../services/Utils";
import SubdirectoryArrowRightIcon from '@material-ui/icons/SubdirectoryArrowRight';
import ReplyIcon from '@material-ui/icons/Reply';
import Typography from '@material-ui/core/Typography/index';
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Carousel from 'react-material-ui-carousel'
import PropTypes from "prop-types";
import Grid from '@material-ui/core/Grid';
import {Avatar} from "@material-ui/core";
import SaveIcon from '@material-ui/icons/Save'
import {Chip} from "@material-ui/core";
import {AuthService} from "../../../services/AuthService";
import {RestService} from "../../../services/RestService";
import { Map,Marker, GoogleApiWrapper } from 'google-maps-react';
import { ValidatorForm, TextValidator} from 'react-material-ui-form-validator';
import {Config} from "../../../services/Config";

const styles = theme => ({
    container: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    bigAvatar: {
        width: 60,
        height: 60,
    },
    biggerAvatar: {
        width: 75,
        height: 75,
    },
    form:{

    }
});

function getPost(self){
    const params = {latitude: self.props.latitude,
        longitude: self.props.longitude};

    return RestService().getPost(self.state.post_id,params);
}

function renderImageCarrousel(image_rauls){
    //Empty carrousel
    if(image_rauls.length === 0)
        return (<Carousel>
            <CardMedia
                component="img"
                image={Config.DEFAULT_POST_URL}
                alt={"image_0"}
                height="1000"
                title=""
                key={"image_0"}
            />
            </Carousel>);

    return (<Carousel>
        {image_rauls.map( (it,index) => {
            if(it) {
                return (<CardMedia
                    component="img"
                    image={it}
                    alt={"image_" + index}
                    height="1000"
                    title=""
                    key={"image_" + index}
                />)
            }
        })}
    </Carousel>);
}

function change(e,self){
    self.setState({
        [e.target.name]: e.target.value
    })
}

function renderSubscribeButton(subscribed, post, self){
    const {t,classes} = self.props;
    const subs_str = subscribed? t('unsubscribe'):t('subscribe');

    return(<Button
        variant="contained"
        color="primary"
        size="small"
        className={classes.button}
        startIcon={<SaveIcon />}
        onClick={e => {SubscribeToggle(subscribed, post, self)}}
    >
        {subs_str}
    </Button>);
}

function submitComment(e, self, key, post_id, parent_id){
     e.preventDefault();
     const comment = self.state[key];
     RestService().commentParentPost(post_id, comment, parent_id)
         .then(r=>{
            return getPost(self);
         })
         .then(r=>{
         self.setState({'post': r, [key]: ""});
         })
         .catch(err => {
             //TODO Show error in putting comment
         })
}

function renderCommentBox(user, post_id, self, parent_id = 0){
    const { classes, t } = self.props;
    const state = self.state;
    const comment_date = new Date();
    const formatted_comment_date = comment_date.toLocaleDateString();
    const key = "comment_"+user.id+"_"+parent_id;

    return (<Grid container alignItems={"center"} alignContent={"center"} justify={"center"}>
        {parent_id > 0 && <Grid item xs={2} sm={2}>
            <ReplyIcon fontSize={"large"}/>
        </Grid>}
        <Grid item xs={10} sm={10}>
            <Grid key={key} item xs={12} sm={12}>
                <Paper className={classes.root}>
                    <Grid container alignItems={"center"} alignContent={"center"} justify={"center"}>
                        <Grid item xs={1} sm={1}>
                            <Avatar className={classes.bigAvatar} alt={user.name} src={user.profile_picture} />
                        </Grid>
                        <Grid item xs={10} sm={10}>
                            <ValidatorForm
                                className={classes.form}
                                autoComplete="off"
                                ref="form"
                                instantValidate={false}
                                onSubmit={e => {submitComment(e, self, key, post_id, parent_id);}}
                                onError={errors => console.log(errors)}>
                                <div style={{ padding: 20 }}>
                                    <TextValidator
                                        id="filled-multiline-static"
                                        name={key}
                                        label={(parent_id > 0)? t('reply'):t('comment')}
                                        multiline
                                        fullWidth
                                        rows="4"
                                        variant="outlined"
                                        validators={['minStringLength:5','maxStringLength:500']}
                                        errorMessages={[t('comment-min-str-length'), t('comment-max-str-length')]}
                                        onChange={e => change(e,self)}
                                        value={state[key] ? state[key]:""}
                                    />
                                <Typography variant="overline" display="block" gutterBottom align={"right"}>
                                    {formatted_comment_date}
                                </Typography>
                                    <Typography variant="overline" display="block" gutterBottom align={"right"}>
                                        <Button variant="contained" color="primary" type="submit">
                                            {t('send')}
                                        </Button>
                                    </Typography>

                            </div>
                            </ValidatorForm>
                        </Grid>

                    </Grid>
                </Paper>
            </Grid>
        </Grid>
    </Grid>);
}

function renderCommentChilds(user, post_id, item, self){
    const { classes, t } = self.props;
    const state = self.state;

    if(item.children.length === 0)
        return null;

    return (
    <Grid container alignItems={"center"} alignContent={"center"} justify={"center"}>
        <Grid item xs={2} sm={2}>
            <SubdirectoryArrowRightIcon fontSize={"large"}/>
        </Grid>
        <Grid item xs={10} sm={10}>
            {item.children.map( (item_child, i) => {return renderComment(user, post_id, item_child,i, self, t, item.id)})}
        </Grid>
    </Grid>)
}

function renderComment(user, post_id, item, i, self,  parent_id= 0){
    const { classes, t } = self.props;
    const state = self.state;
    const comment_date = new Date(item.date);
    const formatted_comment_date = comment_date.toLocaleDateString();
    const xs_item = (parent_id > 0)? 12: 10;
    return(<Grid key={"comment_"+item.id+"_"+i} item xs={xs_item} sm={xs_item}>
        <Paper className={classes.root}>
            <Grid container alignItems={"center"} alignContent={"center"} justify={"center"}>
                <Grid item xs={1} sm={1}>
                    <Avatar className={classes.bigAvatar} alt={item.author.name} src={item.author.profile_picture} />
                </Grid>
                <Grid item xs={11} sm={11}>
                    <Typography variant="h5" component="h3">
                        {item.author.name}
                    </Typography>
                    <Typography component="p">
                        {item.content}
                    </Typography>
                    <Typography variant="overline" display="block" gutterBottom align={"right"}>
                        {formatted_comment_date}
                    </Typography>
                </Grid>
            </Grid>
        </Paper>
        <br/>
        {parent_id === 0 && (<div>
            <br/>
        {renderCommentChilds(user, post_id, item,self)}
            <br/>
        {renderCommentBox(user, post_id, self,item.id)}</div>)
        }
    </Grid>);
}

class PostComplete extends Component {
    constructor(props, context) {
        super(props, context);
        const id = props.match.params.id;
        this.state = {
            post_id: id,
            post: undefined,
            user: undefined,
            subscribing: false,
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        getPost(this)
            .then(r=>{
                this.setState({'post': r,'user':AuthService().getLoggedUser()});
            }).catch(r=>{
            //TODO SHOW ERROR
        });
    }

    render() {

        const { classes, t } = this.props;
        const { post, user } = this.state;
        const mapStyles = {
            width: '100%',
            height: '400px',
        };

        if(!post)
            return ("LOADING");

        const post_date = new Date(post.event_date);
        const formatted_post_date = post_date.toLocaleDateString();

        return (<Grid container spacing={2} alignContent={"center"} alignItems={"center"} justify={"center"} >
                <Grid item xs={12} sm={12} >
                    <Typography>

                    </Typography>
                </Grid>
                <Grid item xs={7} sm={7}>
                    <Paper className={classes.root}>
                        {renderImageCarrousel(post.image_urls)}
                    </Paper>
                </Grid>
                <Grid item xs={5} sm={5}>
                    <CardContent>
                        <Paper className={classes.root}>
                            <Grid container spacing={4} alignContent={"center"} alignItems={"center"} justify={"space-between"}>
                                <Grid item xs={10} sm={10}>
                                        <Typography variant="h4" gutterBottom>
                                            {post.title}
                                        </Typography>
                                </Grid>
                                <Grid item xs={2} sm={2}>
                                    <Chip
                                        label={t(post.category)}
                                        variant="outlined"
                                    />
                                </Grid>
                                <Grid item xs={12} sm={12}>
                                        <Typography variant="overline" display="block" gutterBottom>
                                            {t('description')}
                                        </Typography>
                                        <Typography variant="body1">
                                            {post.description}
                                        </Typography>
                                </Grid>
                                <Grid item xs={3} sm={3}>
                                        <Typography variant="overline" display="block" gutterBottom>
                                            {t('pet-type')}
                                        </Typography>
                                        <Typography variant="body1">
                                            {t(post.pet)}
                                        </Typography>
                                </Grid>
                                <Grid item xs={3} sm={3}>
                                    <Typography variant="overline" display="block" gutterBottom>
                                        {t('gender')}
                                    </Typography>
                                    <Typography variant="body1">
                                        {post.is_male? t("male"):t("female")}
                                    </Typography>
                                </Grid>
                                <Grid item xs={3} sm={3}>
                                    <Typography variant="overline" display="block" gutterBottom>
                                        {t('date-post')}
                                    </Typography>
                                    <Typography variant="body1">
                                        {formatted_post_date}
                                    </Typography>
                                </Grid>
                                <Grid item xs={3} sm={3} hidden={post.distance === 0}>
                                    <Typography variant="overline" display="block" gutterBottom>
                                        {t('distance')}
                                    </Typography>
                                    <Typography variant="body1">
                                        {post.distance}
                                    </Typography>
                                </Grid>

                                <Grid item xs={12} sm={12}>
                                    <Typography variant="overline" display="block" gutterBottom align={"right"}>
                                        {renderSubscribeButton(post.subscribed,post, this)}
                                    </Typography>

                                </Grid>
                            </Grid>
                        </Paper>
                        <br/>
                        <br />
                        <Typography variant="overline" display="block" gutterBottom>
                            {t("contact-data")}
                        </Typography>
                        <Paper className={classes.root}>

                            <Grid container spacing={3} alignContent={"center"} alignItems={"center"} justify={"center"}>
                                <Grid item xs={10} sm={10}>
                                    <Typography variant="overline" display="block" gutterBottom>
                                        {t('contact-mail')}
                                    </Typography>
                                    <Typography variant="body1">
                                        {post.creator.email}
                                    </Typography>
                                </Grid>
                                <Grid item xs={2} sm={2}>
                                    <Avatar alt={post.creator.name} src={post.creator.profile_picture} className={classes.biggerAvatar} />
                                </Grid>
                                <Grid item xs={6} sm={6}>
                                    <Typography variant="overline" display="block" gutterBottom>
                                        {t('contact-name')}
                                    </Typography>
                                    <Typography variant="body1">
                                        {post.creator.name}
                                    </Typography>
                                </Grid>
                                <Grid item xs={6} sm={6}>
                                    <Typography variant="overline" display="block" gutterBottom>
                                        {t('contact-phone')}
                                    </Typography>
                                    <Typography variant="body1">
                                        {post.contact_phone}
                                    </Typography>
                                </Grid>
                            </Grid>
                        </Paper>
                        <br/>
                        <Grid item xs={12} sm={12} style={{position: 'relative', height: '50vh'}} >
                            <Typography variant="overline" display="block" gutterBottom>
                                {t("contact-location")}
                            </Typography>
                            <Map
                                google={this.props.google}
                                zoom={15}
                                style={mapStyles}
                                initialCenter={{ lat: post.locationDTO.latitude, lng:post.locationDTO.longitude}}
                            >
                                <Marker position={{ lat: post.locationDTO.latitude, lng: post.locationDTO.longitude}} />
                            </Map>
                        </Grid>
                    </CardContent>
                </Grid>

                <Grid item xs={10} sm={10} >
                    <Typography variant="overline" display="block" gutterBottom>
                        {t("comments")}
                    </Typography>
                    <br/>
                    <Paper className={classes.root}>
                        <Grid container spacing={1} alignItems={"center"} alignContent={"center"} justify={"center"}>
                            <Typography variant="overline" display="block" gutterBottom>
                                {post.comments.length === 0 && t('no-comments')}
                            </Typography>
                            {post.comments.map( (item, i) => {return renderComment(user, post.id, item, i, this)})}
                            <br/>
                            {renderCommentBox(user, post.id,this)}
                        </Grid>

                    </Paper>
                </Grid>



            </Grid>
        );
    }
}

PostComplete.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(GoogleApiWrapper({
    apiKey: Config.GOOGLE_MAPS_KEY
})(PostComplete)));