import React, { Component } from 'react';
import { AuthService } from "../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";
import SearchIcon from "@material-ui/icons/Search"
import InputAdornment from '@material-ui/core/InputAdornment';
import {ValidatorForm, TextValidator} from "react-material-ui-form-validator";
import PropTypes from "prop-types";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import PostLocation from "../Post/PostLocation/PostLocation";
import {RestService} from "../../services/RestService";
import PostSearchZone from "../SearchZone/PostSearchZone";
import LinearProgress from "@material-ui/core/LinearProgress";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class Main extends Component {

    constructor(props, context) {
        super(props, context);
        let category = (this.props.match.params.category)?this.props.match.params.category:null;
        this.state = {
            searchzones: undefined,
            category: category
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        RestService().getSearchZones().then(r=>{
            this.setState({searchzones: r});
        }).catch(err =>{
            //TODO show error
        })
    }
    drawAllSearchZones(){
        let table = [];
        this.state.searchzones.searchzones.map((item,i)=>{
            table.push(<Grid item key={i} xs={10} sm={10}><PostSearchZone kiy={i+1} searchzone={item} category={this.state.category} /></Grid>)
        });
        return table;
    }
    render() {
        const { classes,t } =  this.props;
        if(this.state.searchzones === undefined){
            return <LinearProgress />;
        }

        if(this.state.searchzones.totalCount === 0){
            this.props.history.push('/searchzones/create')
        }

        //const location = {'latitude': -34.6037618, 'longitude': -58.381715, 'range': 1000000}
        //<PostLocation location={location} category={category}/>
        return(<Grid container spacing={3} alignContent={"center"} justify={"center"} alignItems={"center"}>
            {this.drawAllSearchZones()}

        </Grid>);
    }
}
Main.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(Main));