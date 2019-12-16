import React from 'react';
import { Redirect } from 'react-router';
import PropTypes from 'prop-types';
import Grid from "@material-ui/core/Grid"
import { withStyles } from '@material-ui/core/styles/index';
import {withTranslation} from "react-i18next";
import NotificationCard from "./NotificationCard";
import {AuthService} from "../../services/AuthService";
import {createMuiTheme, MuiThemeProvider} from "@material-ui/core/styles";
import CssBaseline from "@material-ui/core/CssBaseline";
import Pagination from "material-ui-flat-pagination";
import {Config} from "../../services/Config";
import {RestService} from "../../services/RestService";
import {Link as LinkDom} from "react-router-dom";
import Link from "@material-ui/core/Link";
import Typography from "@material-ui/core/Typography";

const themeMui = createMuiTheme();
const styles = theme => ({
    root: {
        display: "grid",
        gridTemplateColumns: "repeat(4, 1fr)",
        gridGap: "24px",
    },
});

class NotificationCardsGrid extends React.Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            data:undefined,
            page: 1,
            offset: 0,
            pageSize: Config.PAGE_SIZE,
            all: (props.all)? props.all:false
        }
    }

    componentDidUpdate(prevProps,prevState){

        if(prevProps.all !== this.props.all ){
            this.loadNotifications(this.props.all)
        }
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        this.loadNotifications();
    }

    loadNotifications(all=this.state.all){
        const params = {page:this.state.page, pageSize: this.state.pageSize };
        let req;
        if(all)
            req = RestService().getAllNotifications(params);
        else
            req = RestService().getNotifications(params);
        req.then(
            r=>{
                this.setState({data:r, all:all})
            }
        ).catch(err =>{
            //TODO show error
        });
    }

    drawNotificationAll(){
        let table = [];
        for (let i = 0; i < this.state.data.notifications.length ; i++) {
            table.push(<Grid item key={i} xs={8} sm={8}><NotificationCard notification={this.state.data.notifications[i]} /></Grid>);
        }

        return table;
    }

    handlePageClick(offset) {
        const {pageSize} = this.state;
        this.setState({ offset:offset, page: (offset/pageSize + 1)});
    }

    renderLink(){
       const {t} = this.props;
        let link = '/notifications';
        let info =  t('show-less-not');

        if(!this.state.all){
            link = link+'/all';
            info =  t('show-all-not');
        }

        return (<Link component={ LinkDom } to={link} variant="body2">
            <Typography variant="overline" display="block" align={"right"} gutterBottom>
                {info}
            </Typography>
        </Link>);

    }

    render() {
        const { classes,t } =  this.props;

        if(this.state.data === undefined){
            return "LOADING";
        }

        if (this.state.data.count === 0 ){
            return (
                <div>
                    <Grid container spacing={2} alignContent={"center"} justify={"center"}>
                        <Grid item xs={8} sm={8}>
                            <Typography variant="h4" display="block" align={"left"} gutterBottom>
                                {t('notifications')}
                            </Typography>
                        </Grid>
                        <Grid item xs={8} sm={8}>
                            {this.renderLink()}
                        </Grid>
                        <Grid item xs={12} sm={12}>
                            <Grid container spacing={3} alignContent={"center"} justify={"center"}>
                                {t('empty-notifications')}
                            </Grid>
                        </Grid>
                    </Grid>
                </div>
            );
        }

        return (
            <div>

                <Grid container spacing={2} alignContent={"center"} justify={"center"}>
                    <Grid item xs={8} sm={8}>
                        <Typography variant="h4" display="block" align={"left"} gutterBottom>
                            {t('notifications')}
                        </Typography>
                    </Grid>
                    <Grid item xs={8} sm={8}>
                        {this.renderLink()}
                    </Grid>
                    <Grid item xs={12} sm={12}>
                        <Grid container spacing={3} alignContent={"center"} justify={"center"}>
                    {this.drawNotificationAll()}
                        </Grid>
                    </Grid>
                    <Grid item xs={4} sm={2}>

                        <MuiThemeProvider theme={themeMui}>
                            <CssBaseline />
                            <Pagination reduced={true} size={"large"} centerRipple={false}
                                        limit={Config.PAGE_SIZE}
                                        offset={this.state.offset}
                                        total={this.state.data.totalCount}
                                        onClick={(e, offset) => this.handlePageClick(offset)}
                            />
                        </MuiThemeProvider>
                    </Grid>
                </Grid>


            </div>
        );

    }


}

NotificationCardsGrid.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(NotificationCardsGrid));