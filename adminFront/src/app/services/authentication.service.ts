import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {

  constructor() { }

  logout(): void {
    sessionStorage.removeItem('jwtToken');
    sessionStorage.removeItem('ang-refresh-token');
    window.location.href = 'https://localhost:8443/auth/realms/BSEPT4/protocol/openid-connect/logout?redirect_uri=https://localhost:4200';
  }

}
