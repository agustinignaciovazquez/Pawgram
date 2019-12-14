import React, { Component } from 'react';
import {AuthService} from "../../../services/AuthService";
import {RestService} from "../../../services/RestService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import PostDataGrid from '../PostCardsGrid/PostCardsGrid'
import PostSearch from "./PostSearch";
import Grid from "@material-ui/core/Grid";
import SearchIcon from "@material-ui/icons/Search"
import InputAdornment from '@material-ui/core/InputAdornment';
import {ValidatorForm, TextValidator} from "react-material-ui-form-validator";
import PropTypes from "prop-types";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class PostSearchContainer extends Component {

    constructor(props, context) {
        super(props, context);
        const query = props.match.params.query;
        this.state = {
            query: query,
            query_input: query,
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }

    }

    render() {
        const { classes,t } =  this.props;
        let query = this.state.query;
        const category = this.props.match.params.category;
        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
            <Grid item xs={12} sm={12}>
                <ValidatorForm
                    autoComplete="off"
                    ref="form"
                    onSubmit={e => {
                        e.preventDefault();
                        query = this.state.query_input;
                        this.setState({'query': query});}}
                    onError={errors => console.log(errors)}>
                    <Typography variant={"h5"} display="block" gutterBottom align={"left"}>
                        {t('search')}
                    </Typography>
                <Grid container alignContent={"center"} justify={"space-around"} alignItems={"center"}>
                    <Grid item xs={9} sm={9}>

                        <TextValidator
                            className={classes.margin}
                            fullWidth
                            variant={"outlined"}
                            id="input-with-icon-textfield"
                            InputProps={{
                                startAdornment: (
                                    <InputAdornment position="start">
                                        <SearchIcon />
                                    </InputAdornment>
                                ),
                            }}
                            validators={['minStringLength:3','maxStringLength:50']}
                            errorMessages={[t('search-min-str-length'), t('search-max-str-length')]}
                            onChange={e => {this.setState({'query_input': e.target.value})}}
                            value={this.state.query_input ? this.state.query_input:""}
                        />
                    </Grid>
                    <Grid item xs={2} sm={2}>
                        <Button fullWidth variant="outlined" color="primary" type="submit">
                            {t('search')}
                        </Button>
                    </Grid>
                </Grid>
            </ValidatorForm>
            </Grid>
            <Grid item xs={12} sm={12}><PostSearch query={query} category={category}/></Grid>

        </Grid>);
    }
}
PostSearchContainer.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostSearchContainer));