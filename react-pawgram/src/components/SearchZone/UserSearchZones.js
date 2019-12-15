import React from 'react';
import { Redirect } from 'react-router';
import PropTypes from 'prop-types';
import Grid from "@material-ui/core/Grid"
import { withStyles } from '@material-ui/core/styles/index';
import {withTranslation} from "react-i18next";
import {AuthService} from "../../services/AuthService";
import SearchZoneCard from "./SearchZoneCard";
import {RestService} from "../../services/RestService";

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
            console.log(this.state.data.searchzones[i])
            table.push(<Grid item key={i} xs={10}><SearchZoneCard searchzone={this.state.data.searchzones[i]} /></Grid>);
        }

        return table;
    }

    render() {
        const { classes,t } =  this.props;

        if(!this.state.data)
            return "Loading";

        if (this.state.data.count === 0 ){
            return (
                <div>
                    <h3>{t('searchzones')}</h3>
                    <div>{t('empty-sz')}</div>
                </div>
            );
        }

        return (
            <div>
                <h3>{t('my-search-zones')}</h3>
                <Grid container spacing={8} alignContent={"center"} justify={"center"} alignItems={"center"}>
                    {this.drawPostAll()}
                </Grid>

            </div>
        );

    }


}

UserSearchZones.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(UserSearchZones));