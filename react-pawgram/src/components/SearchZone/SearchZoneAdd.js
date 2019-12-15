import React, { Component } from 'react';
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";
import PropTypes from "prop-types";
import {AuthService} from "../../services/AuthService";
import {RestService} from "../../services/RestService";
import GoogleMapsSearchPicker from "../GoogleMaps/GoogleMapsSearchPicker";
import {TextValidator, ValidatorForm} from "react-material-ui-form-validator";
import Box from "@material-ui/core/Box";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import {IOSSlider} from "../../services/Utils";
import {Config} from "../../services/Config";
import Button from "@material-ui/core/Button";
import {Chip} from "@material-ui/core";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class SearchZoneAdd extends Component {

    constructor(props, context) {
        super(props, context);
        const id = props.match.params.id;
        this.state = {
            range: Config.MIN_DISTANCE_SZ,
            longitude: "",
            latitude: "",
            marker: {latitude: "", longitude: ""},
            show_error:false,
            show_error_server:false
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
    }

    handleMarker (m) {
        // Set new location
        this.setState({'marker':m, 'latitude': m.latitude, 'longitude': m.longitude});
    }

    getDistanceMarkers(){
        let a = []
        for(let i = Config.MIN_DISTANCE_SZ;i <=Config.MAX_DISTANCE_SZ;i++)
            a.push({value: i});
        return a;
    }
    submitSz(e){
        e.preventDefault();
        const data = {longitude:this.state.longitude,latitude:this.state.latitude,range:this.state.range};
        RestService(data).createSearchZone(data).then(
                r=>{
                    this.props.history.push('/searchzones')
                }
            ).catch( err=>{
                const errors = (err.response.status !== 500 && err.response.data.errors)? err.response.data.errors:[];
                this.setState({show_error:false,show_error_server:true});
            });
    }
    render() {
        const { classes,t } =  this.props;

        return(
            <Grid container alignItems={"center"} justify={"center"} alignContent={"center"}>
                <Grid item xs={9} sm={9}>
                    <Typography variant="h4" gutterBottom>
                        {t('create-sz')}
                    </Typography>
                </Grid>
                <Grid item xs={9} sm={9}>
            <Paper>
                <Box p={2}>
                <ValidatorForm
                    className={classes.form}
                    autoComplete="off"
                    ref="form"
                    onSubmit={e => this.submitSz(e)}
                    onError={errors => {this.setState({show_error:true, show_error_server:false});}}
                >
                    <Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={12}>
                                <Typography component="h1" variant="h5" color={"secondary"} hidden={!this.state.show_error}>
                                    {t('complete-all-fields')}
                                </Typography>
                                <Typography component="h1" variant="h5" color={"secondary"} hidden={!this.state.show_error_server}>
                                    {t('error-server')}
                                </Typography>
                            </Grid>
                        </Grid>
                        <Grid item xs={10} sm={10} style={{position: 'relative', height: '65vh'}}>
                            <Typography gutterBottom> {t('location')}</Typography><br/>
                            <GoogleMapsSearchPicker marker={this.state.marker} callback={(m) => this.handleMarker(m)} range={this.state.range}/>
                         </Grid>

                        <Grid item xs={10} sm={10}>
                            <Typography gutterBottom>{t('range-search-select')}</Typography><br/>
                            <IOSSlider aria-label="ios slider" defaultValue={this.state.range}
                                       onChange={(e, newValue) => {this.setState({range:newValue*1000})}}
                                       max={Config.MAX_DISTANCE_SZ} min={Config.MIN_DISTANCE_SZ}
                                       marks={this.getDistanceMarkers()} valueLabelDisplay="on" />
                        </Grid>

                        <Grid item xs={10} sm={10} hidden={true}>
                            <TextValidator
                                name="longitude"
                                variant="outlined"
                                id="longitude"
                                label={t("longitude")}
                                validators={['required']}
                                errorMessages={[t('no-location')]}
                                value={this.state.longitude}
                            />

                            <TextValidator
                                name="latitude"
                                variant="outlined"
                                id="latitude"
                                label={t("latitude")}
                                validators={['required']}
                                errorMessages={[t('no-location')]}
                                value={this.state.latitude}
                            />
                        </Grid>
                     </Grid>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.submit}
                    >
                        {t("create-sz")}
                    </Button>
                </ValidatorForm>
                </Box>
            </Paper>
                </Grid>
            </Grid>);
    }
}
SearchZoneAdd.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(SearchZoneAdd));