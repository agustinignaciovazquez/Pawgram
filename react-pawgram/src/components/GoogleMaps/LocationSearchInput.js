import React from 'react';
import PlacesAutocomplete, {
    geocodeByAddress,
    getLatLng,
} from 'react-places-autocomplete';
import {withStyles} from "@material-ui/core/styles";
import {withTranslation} from "react-i18next";
import {GoogleApiWrapper} from "google-maps-react";
import {Config} from "../../services/Config";
import TextField from "@material-ui/core/TextField";
import MenuItem from "@material-ui/core/MenuItem";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
class LocationSearchInput extends React.Component {
    constructor(props) {
        super(props);
        this.state = { address: '' };
    }

    handleChange = address => {
        this.setState({ address });
    };

    handleSelect = address => {
        this.setState({ address });
        geocodeByAddress(address)
            .then(results => getLatLng(results[0]))
            .then(latLng => {this.props.callback(latLng);})
            .catch(error => {console.error('Error', error)
            //TODO SHOW ERROR HERE
            });
    };

    render() {
        const {classes,t} = this.props;
        return (
            <PlacesAutocomplete
                value={this.state.address}
                onChange={this.handleChange}
                onSelect={this.handleSelect}
            >
                {({ getInputProps, suggestions, getSuggestionItemProps, loading }) => (
                    <Grid container>
                        <Grid item xs={4} sm={4}>
                        <TextField
                            {...getInputProps({
                                placeholder: t('search-place'),
                                className: 'location-search-input',
                                variant:"outlined",
                                fullWidth: true
                            })}
                        />
                        </Grid>
                        <Grid item xs={12} sm={12}>
                        <Paper>
                            {loading && <div>{t('loading')}</div>}
                            {suggestions.map(suggestion => {
                                const className = suggestion.active
                                    ? 'suggestion-item--active'
                                    : 'suggestion-item';
                                // inline style for demonstration purpose
                                const style = suggestion.active
                                    ? { backgroundColor: '#fafafa', cursor: 'pointer' }
                                    : { backgroundColor: '#ffffff', cursor: 'pointer' };
                                return (
                                    <div
                                        {...getSuggestionItemProps(suggestion, {
                                            className,
                                            style,
                                        })}
                                    >
                                        <MenuItem >{suggestion.description}</MenuItem>
                                    </div>
                                );
                            })}
                        </Paper>
                        </Grid>
                    </Grid>
                )}
            </PlacesAutocomplete>
        );
    }
}
export default withTranslation()(GoogleApiWrapper({
    apiKey: Config.GOOGLE_MAPS_KEY
})(LocationSearchInput));