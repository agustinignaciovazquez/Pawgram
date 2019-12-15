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
import {RestService} from "../../../services/RestService";

class PostDeleteDialog extends Component  {
    constructor(props, context) {
        super(props, context);
        const post_id = props.post_id;
        const image_id = props.image_id;
        const callback = props.callback? props.callback: (e)=>console.log(e);
        let is_image = Boolean(image_id);

        this.state = {
            post_id: post_id,
            post_image_id: image_id,
            is_image: is_image,
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

    removePost = () => {
        const {post_id, post_image_id,is_image} = this.state;
        let req;
        this.handleClose();
        if(is_image)
            req = RestService().deletePostImage(post_id,post_image_id);
        else
            req = RestService().deletePost(post_id);
        this.state.callback(req);
    }

    renderDialogContent(is_image){
        const {classes,t} = this.props;
        if(is_image)
            return t('remove-image-dialog');
        return t('remove-post-dialog');
    }

    render() {
        const {classes,t} = this.props;

        return (
            <div>
                <Button variant="outlined" color="primary" onClick={this.handleClickOpen} >
                    <RemoveIcon />
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
                            {this.renderDialogContent(this.state.is_image)}
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose} color="primary">
                            {t('disagree')}
                        </Button>
                        <Button onClick={this.removePost} color="primary" autoFocus>
                            {t('agree')}
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}
export default withTranslation()(PostDeleteDialog);