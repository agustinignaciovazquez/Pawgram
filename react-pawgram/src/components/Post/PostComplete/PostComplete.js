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
import Typography from '@material-ui/core/Typography/index';
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Carousel from 'react-material-ui-carousel'
import PropTypes from "prop-types";
import Grid from '@material-ui/core/Grid';
import {Avatar} from "@material-ui/core";
import {Chip} from "@material-ui/core";
import {AuthService} from "../../../services/AuthService";
import {RestService} from "../../../services/RestService";
import { Map,Marker, GoogleApiWrapper } from 'google-maps-react';
import {Config} from "../../../services/Config";

const styles = theme => ({
    container: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    textField: {
    },
});

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

class PostComplete extends Component {


    constructor(props, context) {
        super(props, context);
        const id = props.match.params.id;
        this.state = {
            post_id: id,
            post: undefined,
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        const params = {latitude: this.props.latitude,
                        longitude: this.props.longitude};
        console.log(this.state)
        RestService().getPost(this.state.post_id,params)
            .then(r=>{
                this.setState({'post': r});
            }).catch(r=>{
            //TODO SHOW ERROR
        });
    }

    render() {
        const { classes, t } = this.props;
        const {post} = this.state;
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
                                <Grid item xs={10} sm={10}/>

                                <Grid item xs={2} sm={2}>
                                    <Chip
                                        label={t(post.category)}
                                        variant="outlined"
                                    />
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
                                    <Avatar alt={post.creator.name} src={post.creator.profile_picture} />
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

                <Grid item xs={7} sm={7} >
                    <Typography variant="overline" display="block" gutterBottom>
                        {t("comments")}
                    </Typography>
                    <br/>
                    <Paper className={classes.root}>
                        <Grid container spacing={1} alignItems={"center"} alignContent={"center"} justify={"center"}>
                            <Typography variant="overline" display="block" gutterBottom>
                                {post.comments.length === 0 && t('no-comments')}
                            </Typography>
                            {post.comments.map( (item, i) => {
                                return(<Grid item xs={12} sm={12}>
                                    <Card className={classes.card}>
                                        <CardContent>

                                            <Typography variant="h5" component="h5">

                                                be

                                                lent
                                            </Typography>
                                            <Typography className={classes.pos} color="textSecondary">
                                                adjective
                                            </Typography>
                                            <Typography variant="body2" component="p">
                                                well meaning and kindly.
                                            </Typography>
                                        </CardContent>
                                        <CardActions>
                                            <Button size="small">Learn More</Button>
                                        </CardActions>
                                    </Card>
                                </Grid>);
                            })}
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