import React, { Component } from 'react';
import PropTypes from "prop-types";
import { AuthService } from "../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import LocationSearchInput from "./LocationSearchInput";
import {GoogleApiWrapper, Map, Marker} from "google-maps-react";
import {Config} from "../../services/Config";
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
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
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

        const initialLatitude = (this.state.marker)? this.state.marker.latitude:Config.DEFAULT_POSITION.lat;
        const initialLongitude = (this.state.marker)? this.state.marker.longitude:Config.DEFAULT_POSITION.lng;

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
                initialCenter={{ lat: initialLatitude, lng: initialLongitude}}
                center={{ lat: initialLatitude, lng: initialLongitude}}
            >
                {this.state.marker &&
                <Marker position={{ lat: this.state.marker.latitude, lng: this.state.marker.longitude}}
                        draggable={true}
                        onDragend={(t, map, coordinate) => this.onMarkerDragEnd(coordinate)}
                />}
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