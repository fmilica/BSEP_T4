import { Routes } from "@angular/router";
import { CertificatesComponent } from "./components/certificates/certificates.component";
import { CreateCertificateComponent } from "./components/create-certificate/create-certificate.component";
import { CsrComponent } from "./components/csr/csr.component";
import { HomepageComponent } from "./components/homepage/homepage.component";
import { LoginComponent } from "./components/login/login.component";
import { LogsComponent } from "./components/logs/logs.component";
import { UsersComponent } from "./components/users/users.component";

export const routes: Routes = [
    {
      path: '',
      component: LoginComponent,
      pathMatch: 'full'
    },
    {
      path: 'homepage',
      component: HomepageComponent,
      // canActivate: [LoginGuard],
      children: [
        {
          path: '',
          redirectTo: 'csr',
          pathMatch: 'full',
        },
        {
          path: 'csr',
          component: CsrComponent,
          //canActivate: [LoginGuard],
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
];