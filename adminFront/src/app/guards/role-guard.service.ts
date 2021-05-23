import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { ToastrService } from 'ngx-toastr';

@Injectable({
    providedIn: 'root'
})
export class RoleGuard implements CanActivate {

    constructor(private router: Router,
        private toastr: ToastrService) { }

    canActivate(route: ActivatedRouteSnapshot): boolean {
        const expectedRoles: string = route.data.expectedRoles;
        const token = sessionStorage.getItem('jwtToken');
        const jwt: JwtHelperService = new JwtHelperService();

        if (!token) {
            this.router.navigate(['login']);
            this.toastr.error('401 Unauthorized access');
            return false;
        }

        const info = jwt.decodeToken(token);
        const roles: string[] = expectedRoles.split('|', 2);

        const userRoles = info.realm_access.roles;
        let ind = false
        for (let role in userRoles) {
            if (roles.indexOf(userRoles[role]) !== -1) {
                ind = true;
                break;
            }
        }
        if (!ind) {
            this.router.navigate(['unauthorized']);
            this.toastr.error('403 Unauthorized access');
            return false;
        }
        return true;
    }

}
