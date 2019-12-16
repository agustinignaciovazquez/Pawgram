import React, { Component } from 'react';
import { AuthService } from "../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";

import PropTypes from "prop-types";
import PostLocation from "../Post/PostLocation/PostLocation";
import {RestService} from "../../services/RestService";
import PostCardsGrid from "../Post/PostCardsGrid/PostCardsGrid";
import LinearProgress from "@material-ui/core/LinearProgress";
import Typography from "@material-ui/core/Typography";
import {Link as LinkDom} from "react-router-dom";
import Link from "@material-ui/core/Link";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class PostSearchZone extends Component {

    constructor(props, context) {
        super(props, context);
        const searchzone = props.searchzone;
        const category = props.category? props.category:null;
        const location ={'latitude': searchzone.locationDTO.latitude,
            'longitude': searchzone.locationDTO.longitude,
            'range':searchzone.range};
        this.state = {
            searchzone: searchzone,
            category: category,
            location: location,
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        this.loadPost();
    }

    loadPost(location=this.state.location, category = this.state.category){
        const self = this;
        const {latitude,longitude,range} = location;
        RestService().getPosts(latitude, longitude, range, category, null,null,1,4)
            .then(r=>{
                this.setState({'posts': r, 'user':AuthService().getLoggedUser(), 'location': location, 'category': category});
            }).catch(r=>{

        });
    }

    renderMorePostsLink(){
        const { classes,t,kiy } =  this.props;
        if (this.state.posts.count === 0)
            return null;

        return <Grid item xs={10} sm={10}>
            <Link component={ LinkDom } to={"/searchzones/"+this.state.searchzone.id} variant="body2">
                <Typography variant="overline"  display="block" gutterBottom align={"right"}>
                    <h4>{t('more-posts')}</h4>
                </Typography>
            </Link>

        </Grid>
    }

    render() {
        const { classes,t,kiy } =  this.props;

        if(this.state.posts === undefined){
            return <LinearProgress />;
        }

        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
            <Grid item xs={10} sm={10}>
                <Typography variant="overline" display="block" gutterBottom>
                    {t('searchzone') +" #" + kiy}
                </Typography>
            </Grid>
            <Grid item xs={10} sm={10}><PostCardsGrid posts={this.state.posts} /></Grid>
            {this.renderMorePostsLink()}
        </Grid>);
    }
}
PostSearchZone.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostSearchZone));