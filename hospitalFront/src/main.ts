import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import * as Keycloak from 'keycloak-js';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

//keycloak init options
let initOptions = {
  url: 'https://localhost:8443/auth',
  realm: 'BSEPT4',
  clientId: 'HospitalFrontend',
  'enable-cors': true,
}

let keycloak = Keycloak(initOptions);

keycloak.init({ onLoad: "login-required" }).then((auth) => {

  if (!auth) {
    window.location.reload();
  } else {
    console.log("Authenticated");
  }

  //bootstrap after authentication is successful.
  platformBrowserDynamic().bootstrapModule(AppModule)
    .catch(err => console.error(err));

  sessionStorage.setItem("jwtToken", keycloak.token);
  sessionStorage.setItem("ang-refresh-token", keycloak.refreshToken);
}).catch(() => {
  console.error("Authenticated Failed");
});

keycloak.onTokenExpired = () => {
  console.log('Token expired.')
  keycloak.updateToken(30).then((refreshed) => {
    if (refreshed) {
      console.log('Token refreshed.');
      sessionStorage.setItem("jwtToken", keycloak.token);
      sessionStorage.setItem("ang-refresh-token", keycloak.refreshToken);
    } else {
      console.log('Token not refreshed, valid for '
        + Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000) + ' more seconds.');
    }
  }).catch(() => {
    console.log('Failed to refresh token.');
    keycloak.clearToken();
  });
}
