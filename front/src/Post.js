import {
    Card,
    CardActionArea,
    CardContent,
    Typography,
    Button,
    CardActions,
} from "@material-ui/core";

import { Component } from "react";

class Post extends Component {
    render() {
        return (
            <Card>
                <CardContent>
                    <Typography gutterBottom variant="h5" component="h2">
                        {"Esto es título"}
                    </Typography>
                    <Typography
                        variant="body2"
                        color="textSecondary"
                        component="p"
                    >
                        {"Esto es una descripcion"}
                    </Typography>
                </CardContent>
            </Card>
        );
    }
}

export default Post;
