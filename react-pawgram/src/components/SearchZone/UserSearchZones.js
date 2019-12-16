import React from 'react';
import { Redirect } from 'react-router';
import PropTypes from 'prop-types';
import Grid from "@material-ui/core/Grid"
import { withStyles } from '@material-ui/core/styles/index';
import {withTranslation} from "react-i18next";
import {AuthService} from "../../services/AuthService";
import SearchZoneCard from "./SearchZoneCard";
import {RestService} from "../../services/RestService";
import AddIcon from "@material-ui/icons/Add";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import {Config} from "../../services/Config";

const styles = theme => ({
    root: {
        display: "grid",
        gridTemplateColumns: "repeat(4, 1fr)",
        gridGap: "24px",
    },
});

class UserSearchZones extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {data: undefined};
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        RestService().getSearchZones().then(r=>{
            this.setState({data: r})
        }).catch(
            r=>{
                //TODO show error
            }
        )
    }

    drawPostAll(){
        let table = [];
        for (let i = 0; i < this.state.data.searchzones.length ; i++) {
            table.push(<Grid item key={i} xs={12}><SearchZoneCard searchzone={this.state.data.searchzones[i]} /></Grid>);
        }

        return table;
    }
    renderAddButton(){
        const {classes,t}= this.props;
        if(Config.MAX_SEARCH_ZONES - this.state.data.searchzones.length > 0){
            return(<Button
                fullWidth
                variant="contained"
                color="primary"
                size="small"
                className={classes.button}
                startIcon={<AddIcon />}
                onClick={e => {this.props.history.push('/searchzones/create')}}
            >
                {t('add-sz')}
            </Button>)
        }
        return <Typography variant="overline" display="block" gutterBottom>
            {t('max-sz-reached')}
        </Typography>
    }
    render() {
        const { classes,t } =  this.props;

        if(!this.state.data)
            return "Loading";

        if (this.state.data.count === 0 ){
            return (
                <div>
                    <Grid container spacing={8} alignContent={"center"} justify={"center"} alignItems={"center"}>
                        <Grid item xs={10} sm={10}>
                            <Typography variant="h4" display="block" align={"left"} gutterBottom>
                                {t('my-search-zones')}
                            </Typography>
                        </Grid>
                        <Grid item xs={4} sm={4}>{this.renderAddButton()}</Grid>
                        <Grid item xs={10} sm={10}>{t('empty-sz')}</Grid>
                    </Grid>
                </div>
            );
        }

        return (
            <div>
                <Grid container spacing={4} alignContent={"center"} justify={"center"} alignItems={"center"}>
                    <Grid item xs={10} sm={10}>
                        <Typography variant="h4" display="block" align={"left"} gutterBottom>
                            {t('my-search-zones')}
                        </Typography>
                    </Grid>
                    <Grid item xs={4} sm={4}>{this.renderAddButton()}</Grid>
                    <Grid item xs={10} sm={10}>
                        {this.drawPostAll()}
                    </Grid>
                </Grid>

            </div>
        );

    }


}

UserSearchZones.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(UserSearchZones));