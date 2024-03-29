import React from 'react';
import { Redirect } from 'react-router';
import PropTypes from 'prop-types';
import Grid from "@material-ui/core/Grid"
import { withStyles } from '@material-ui/core/styles/index';
import {withTranslation} from "react-i18next";
import PostCard from '../PostCard/PostCard'
import {AuthService} from "../../../services/AuthService";

const styles = theme => ({
    root: {
        display: "grid",
        gridTemplateColumns: "repeat(4, 1fr)",
        gridGap: "24px",
    },
});

class PostCardsGrid extends React.Component {
    state = {data: this.props.posts};

    componentDidMount() {
        if (!AuthService().isLoggedIn()){
            this.props.history.push('/login');
        }
    }

    componentDidUpdate(prevProps,prevState){
        if(prevProps.posts !== this.props.posts){
            this.setState({data: this.props.posts});
        }
    }

    drawPostAll(){
        let table = [];
        for (let i = 0; i < this.state.data.posts.length ; i++) {
            table.push(<Grid item key={i} xs={4}><PostCard post={this.state.data.posts[i]} /></Grid>);
        }

        return table;
    }

    render() {
        const { classes,t } =  this.props;

        if (this.state.data.count === 0 ){
            return (
                <div>
                    <h3>{t('posts')}</h3>
                    <div>{t('empty-posts')}</div>
                </div>
            );
        }

        return (
            <div>
                <h3>{t('posts')}</h3>
                <Grid container spacing={8}>
                    {this.drawPostAll()}
                </Grid>

            </div>
        );

    }


}

PostCardsGrid.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostCardsGrid));