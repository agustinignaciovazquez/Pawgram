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
        };
    }

    renderNotificationTitle(notification){
        const {t} = this.props;
        if(notification.comment)
            return t('new-comment');
        return t('new-post-update');
    }

    renderNotificationSubheader(notification){
        const {t} = this.props;
        if(notification.comment)
            return notification.comment.content.substring(0, 20);
        return t('new-post-update');
    }

    renderRemoveNotification(is_seen){
        if(!is_seen)
            return <IconButton aria-label="remove" onClick={event => {console.log("CLICK")}}>
                <CloseIcon />
            </IconButton>;
        return null;
    }
    renderAvatar(notification){
        const {classes,t} = this.props;
        if(notification.comment){
            return (<Avatar aria-label="comment" className={classes.avatar} src={notification.comment.author.profile_picture}>
                {notification.comment.author.name}
                </Avatar>)
        }else{
            return <Avatar aria-label="post" className={classes.avatar} src={notification.post.image_urls[0]}>
                {notification.post.title}
                </Avatar>
        }
    }
    render() {
        const { classes, notification, t } = this.props;
        return (
            <Card className={classes.card}>
                <CardHeader
                    avatar={this.renderAvatar(notification)}
                    action={this.renderRemoveNotification(notification.is_seen)}
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