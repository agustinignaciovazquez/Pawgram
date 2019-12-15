import {RestService} from "./RestService";
import Typography from "@material-ui/core/Typography";
import Link from "@material-ui/core/Link";
import React from "react";
import {computeOffset} from "spherical-geometry-js";
import {withStyles} from "@material-ui/core";
import Slider from '@material-ui/core/Slider';

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

export const createPolygonCircle = (latLng, range, points=50)=>{
    let a=[],p=360/points,d=0;
    for(let i=0;i<points;++i,d+=p){
        let r = computeOffset(latLng,range,d);
        a.push({lat:r.latitude,lng:r.longitude});
    }
    return a;
}

export const PasswordMatchValidation = (self) => (value) => {
    if (value !== self.state.password) {
        return false;
    }
    return true;
};

const iOSBoxShadow =
    '0 3px 1px rgba(0,0,0,0.1),0 4px 8px rgba(0,0,0,0.13),0 0 0 1px rgba(0,0,0,0.02)';

export const IOSSlider = withStyles({
    root: {
        color: '#3880ff',
        height: 2,
        padding: '15px 0',
    },
    thumb: {
        height: 28,
        width: 28,
        backgroundColor: '#fff',
        boxShadow: iOSBoxShadow,
        marginTop: -14,
        marginLeft: -14,
        '&:focus,&:hover,&$active': {
            boxShadow: '0 3px 1px rgba(0,0,0,0.1),0 4px 8px rgba(0,0,0,0.3),0 0 0 1px rgba(0,0,0,0.02)',
            // Reset on touch devices, it doesn't add specificity
            '@media (hover: none)': {
                boxShadow: iOSBoxShadow,
            },
        },
    },
    active: {},
    valueLabel: {
        left: 'calc(-50% + 11px)',
        top: -22,
        '& *': {
            background: 'transparent',
            color: '#000',
        },
    },
    track: {
        height: 2,
    },
    rail: {
        height: 2,
        opacity: 0.5,
        backgroundColor: '#bfbfbf',
    },
    mark: {
        backgroundColor: '#bfbfbf',
        height: 8,
        width: 1,
        marginTop: -3,
    },
    markActive: {
        opacity: 1,
        backgroundColor: 'currentColor',
    },
})(Slider);