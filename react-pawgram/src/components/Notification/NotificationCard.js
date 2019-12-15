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
import {Link as LinkDom} from "react-router-dom";
import Link from "@material-ui/core/Link";

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

class NotificationCard extends React.Component {


    constructor(props, context) {
        super(props, context);
        this.state = {
            removed: false,
        };
    }

    renderNotificationTitle(notification){
        const {t} = this.props;
        const postEnd = (notification.post.title.length > 10)?"...":""
        if(notification.comment)
            return t('new-comment') + " " + notification.post.title.substring(0, 10) + postEnd;
        return t('new-post-update');
    }

    renderNotificationSubheader(notification){
        const {t} = this.props;
        const postEnd = (notification.post.title.length > 10)?"...":""
        if(notification.comment) {
            const commentEnd = (notification.comment.content.length > 20)?"...":""
            return notification.comment.content.substring(0, 20) + commentEnd;
        }
        return notification.post.title.substring(0, 20)+postEnd;
    }

    renderRemoveNotification(is_seen, id){
        if(!is_seen)
            return <IconButton aria-label="remove" onClick={event => {this.handleRemoveNotification(id)}}>
                <CloseIcon />
            </IconButton>;
        return null;
    }

    handleRemoveNotification(id){
        RestService().markNotificationAsSeen(id).then( r=>{
            this.setState({removed: true});
        }).catch(r=>{
            //TODO Show error here
        })
    }

    renderAvatar(notification){
        const {classes,t} = this.props;
        if(notification.comment){
            return (<Link component={ LinkDom } to={"/post/"+notification.post.id} variant="body2">
                <Avatar aria-label="comment" className={classes.avatar} src={notification.comment.author.profile_picture}>
                {notification.comment.author.name}
                </Avatar>
            </Link>)
        }else{
            const image = (notification.post.image_urls.length >0)? notification.post.image_urls[0]: "";

            return <Link component={ LinkDom } to={"/post/"+notification.post.id} variant="body2">
                <Avatar aria-label="comment" className={classes.avatar} src={image}>
                    {notification.post.id}
                </Avatar>
            </Link>
        }
    }
    render() {
        const { classes, notification, t } = this.props;
        if(this.state.removed)
            return null;
        return (
            <Card className={classes.card}>
                <CardHeader
                    avatar={this.renderAvatar(notification)}
                    action={this.renderRemoveNotification(notification.is_seen, notification.id)}
                    title={this.renderNotificationTitle(notification)}
                    subheader={this.renderNotificationSubheader(notification)}
                />
            </Card>
        );
    }
}

NotificationCard.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(NotificationCard));