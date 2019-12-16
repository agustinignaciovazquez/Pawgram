import React, { Component } from 'react';
import {AuthService} from "../../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";
import SearchIcon from "@material-ui/icons/Search"
import InputAdornment from '@material-ui/core/InputAdornment';
import {ValidatorForm, TextValidator} from "react-material-ui-form-validator";
import PropTypes from "prop-types";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import PostLocation from "./PostLocation";
import LinearProgress from "@material-ui/core/LinearProgress";
import {RestService} from "../../../services/RestService";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class PostLocationContainer extends Component {

    constructor(props, context) {
        super(props, context);
        const id = props.match.params.id;
        const category = props.category? props.category:null;
        this.state = {
            sz_id: id,
            category:category,
            searchzone: undefined,
        };
    }

    componentDidUpdate(prevProps,prevState){
        if(prevProps.match.params.id !== this.props.match.params.id){
            this.setState({searchzone: undefined,sz_id:this.props.match.params.id});
        }
        if(prevState.sz_id !== this.state.sz_id){
            this.getSZ();
        }
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        this.getSZ();
    }

    getSZ(){
        RestService().getSearchZone(this.state.sz_id).then(r=>{
            this.setState({searchzone:r});
        }).catch(err=>{
            this.props.history.push('/');
        })
    }

    render() {
        const { classes,t } =  this.props;
        const {searchzone,category} = this.state;

        if(this.state.searchzone === undefined){
            return <LinearProgress />;
        }

        const location ={'latitude': searchzone.locationDTO.latitude,
            'longitude': searchzone.locationDTO.longitude,
            'range':searchzone.range};

        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
            <Grid item xs={10} sm={10}><PostLocation location={location} category={category}/></Grid>
        </Grid>);
    }
}
PostLocationContainer.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostLocationContainer));