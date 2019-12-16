import React from 'react';
import { Redirect } from 'react-router';
import PropTypes from 'prop-types';
import Grid from "@material-ui/core/Grid"
import { withStyles } from '@material-ui/core/styles/index';
import {withTranslation} from "react-i18next";
import {AuthService} from "../../services/AuthService";
import {createMuiTheme, MuiThemeProvider} from "@material-ui/core/styles";
import CssBaseline from "@material-ui/core/CssBaseline";
import Pagination from "material-ui-flat-pagination";
import {Config} from "../../services/Config";
import {RestService} from "../../services/RestService";
import {Link as LinkDom} from "react-router-dom";
import Link from "@material-ui/core/Link";
import Typography from "@material-ui/core/Typography";
import LinearProgress from "@material-ui/core/LinearProgress";
import ConversationCard from "./ConversationCard";

const themeMui = createMuiTheme();
const styles = theme => ({
    root: {
        display: "grid",
        gridTemplateColumns: "repeat(4, 1fr)",
        gridGap: "24px",
    },
});

class ConversationCardsGrid extends React.Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            data:undefined,
        }
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        this.loadConversations();
    }

    loadConversations(a){
        RestService().getConversations().then(
            r=>{
                this.setState({data:r, show_error:false})
            }
        ).catch(err =>{
            this.setState({ show_error:true})
        });
    }

    drawConversationAll(){
        let table = [];
        for (let i = 0; i < this.state.data.users.length ; i++) {
            table.push(<Grid item key={i} xs={8} sm={8}><ConversationCard user={this.state.data.users[i]} /></Grid>);
        }

        return table;
    }

    render() {
        const { classes,t } =  this.props;

        if(this.state.data === undefined){
            return <LinearProgress />;
        }

        if (this.state.data.count === 0 ){
            return (
                <div>
                    <Grid container spacing={2} alignContent={"center"} justify={"center"}>
                        <Grid item xs={8} sm={8}>
                            <Typography variant="h4" display="block" align={"left"} gutterBottom>
                                {t('conversations')}
                            </Typography>
                        </Grid>
                        <Grid item xs={8} sm={8}>
                            {this.renderLink()}
                        </Grid>
                        <Grid item xs={12} sm={12}>
                            <Grid container spacing={3} alignContent={"center"} justify={"center"}>
                                {t('empty-conversations')}
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
                            {t('conversations')}
                        </Typography>
                    </Grid>
                    <Grid item xs={12} sm={12}>
                        <Grid container spacing={3} alignContent={"center"} justify={"center"}>
                            {this.drawConversationAll()}
                        </Grid>
                    </Grid>
                </Grid>


            </div>
        );

    }


}

ConversationCardsGrid.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(ConversationCardsGrid));