import React, {Component} from 'react';
import {fade, makeStyles, withStyles} from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import InputBase from '@material-ui/core/InputBase';
import Badge from '@material-ui/core/Badge';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';
import MenuIcon from '@material-ui/icons/Menu';
import SearchIcon from '@material-ui/icons/Search';
import AccountCircle from '@material-ui/icons/AccountCircle';
import MailIcon from '@material-ui/icons/Mail';
import NotificationsIcon from '@material-ui/icons/Notifications';
import MoreIcon from '@material-ui/icons/MoreVert';
import {ValidatorForm,TextValidator} from "react-material-ui-form-validator";
import PropTypes from "prop-types";
import {Link as LinkDom, Redirect} from "react-router-dom";
import {withTranslation} from "react-i18next";
import {AuthService} from "../../services/AuthService";
import Logo from "../../resources/images/logo.png"
import Grid from "@material-ui/core/Grid";
import Link from "@material-ui/core/Link";
const styles = theme => ({
    grow: {
        flexGrow: 1,
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        display: 'none',
        [theme.breakpoints.up('sm')]: {
            display: 'block',
        },
    },
    search: {
        position: 'relative',
        borderRadius: theme.shape.borderRadius,
        backgroundColor: fade(theme.palette.common.white, 0.15),
        '&:hover': {
            backgroundColor: fade(theme.palette.common.white, 0.25),
        },
        marginRight: theme.spacing(2),
        marginLeft: 0,
        width: '100%',
        [theme.breakpoints.up('sm')]: {
            marginLeft: theme.spacing(3),
            width: 'auto',
        },
    },
    searchIcon: {
        width: theme.spacing(7),
        height: '100%',
        position: 'absolute',
        pointerEvents: 'none',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    inputRoot: {
        color: 'inherit',
    },
    inputInput: {
        padding: theme.spacing(1, 1, 1, 7),
        transition: theme.transitions.create('width'),
        width: '100%',
        [theme.breakpoints.up('md')]: {
            width: 200,
        },
    },
    sectionDesktop: {
        display: 'none',
        [theme.breakpoints.up('md')]: {
            display: 'flex',
        },
    },
    sectionMobile: {
        display: 'flex',
        [theme.breakpoints.up('md')]: {
            display: 'none',
        },
    },
});

class NavBar extends Component {
    constructor(props, context){
        super(props, context);
        this.state = {
            'anchorEl': null,
            'redirectUrl': null,
            'mobileMoreAnchorEl':null,
            'searchRedirect': ""
        };
    }

    handleRedirectUrl(url){
        this.setState({'redirectUrl': url});
    }

    render() {
        const {classes,t} = this.props;


        const handleProfileMenuOpen = event => {
            this.setState({'anchorEl':event.currentTarget});
        };

        const handleMobileMenuClose = () => {
            this.setState({'mobileMoreAnchorEl': null});
        };

        const handleMenuClose = () => {
            this.setState({'anchorEl': null});
            handleMobileMenuClose();
        };

        const handleMobileMenuOpen = event => {
            this.setState({'mobileMoreAnchorEl': event.currentTarget});
        };

        const menuId = 'primary-search-account-menu';
        const renderMenu = (
            <Menu
                anchorEl={this.state.anchorEl}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
                id={menuId}
                keepMounted
                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                open={Boolean(this.state.anchorEl)}
                onClose={handleMenuClose}
            >
                <MenuItem onClick={e=>{handleMenuClose();this.handleRedirectUrl('/settings')}}>{t('my-account')}</MenuItem>
                <MenuItem onClick={e=>{handleMenuClose();this.handleRedirectUrl('/logout')}}>{t('logout')}</MenuItem>
            </Menu>
        );

        const mobileMenuId = 'primary-search-account-menu-mobile';
        const renderMobileMenu = (
            <Menu
                anchorEl={this.state.mobileMoreAnchorEl}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
                id={mobileMenuId}
                keepMounted
                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                open={Boolean(this.state.mobileMoreAnchorEl)}
                onClose={handleMobileMenuClose}
            >
                <MenuItem>
                    <IconButton aria-label="show 4 new mails" color="inherit" onClick={e=>{this.handleRedirectUrl('/messages')}}>
                        <Badge badgeContent={0} color="secondary">
                            <MailIcon />
                        </Badge>
                    </IconButton>
                    <p>Messages</p>
                </MenuItem>
                <MenuItem>
                    <IconButton aria-label="show 11 new notifications" color="inherit" onClick={e=>{this.handleRedirectUrl('/notifications')}}>
                        <Badge badgeContent={0} color="secondary">
                            <NotificationsIcon />
                        </Badge>
                    </IconButton>
                    <p>Notifications</p>
                </MenuItem>
                <MenuItem onClick={handleProfileMenuOpen}>
                    <IconButton
                        aria-label="account of current user"
                        aria-controls="primary-search-account-menu"
                        aria-haspopup="true"
                        color="inherit"
                    >
                        <AccountCircle />
                    </IconButton>
                    <p>Profile</p>
                </MenuItem>
            </Menu>
        );

        if(!this.props.user)
            return null;

        if(this.state.redirectUrl){
            const redirectUrl = this.state.redirectUrl;
            this.setState({'redirectUrl': null}); //TODO Remove search bar when route is search
            return ( <Redirect to={redirectUrl} />);
        }

        return (
            <div className={classes.grow}>
                <AppBar position="static">
                    <Toolbar>
                        <IconButton
                            edge="start"
                            className={classes.menuButton}
                            color="inherit"
                            aria-label="open drawer"
                        >
                            <MenuIcon />
                        </IconButton>
                        <LinkDom >
                            <img src={Logo} />
                        </LinkDom>
                        <div className={classes.search}>
                            <ValidatorForm
                                autoComplete="off"
                                onSubmit={e => {this.handleRedirectUrl('/search/'+this.state.searchRedirect)}}
                                onError={errors => console.log(errors)}
                            >
                                <div className={classes.searchIcon}>
                                    <SearchIcon />
                                </div>

                                <InputBase
                                    placeholder={t('search')}
                                    classes={{
                                        root: classes.inputRoot,
                                        input: classes.inputInput,
                                    }}
                                    inputProps={{ 'aria-label': 'search' }}
                                    value={this.state.searchRedirect}
                                    onChange={e => this.setState({'searchRedirect': e.target.value})}
                                />
                            </ValidatorForm>
                        </div>
                        <div className={classes.grow} />
                        <div className={classes.sectionDesktop}>
                            <IconButton aria-label="messages" color="inherit" onClick={e=>{this.handleRedirectUrl('/messages')}}>
                                <Badge badgeContent={0} color="secondary">
                                    <MailIcon />
                                </Badge>
                            </IconButton>
                            <IconButton aria-label="notifications" color="inherit" onClick={e=>{this.handleRedirectUrl('/notifications')}}>
                                    <Badge badgeContent={0} color="secondary">
                                        <NotificationsIcon />
                                    </Badge>
                            </IconButton>
                            <IconButton
                                edge="end"
                                aria-label="account of current user"
                                aria-controls={menuId}
                                aria-haspopup="true"
                                onClick={handleProfileMenuOpen}
                                color="inherit"
                            >
                                <AccountCircle />
                            </IconButton>
                        </div>
                        <div className={classes.sectionMobile}>
                            <IconButton
                                aria-label="show more"
                                aria-controls={mobileMenuId}
                                aria-haspopup="true"
                                onClick={handleMobileMenuOpen}
                                color="inherit"
                            >
                                <MoreIcon />
                            </IconButton>
                        </div>
                    </Toolbar>
                </AppBar>
                {renderMobileMenu}
                {renderMenu}
            </div>
        );
    }
}
NavBar.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(NavBar));