import React, {Component} from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import {withStyles} from "@material-ui/core/styles";
import {withTranslation} from "react-i18next";
import RemoveIcon from "@material-ui/icons/DeleteOutline";
import IconButton from "@material-ui/core/IconButton";
import EditIcon from "@material-ui/icons/Edit";
import PropTypes from "prop-types";
import {RestService} from "../../services/RestService";

const styles = theme => ({

});

class SearchZoneDeleteDialog extends Component  {
    constructor(props, context) {
        super(props, context);
        const sz_id = props.sz_id;
        const callback = props.callback? props.callback: (e)=>console.log(e);

        this.state = {
            sz_id: sz_id,
            callback: callback,
            open:false,
        };
    }

    handleClickOpen = () => {
        this.setState({open:true});
    };

    handleClose = () => {
        this.setState({open:false});
    };

    removeSZ = () => {
        const {sz_id} = this.state;
        let req;
        this.handleClose();
        req = RestService().deleteSearchZone(sz_id);
        this.state.callback(req);
    }

    render() {
        const {classes,t} = this.props;

        return (
            <div>
                <Button
                    fullWidth
                    variant="contained"
                    color="primary"
                    size="small"
                    className={classes.button}
                    startIcon={<RemoveIcon />}
                    onClick={this.handleClickOpen}
                >
                    {t('remove')}
                </Button>
                <Dialog
                    open={this.state.open}
                    onClose={this.handleClose}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">{t('delete')}</DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            {t('delete-sz')}
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose} color="primary">
                            {t('disagree')}
                        </Button>
                        <Button onClick={this.removeSZ} color="primary" autoFocus>
                            {t('agree')}
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}
SearchZoneDeleteDialog.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles)(withTranslation()(SearchZoneDeleteDialog));