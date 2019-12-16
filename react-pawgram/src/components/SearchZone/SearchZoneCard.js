import React, {Component} from 'react';
import { makeStyles } from '@material-ui/core/styles/index';
import Card from '@material-ui/core/Card/index';
import CardActionArea from '@material-ui/core/CardActionArea/index';
import CardHeader from '@material-ui/core/CardHeader';
import CardActions from '@material-ui/core/CardActions/index';
import CardContent from '@material-ui/core/CardContent/index';
import CardMedia from '@material-ui/core/CardMedia/index';
import Button from '@material-ui/core/Button/index';
import Typography from '@material-ui/core/Typography/index';
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import { red } from '@material-ui/core/colors';
import Chip from '@material-ui/core/Chip';
import DoneIcon from '@material-ui/icons/Done';
import {Link as LinkDom} from "react-router-dom";
import Link from "@material-ui/core/Link";
import Paper from "@material-ui/core/Paper";
import PropTypes from "prop-types";
import SearchZoneDeleteDialog from "./SearchZoneDeleteDialog";
import {GoogleApiWrapper, Map, Marker, Polygon} from "google-maps-react";
import {Config} from "../../services/Config";
import Grid from "@material-ui/core/Grid";
import { computeOffset } from 'spherical-geometry-js';
import {createPolygonCircle} from "../../services/Utils";
const styles = theme =>({
    card: {
        maxWidth: 1000,
    },
    chip: {
        backgroundColor: red[500],
    },
});

class SearchZoneCard extends Component {


    constructor(props, context) {
        super(props, context);
        this.state = {
            removed: false
        };
    }

    render() {
        const { classes, searchzone, t } = this.props;
        const mapStyles = {
            width: '100%',
            height: '400px',
        };
        const latLng = { lat: searchzone.locationDTO.latitude, lng:searchzone.locationDTO.longitude}
        return (
            <Paper className={classes.root} hidden={this.state.removed}>
                <CardContent>
                    <Grid container>
                        <Grid item xs={12} sm={12}>
                            <Typography gutterBottom variant="h5" component="h2">
                                {t('searchzone-range') }
                            </Typography>
                            <Typography variant="body2" color="textSecondary" component="p" hidden={1 === 1}>
                                Near by
                            </Typography>
                         </Grid>
                        <Grid item xs={12} sm={12} style={{position: 'relative', height: '50vh'}} >
                            <Map
                                google={this.props.google}
                                zoom={13}
                                style={mapStyles}
                                initialCenter={latLng}
                            >
                                <Marker position={latLng} />
                                <Polygon
                                    paths={createPolygonCircle(latLng,searchzone.range)}
                                    strokeColor="#0000FF"
                                    strokeOpacity={0.8}
                                    strokeWeight={2}
                                    fillColor="#0000FF"
                                    fillOpacity={0.35} />
                            </Map>
                        </Grid>
                    </Grid>
                </CardContent>
                <CardActions>
                    <Grid container justify={"flex-end"}>
                        <Grid item xs={3} sm={3}>
                        <SearchZoneDeleteDialog sz_id={searchzone.id} callback={r=>{this.setState({'removed':true})}}/>

                        </Grid>
                    </Grid>
                </CardActions>
            </Paper>
        );
    }
}

SearchZoneCard.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(GoogleApiWrapper({
    apiKey: Config.GOOGLE_MAPS_KEY
})(SearchZoneCard)));