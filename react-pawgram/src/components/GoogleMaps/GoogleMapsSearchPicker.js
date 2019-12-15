import React, { Component } from 'react';
import PropTypes from "prop-types";
import { AuthService } from "../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import LocationSearchInput from "./LocationSearchInput";
import {GoogleApiWrapper, Map, Marker, Polygon} from "google-maps-react";
import {Config} from "../../services/Config";
import {createPolygonCircle} from "../../services/Utils";
const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class GoogleMapsSearchPicker extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            marker: props.marker,
            range: props.range?props.range:0
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }

    }

    componentDidUpdate(prevProps,prevState){
        if(prevProps.range !== this.props.range ){
            this.setState({range: this.props.range});
        }
    }

    handleSelectSearch(r){
        const marker = {'latitude': r.lat, 'longitude': r.lng};
        this.setState({'marker': marker});
    }

    onMarkerDragEnd(coordinate){
        const { latLng } = coordinate;
        const marker = {'latitude': latLng.lat(), 'longitude': latLng.lng()};
        this.setState({'marker': marker});
    }

    render() {
        const { classes,t } =  this.props;
        const mapStyles = {
            width: '100%',
            height: '400px',
        };
        const isMarked = (this.state.marker && !isNaN(parseFloat(this.state.marker.latitude)) && !isNaN(parseFloat(this.state.marker.latitude)));
        const initialLatitude = isMarked? this.state.marker.latitude:Config.DEFAULT_POSITION.lat;
        const initialLongitude = isMarked? this.state.marker.longitude:Config.DEFAULT_POSITION.lng;
        const latLng = {lat:initialLatitude,lng:initialLongitude};

        //We call the callback so it gets updated
        if(this.state.marker !== this.props.marker){
            this.props.callback(this.state.marker);
        }

        return(<div>
            <LocationSearchInput callback={(r) => {this.handleSelectSearch(r)}}/>
            <Map
                google={this.props.google}
                zoom={15}
                style={mapStyles}
                initialCenter={latLng}
                center={latLng}
            >
                {isMarked &&
                <Marker position={{ lat: this.state.marker.latitude, lng: this.state.marker.longitude}}
                        draggable={true}
                        onDragend={(t, map, coordinate) => this.onMarkerDragEnd(coordinate)}
                />}
                {isMarked && this.state.range &&
                <Polygon
                    paths={createPolygonCircle(latLng,3000)}
                    strokeColor="#0000FF"
                    strokeOpacity={0.8}
                    strokeWeight={2}
                    fillColor="#0000FF"
                    fillOpacity={0.35} />}
            </Map>
        </div>);
    }
}

GoogleMapsSearchPicker.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(GoogleApiWrapper({
    apiKey: Config.GOOGLE_MAPS_KEY
})(GoogleMapsSearchPicker)));