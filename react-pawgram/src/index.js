import React, { Component, Suspense } from "react";
import ReactDOM from "react-dom";
import { I18nextProvider} from "react-i18next";
import i18n from "./i18n/i18n";

import './index.css';
import App from './components/App/App';
import * as serviceWorker from './serviceWorker';
import LinearProgress from '@material-ui/core/LinearProgress';
ReactDOM.render(
    <I18nextProvider i18n={i18n}>
        <Suspense fallback={<div><LinearProgress /></div>}>
        <App />
        </Suspense>
    </I18nextProvider>,
    document.getElementById("root")
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
