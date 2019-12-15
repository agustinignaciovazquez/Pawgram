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

function getRedirectUrl(self, query=self.state.query,category=self.state.category){
    let redirectUrl;
    if (category)
        redirectUrl = '/category/'+ category + '/location/' + query;
    else
        redirectUrl = '/location/' + query;

    return redirectUrl;
}

function renderCategories(self){
    const { classes, t } = self.props;
    const { labelWidth } = self.state;

    return(<FormControl fullWidth className={classes.formControl} color={"primary"}>
        <InputLabel id="demo-simple-select-label">
            {t('category-criteria')}
        </InputLabel>
        <Select
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={self.state.category}
            //'redirectUrl': getRedirectUrl(self,self.state.query,event.target.value)
            onChange={event => {self.setState({'category': event.target.value})}} >
            <MenuItem value={null}>
                <em>{t('categories-default')}</em>
            </MenuItem>
            {Config.CATEGORIES.map(item => {
                return <MenuItem value={item}>{t(item)}</MenuItem>
            })}

        </Select>
    </FormControl>)
}

function renderSelectOrderCriteriaSelect(self){
    const { classes, t } = self.props;
    const { labelWidth } = self.state;

    return(<FormControl fullWidth className={classes.formControl} color={"primary"}>
        <InputLabel id="demo-simple-select-label">
            {t('order-criteria')}
        </InputLabel>
        <Select
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={self.state.order_criteria}
            onChange={event => {self.setState({'order_criteria': event.target.value});}}
        >
            <MenuItem value={null}>
                <em>{t('order-default')}</em>
            </MenuItem>
            {Config.ORDER_CRITERIA.map(item => {
                return <MenuItem value={item}>{t(item)}</MenuItem>
            })}

        </Select>
    </FormControl>)
}

function renderSelectSortCriteriaSelect(self){
    const { classes, t } = self.props;
    const { latitude,longitude } = self.state.location;

    return(<FormControl fullWidth className={classes.formControl} color={"primary"}>
        <InputLabel id="demo-simple-select-label">
            {t('sort-criteria')}
        </InputLabel>
        <Select
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={self.state.sort_criteria}
            onChange={event => {self.setState({'sort_criteria': event.target.value});}}
        >
            <MenuItem value={null}>
                <em>{t('sort-default')}</em>
            </MenuItem>
            {Config.SORT_CRITERIA.map(item => {
                //Hot-fix for sort by distance without location
                if('distance' === item && (latitude == null || longitude == null))
                    return;
                return <MenuItem value={item}>{t(item)}</MenuItem>
            })}

        </Select>
    </FormControl>)
}

class PostLocation extends Component {
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
        this.loadPost(this.props.location, this.props.category);
    }

    loadPost(location=this.state.location, category = this.state.category){
        const self = this;
        //const redirectUrl = getRedirectUrl(self, location, category); : State 'redirectUrl': redirectUrl
        const {latitude,longitude,range} = location;
        RestService().getPosts(latitude, longitude, range,category, self.state.sort_criteria, self.state.order_criteria, self.state.page, self.state.pageSize, self.props.latitude, self.props.longitude)
            .then(r=>{
                this.setState({'posts': r, 'user':AuthService().getLoggedUser(), 'location': location, 'category': category});
            }).catch(r=>{
            //TODO SHOW ERROR
        });
    }

    componentDidUpdate(prevProps,prevState){
        if(prevProps.location !== this.props.location || prevProps.category !== this.props.category){
            this.setState(this.DEFAULT_STATE);
            this.loadPost(this.props.location, this.props.category);
        }else if(!(prevState.posts !== this.state.posts || prevState.user !== this.state.user
            || prevState.redirectUrl !== this.state.redirectUrl || prevState.location !== this.state.location)){
            //Some filter change so we reload and save this config
            this.loadPost();
        }
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
            return ("LOADING");

        return(<Grid container justify={"center"} alignItems={"center"} alignContent={"center"}>
            <Grid item xs={12} sm={12}>
                <Typography variant="overline" display="block" gutterBottom align={"right"}>
                    {t('filters')}
                </Typography>
                <Grid container justify={"flex-end"} spacing={5}>

                    <Grid item xs={3} sm={3}>
                        <Typography variant="overline" display="block" gutterBottom align={"right"}>
                            {renderCategories(this)}
                        </Typography>
                    </Grid>
                    <Grid item xs={2} sm={2}>
                        <Typography variant="overline" display="block" gutterBottom align={"right"}>
                            {renderSelectSortCriteriaSelect(this)}
                        </Typography>
                    </Grid>
                    <Grid item xs={2} sm={2}>
                        <Typography variant="overline" display="block" gutterBottom align={"right"}>
                            {renderSelectOrderCriteriaSelect(this)}
                        </Typography>
                    </Grid>
                </Grid>

            </Grid>
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

export default withStyles(styles)(withTranslation()(PostLocation));