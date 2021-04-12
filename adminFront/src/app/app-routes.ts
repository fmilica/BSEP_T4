import { Routes } from "@angular/router";
import { CertificatesComponent } from "./components/certificates/certificates.component";
import { CreateCertificateComponent } from "./components/create-certificate/create-certificate.component";
import { CsrComponent } from "./components/csr/csr.component";
import { HomepageComponent } from "./components/homepage/homepage.component";
import { LoginComponent } from "./components/login/login.component";
import { LogsComponent } from "./components/logs/logs.component";
import { UsersComponent } from "./components/users/users.component";
import { LoginGuard } from "./guards/login-guard.service";
import { RoleGuard } from "./guards/role-guard.service";

export const routes: Routes = [
    {
      path: '',
      redirectTo: '/login',
      pathMatch: 'full',
    },
    {
        path: 'login',
        component: LoginComponent,
        canActivate: [LoginGuard]
    },
    {
      path: 'homepage',
      component: HomepageComponent,
      //canActivate: [RoleGuard],
      //data: { expectedRoles: 'ROLE_SUPER_ADMIN' },
      children: [
        {
          path: '',
          redirectTo: 'csr',
          pathMatch: 'full',
        },
        {
          path: 'csr',
          component: CsrComponent,
        },
        {
          path: 'certificates',
          component: CertificatesComponent,
        },
        {
          path: 'create-certificate',
          component: CreateCertificateComponent,
        },
        {
          path: 'users',
          component: UsersComponent,
        },
        {
          path: 'logs',
          component: LogsComponent,
        }
      ],
    },
    {
      path: '**',
      component: LoginComponent,
    }
];