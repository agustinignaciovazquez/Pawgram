import React from 'react';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import { withStyles } from '@material-ui/core/styles/index';
import {withTranslation} from "react-i18next";
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import { red } from '@material-ui/core/colors';
import CloseIcon from '@material-ui/icons/Close';
import PropTypes from "prop-types";
import {RestService} from "../../services/RestService";
import {Link as LinkDom, Redirect} from "react-router-dom";
import Link from "@material-ui/core/Link";
import {AuthService} from "../../services/AuthService";
import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import CardActionArea from "@material-ui/core/CardActionArea";

const styles = theme =>({
    card: {
    },
    media: {
        height: 0,
        paddingTop: '56.25%', // 16:9
    },
    expand: {
        transform: 'rotate(0deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
    },
    expandOpen: {
        transform: 'rotate(180deg)',
    },
    avatar: {
        backgroundColor: red[500],
    },
});

class ConversationCard extends React.Component {


    constructor(props, context) {
        super(props, context);
        this.state = {
            me: AuthService().getLoggedUser(),
        };
    }

    setRedirectToUrl(url){
        this.setState({redirectUrl:url})
    }

    redirectToUrl(){
        if(this.state.redirectUrl){
            const redirectUrl = this.state.redirectUrl;
            this.setState({'redirectUrl': null});
            return ( <Redirect to={redirectUrl} />);
        }
    }

    render() {
        const { classes,  user} = this.props;

        return (
            <Card className={classes.card}>
                <CardActionArea onClick={event => {this.setRedirectToUrl('/message/'+user.id)}}>
                <CardHeader
                    avatar={<Link component={ LinkDom } to={"/user/"+user.id} variant="body2">
                        <Avatar aria-label="comment" className={classes.avatar} src={user.profile_picture}>
                            {user.name}
                        </Avatar>
                    </Link>}
                    title={user.name +" "+user.surname}
                    subheader={user.email}
                />
                </CardActionArea>
                {this.redirectToUrl()}
            </Card>
        );
    }
}

ConversationCard.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(ConversationCard));