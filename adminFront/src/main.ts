import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import * as Keycloak from 'keycloak-js';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

// platformBrowserDynamic().bootstrapModule(AppModule)
//   .catch(err => console.error(err));

//keycloak init options
let initOptions = {
  url: 'https://localhost:8443/auth', 
  realm: 'BSEPT4', 
  clientId: 'PKIFrontend',
  'enable-cors': true,
  initOptions: {
    flow: 'implicit',
  }
}

let keycloak = Keycloak(initOptions);

keycloak.init({ onLoad: "login-required" }).success((auth) => {

  if (!auth) {
    window.location.reload();
  } else {
    console.log("Authenticated");
  }

  //bootstrap after authentication is successful.
  platformBrowserDynamic().bootstrapModule(AppModule)
    .catch(err => console.error(err));


  localStorage.setItem("jwtToken", keycloak.token);
  localStorage.setItem("ang-refresh-token", keycloak.refreshToken);

  setTimeout(() => {
    keycloak.updateToken(70).success((refreshed) => {
      if (refreshed) {
        console.debug('Token refreshed' + refreshed);
      } else {
        console.warn('Token not refreshed, valid for '
          + Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000) + ' seconds');
      }
    }).error(() => {
      console.error('Failed to refresh token');
    });


  }, 60000)

}).error(() => {
  console.error("Authenticated Failed");
});