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
import MessageCard from "./MessageCard";
import Paper from "@material-ui/core/Paper";
import List from "@material-ui/core/List";
import {TextValidator, ValidatorForm} from "react-material-ui-form-validator";
import Button from "@material-ui/core/Button";

const themeMui = createMuiTheme();
const styles = theme => ({
    root: {
        display: "grid",
        gridTemplateColumns: "repeat(4, 1fr)",
        gridGap: "24px",
    },
});

function change(e,self){
    self.setState({
        [e.target.name]: e.target.value
    })
}

class MessageCardsGrid extends React.Component {
    messagesEndRef = React.createRef();

    constructor(props, context) {
        super(props, context);
        this.state = {
            user_id: props.match.params.id,
            me: AuthService().getLoggedUser(),
            data:undefined,
            send:"",
            show_error:false,
        }
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        this.loadMessages();
    }

    loadMessages(){
        RestService().getMessages(this.state.user_id).then(r=>{
            this.setState({data:r,'show_error': false,send:""},e=>{this.scrollToBottom();});
        }).catch(err=>{
            this.setState({show_error:true});
        });
    }

    drawMessagesAll(){
        let table = [];
        const {t} = this.props;
        if(this.state.data.messages.length === 0)
            return (
                <Grid item xs={12} sm={12}>
                    <Grid container alignItems={"center"} justify={"center"} alignContent={"center"}>
                    <Grid item xs={12} sm={12}>
                        <Typography component="p" align={"center"}>
                           {t('empty-messages')}
                        </Typography>
                    </Grid>
                </Grid>
                </Grid>);

        for (let i = 0; i < this.state.data.messages.length ; i++) {
            const otherUser = this.state.data.otherUser;
            const message = this.state.data.messages[i];
            const flex = (this.state.me.id === message.orig_user)? "flex-end":"flex-start";
            table.push(<Grid item key={i} xs={12} sm={12}>
                <Grid container alignItems={flex} justify={flex} alignContent={flex}>
                    <Grid item xs={6} sm={6}>
                        <MessageCard message={this.state.data.messages[i]} otheruser={otherUser} />
                    </Grid>
                </Grid>
            </Grid>);
        }

        return table;
    }

    scrollToBottom = () => {
        if(this.messagesEndRef.current && this.state.data.messages.length > 0)
            this.messagesEndRef.current.scrollIntoView({ behavior: "smooth" })
    };

    renderChatMessages(){
        return ( <Paper style={{maxHeight: 500, height: 500, overflow: 'auto'}}>
            <List>
                <Grid container spacing={3} alignContent={"center"} justify={"center"}>
                    {this.drawMessagesAll()}
                </Grid>
                <div ref={this.messagesEndRef} />
            </List>
        </Paper>);
    }

    sendMessage(e){
        e.preventDefault();
        console.log("a");
        RestService().sendMessage(this.state.data.otherUser.id,{message: this.state.send}).then(r=>{
            this.loadMessages();
        }).catch(err=>{
            this.setState({'show_error': true});
        });
    }

    handlePageClick(offset) {
        const {pageSize} = this.state;
        this.setState({ offset:offset, page: (offset/pageSize + 1)});
    }

    render() {
        const { classes,t } =  this.props;
        const comment_date = new Date();
        const formatted_comment_date = comment_date.toLocaleDateString();

        if(this.state.data === undefined){
            return <LinearProgress />;
        }

        return (
            <div>

                <Grid container spacing={2} alignContent={"center"} justify={"center"}>
                    <Grid item xs={10} sm={10}>
                        <Typography variant="h4" display="block" align={"left"} gutterBottom>
                            {t('messages')}
                        </Typography>
                        <Typography variant="h5" display="block" align={"right"} gutterBottom>
                            {this.state.data.otherUser.name + " "+this.state.data.otherUser.surname}
                        </Typography>
                    </Grid>
                    <Grid item xs={10} sm={10}>
                        <Typography component="h1" variant="h5" color={"secondary"} hidden={!this.state.show_error}>
                            {t('error-server')}
                        </Typography>
                        {this.renderChatMessages()}
                    </Grid>
                    <Grid item xs={10} sm={10}>
                        <ValidatorForm
                            className={classes.form}
                            autoComplete="off"
                            ref="form"
                            instantValidate={false}
                            onSubmit={e => {this.sendMessage(e)}}
                            onError={errors => console.log(errors)}>
                            <div style={{ padding: 20 }}>
                                <TextValidator
                                    id="filled-multiline-static"
                                    name={"send"}
                                    label={ t('send-message')}
                                    multiline
                                    fullWidth
                                    rows="1"
                                    variant="outlined"
                                    validators={['minStringLength:1','maxStringLength:1000']}
                                    errorMessages={[t('message-min-str-length'), t('message-max-str-length')]}
                                    onChange={e => change(e,this)}
                                    value={this.state.send ? this.state.send:""}
                                />
                                <Typography variant="overline" display="block" gutterBottom align={"right"}>
                                    {formatted_comment_date}
                                </Typography>
                                <Typography variant="overline" display="block" gutterBottom align={"right"}>
                                    <Button variant="contained" color="primary" type="submit">
                                        {t('send')}
                                    </Button>
                                </Typography>

                            </div>
                        </ValidatorForm>
                    </Grid>
                </Grid>


            </div>
        );

    }


}

MessageCardsGrid.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(MessageCardsGrid));