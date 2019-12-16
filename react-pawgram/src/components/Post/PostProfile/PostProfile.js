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

class PostProfile extends Component {
    DEFAULT_STATE = {
        user_id: undefined,
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
        this.loadPost(this.props.user_id, this.props.location, this.props.category);
    }

    loadPost(id=this.state.user_id,location=this.state.location, category = this.state.category){
        const self = this;
        //const redirectUrl = getRedirectUrl(self, location, category); : State 'redirectUrl': redirectUrl
        const {latitude,longitude} = location;
        RestService().getPostedByUser(id,latitude,longitude,category,this.state.page,this.state.pageSize).then(r=>{
            this.setState({'posts': r, 'user':AuthService().getLoggedUser(), 'location': location, 'category': category, user_id:id});
        }).catch(r=>{
            //TODO show error
        })
    }

    componentDidUpdate(prevProps,prevState){
        if(prevState.category !== this.state.category){
            this.setState({offset:0,page:1},e=>{this.loadPost()})
        }else if(prevProps.user_id !== this.props.user_id || prevProps.location !== this.props.location || prevProps.category !== this.props.category){
            this.setState(this.DEFAULT_STATE);
            this.loadPost(this.props.user_id, this.props.location, this.props.category);
        }else if(!(prevState.posts !== this.state.posts || prevState.user !== this.state.user
            || prevState.location !== this.state.location)){
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
            return (<LinearProgress />);

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

export default withStyles(styles)(withTranslation()(PostProfile));