import React, { Component } from 'react';
import { AuthService } from "../../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";
import PropTypes from "prop-types";
import {RestService} from "../../../services/RestService";
import PostABM from "./PostABM";

const styles = theme => ({
    margin: {
        margin: theme.spacing(1),
    },
});

class PostEdit extends Component {

    constructor(props, context) {
        super(props, context);
        const id = props.match.params.id;
        this.state = {
            post_id: id,
            post: undefined
        };
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        const user = AuthService().getLoggedUser();
        RestService().getPost(this.state.post_id).then(r => {
            if(r.creator.id === user.id){
                this.setState({'post':r});
            }else{
                //Forbidden access
                this.props.history.push('/')
            }
        })
            .catch(err => {
                console.log(err);
                //TODO show error
        });
    }

    render() {
        const { classes,t } =  this.props;

        if(this.state.post === undefined)
            return "LOADING";

        return(<Grid container alignContent={"center"} justify={"center"} alignItems={"center"}>
            <Grid item xs={10} sm={10}>
                <PostABM post={this.state.post} category={this.state.post.category}/>
            </Grid>

        </Grid>);
    }
}
PostEdit.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostEdit));