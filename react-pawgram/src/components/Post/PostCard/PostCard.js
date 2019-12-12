import React, {Component} from 'react';
import { makeStyles } from '@material-ui/core/styles/index';
import Card from '@material-ui/core/Card/index';
import CardActionArea from '@material-ui/core/CardActionArea/index';
import CardHeader from '@material-ui/core/CardHeader';
import CardActions from '@material-ui/core/CardActions/index';
import CardContent from '@material-ui/core/CardContent/index';
import CardMedia from '@material-ui/core/CardMedia/index';
import Button from '@material-ui/core/Button/index';
import Typography from '@material-ui/core/Typography/index';
import { withStyles } from '@material-ui/core/styles/index';
import { withTranslation } from 'react-i18next';
import { red } from '@material-ui/core/colors';
import Chip from '@material-ui/core/Chip';
import DoneIcon from '@material-ui/icons/Done';
import {Link as LinkDom} from "react-router-dom";
import Link from "@material-ui/core/Link";
import {Config} from "../../../services/Config";
import PropTypes from "prop-types";

const styles = theme =>({
    card: {
        maxWidth: 345,
    },
    chip: {
        backgroundColor: red[500],
    },
});

class PostCard extends Component {


    constructor(props, context) {
        super(props, context);
        this.state = {
        };
    }

    render() {
        const { classes, post, t } = this.props;
        return (
            <Card className={classes.card}>
                <CardActionArea>
                    <CardMedia
                        component="img"
                        image={post.image_urls.length > 0? post.image_urls[0]: Config.DEFAULT_POST_URL}
                        alt=""
                        height="140"
                        title=""
                    />
                    <CardContent>
                        <Chip
                            icon={<DoneIcon />}
                            label="Clickable deletable"
                        />
                        <Typography gutterBottom variant="h5" component="h2">
                            {post.title}
                        </Typography>
                        <Typography variant="body2" color="textSecondary" component="p">
                            {post.description}
                        </Typography>
                    </CardContent>
                </CardActionArea>
                <CardActions>
                    <Button size="small" color="primary">
                        <Link component={ LinkDom } to={"/post/"+post.id} variant="body2">
                            {t('more-details')}
                        </Link>
                    </Button>
                </CardActions>
            </Card>
        );
    }
}

PostCard.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withTranslation()(PostCard));