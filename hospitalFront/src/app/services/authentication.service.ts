import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {

  constructor() {
    this.jwtService = new JwtHelperService();
   }

  private jwtService: JwtHelperService;

  logout(): void {
    sessionStorage.removeItem('jwtToken');
    sessionStorage.removeItem('ang-refresh-token');
    window.location.href = 'https://localhost:8443/auth/realms/BSEPT4/protocol/openid-connect/logout?redirect_uri=https://localhost:4201';
  }

  getLoggedInUser(): any {
    const token = sessionStorage.getItem('jwtToken');
    if (!token) {
      return '';
    }
    const info = this.jwtService.decodeToken(token);
    return info;
  }

  getLoggedInUserRole(): string {
    const info = this.getLoggedInUser();
    const userRoles = info.realm_access.roles;
    for (let role in userRoles) {
        if (['ADMIN', 'SUPER_ADMIN', 'DOCTOR'].indexOf(userRoles[role]) !== -1) {
            return userRoles[role];
        }
    }
    return ''
  }

}
