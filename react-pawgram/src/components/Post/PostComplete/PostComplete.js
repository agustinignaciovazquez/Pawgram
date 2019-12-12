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
import {AuthService} from "../../../services/AuthService";
import {RestService} from "../../../services/RestService";
import {Config} from "../../../services/Config";

const styles = theme => ({
    container: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    textField: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,
    },
});

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
        if(post === undefined)
            return ("LOADING");
        const post_date = new Date(post.event_date);
        const formatted_post_date = post_date.toLocaleDateString();

        return (<Grid container spacing={2}>
                <Grid item xs={12} sm={12} alignItems={"center"} justify={"center"} alignContent={"center"}>
                    <Typography>
                        {post.category}
                    </Typography>
                </Grid>
                <Grid item xs={7} sm={7}>
                    <Paper className={classes.root}>
                        <Carousel>
                            {post.image_urls.map( (it,index) => {
                                return (<CardMedia
                                    component="img"
                                    image={it}
                                    alt={"image_"+index}
                                    height="500"
                                    title=""
                                    key={"image_"+index}
                                />)
                                    })}
                        </Carousel>
                    </Paper>
                </Grid>
                <Grid item xs={5} sm={5}>
                    <CardContent>
                        <Paper className={classes.root}>
                            <Grid container spacing={4}>
                                <Grid item xs={12} sm={12}>
                                        <Typography variant="overline" display="block" gutterBottom>
                                            {t('description')}
                                        </Typography>
                                        <Typography variant="h4" gutterBottom>
                                            {post.title}
                                        </Typography>
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
                                            {post.pet}
                                        </Typography>
                                </Grid>
                                <Grid item xs={3} sm={3}>
                                    <Typography variant="overline" display="block" gutterBottom>
                                        {t('gender')}
                                    </Typography>
                                    <Typography variant="body1">
                                        {post.is_male}
                                    </Typography>
                                </Grid>
                                <Grid item xs={3} sm={3}>
                                    <Typography variant="overline" display="block" gutterBottom>
                                        {t('location')}
                                    </Typography>
                                    <Typography variant="body1">
                                        {formatted_post_date}
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
                            </Grid>
                        </Paper>
                    </CardContent>
                </Grid>

            </Grid>
        );
    }
}

PostComplete.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostComplete));