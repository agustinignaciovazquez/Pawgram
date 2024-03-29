import React, { Component } from 'react';
import { AuthService } from "../../../services/AuthService";
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import Grid from "@material-ui/core/Grid";
import PropTypes from "prop-types";
import {RestService} from "../../../services/RestService";
import PostABM from "./PostABM";
import LinearProgress from "@material-ui/core/LinearProgress";

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

    componentDidUpdate(prevProps,prevState){
        if(prevProps.match.params.id !== this.props.match.params.id){
            this.setState({post: undefined,post_id:this.props.match.params.id});
        }
        if(prevState.post_id !== this.state.post_id){
            this.getPost();
        }
    }

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
        this.getPost();
    }

    getPost(){
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
                if(err.response.status === 404){
                    this.props.history.push('/404');
                }
            });
    }
    render() {
        const { classes,t } =  this.props;

        if(this.state.post === undefined)
            return <LinearProgress />;

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