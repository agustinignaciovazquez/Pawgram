import {RestService} from "./RestService";
import Typography from "@material-ui/core/Typography";
import Link from "@material-ui/core/Link";
import React from "react";
export const Copyright = () => {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            <Link color="inherit" href="https://pawgram.org/">
                Pawgram
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
};

export const SubscribeToggle = (subscribed, post, self) =>{
    let r;
    if(self.state.subscribing)
        return;
    self.setState({'subscribing': true});
    if(subscribed)
        r = RestService().unsubscribePost(post.id);
    else
        r = RestService().subscribePost(post.id);
    r.then(r=>{
        post.subscribed = !subscribed;
        self.setState({'post': post,'subscribing':false});
    })
        .catch(err => {
            self.setState({'subscribed':false});
            //TODO show error
        });
};

export const ValidateEmail = (mail) => {
    const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(mail).toLowerCase());
};

export const DuplicateMailValidation = (value) => {
    if(ValidateEmail(value) === false)
        return false;
    return RestService().checkDuplicatedMail(value).then( r=>{return false}).catch(r=>{ return true});
};

export const PasswordMatchValidation = (value) => {
    if (value !== this.state.password) {
        return false;
    }
    return true;
};