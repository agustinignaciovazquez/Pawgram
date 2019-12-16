import React, {Component} from 'react';
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
import SaveIcon from '@material-ui/icons/Subscriptions';
import EditIcon from '@material-ui/icons/Edit';
import {Chip} from "@material-ui/core";
import {AuthService} from "../../../services/AuthService";
import {RestService} from "../../../services/RestService";
import { Map,Marker, GoogleApiWrapper } from 'google-maps-react';
import { ValidatorForm, TextValidator} from 'react-material-ui-form-validator';
import {Config} from "../../../services/Config";
import PostComments from "./PostComments";
import PostDeleteDialog from "../PostABM/PostDeleteDialog";
import {Link as LinkDom} from "react-router-dom";
import Link from "@material-ui/core/Link";

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
    if(image_rauls.postImages.length === 0)
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
        {image_rauls.postImages.map( (it,index) => {
            if(it) {
                return (<CardMedia
                    component="img"
                    image={it['url']}
                    alt={"image_" + index}
                    height="1000"
                    title=""
                    key={"image_" + index}
                />)
            }
        })}
    </Carousel>);
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

function renderEditButton(post, self){
    const {t,classes} = self.props;

    return(<Button
        fullWidth
        variant="contained"
        color="primary"
        size="small"
        className={classes.button}
        startIcon={<EditIcon />}
        onClick={e => {self.props.history.push('/post/edit/'+post.id)}}
    >
        {t('edit')}
    </Button>);
}

function renderButtons(post,user,self){
    if(post.creator.id !== user.id)
        return renderSubscribeButton(post.subscribed, post, self);
    else
        return(<Grid container spacing={2} alignItems={"flex-end"} justify={"flex-end"} alignContent={"flex-end"}>
            <Grid item xs={2} sm={2}>
                {renderEditButton(post,self)}
            </Grid>
            <Grid item xs={2} sm={2}>
                <PostDeleteDialog post_id={post.id} callback={r => {self.props.history.push('/')}} />
            </Grid>
        </Grid>)
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
                                        {renderButtons(post,user,this)}
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
                                    <Link component={ LinkDom } to={"/user/"+post.creator.id} variant="body2">
                                        <Avatar alt={post.creator.name} src={post.creator.profile_picture} className={classes.biggerAvatar} />
                                    </Link>
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
                    <PostComments post={post} />
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