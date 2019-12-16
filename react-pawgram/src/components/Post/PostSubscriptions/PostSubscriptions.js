import React, { Component } from 'react';
import {AuthService} from "../../../services/AuthService";
import {RestService} from "../../../services/RestService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import PostDataGrid from '../PostCardsGrid/PostCardsGrid'
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import Grid from "@material-ui/core/Grid";
import {Config} from "../../../services/Config";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import CssBaseline from "@material-ui/core/CssBaseline";
import { createMuiTheme, MuiThemeProvider } from "@material-ui/core/styles";
import Pagination from "material-ui-flat-pagination";
import {Redirect} from "react-router-dom";
import LinearProgress from "@material-ui/core/LinearProgress";

const themeMui = createMuiTheme();
const styles = theme => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    },
    selectEmpty: {
        marginTop: theme.spacing(2),
    },
});



class PostSubscriptions extends Component {
    DEFAULT_STATE = {
        location: undefined,
        posts: undefined,
        user: undefined,
        redirectUrl: undefined,
        labelWidth: 0,
        page: 1,
        offset: 0,
        pageSize: Config.PAGE_SIZE,
    };

    constructor(props, context) {
        super(props, context);
        this.state = this.DEFAULT_STATE;
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        this.loadPost();
    }
    componentDidUpdate(prevProps,prevState){
        if(prevState.offset !== this.state.offset){
            //Some filter change so we reload and save this config
            this.loadPost();
        }
    }

    loadPost(){
        RestService().getUserSubscriptions({page:this.state.page, pageSize:this.state.pageSize})
            .then(r=>{
                this.setState({'posts': r, 'user':AuthService().getLoggedUser()});
            }).catch(r=>{
                
        });
    }

    handlePageClick(offset) {
        const {pageSize} = this.state;
        this.setState({ offset:offset,page: (offset/pageSize + 1)});
    }

    redirectToUrl(){
        if(this.state.redirectUrl){
            const redirectUrl = this.state.redirectUrl;
            this.setState({'redirectUrl': null});
            return ( <Redirect to={redirectUrl} />);
        }
    }

    render() {
        const {t,classes} = this.props;

        if(this.state.posts === undefined)
            return <LinearProgress />;

        return(<Grid container justify={"center"} alignItems={"center"} alignContent={"center"}>
            <Grid item xs={12} sm={12}><PostDataGrid posts={this.state.posts} location={this.state.location} /></Grid>
            <Grid item xs={2} sm={2}>
                <MuiThemeProvider theme={themeMui}>
                    <CssBaseline />
                    <Pagination reduced={true} size={"large"} centerRipple={false}
                                limit={Config.PAGE_SIZE}
                                offset={this.state.offset}
                                total={this.state.posts.totalCount}
                                onClick={(e, offset) => this.handlePageClick(offset)}
                    />
                </MuiThemeProvider>
            </Grid>
            {this.redirectToUrl()}
        </Grid>);
    }
}

export default withStyles(styles)(withTranslation()(PostSubscriptions));