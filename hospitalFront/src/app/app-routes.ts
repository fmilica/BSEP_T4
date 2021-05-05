import { Routes } from "@angular/router";
import { CreateCsrComponent } from "./components/create-csr/create-csr.component";
import { HomepageComponent } from "./components/homepage/homepage.component";
import { LoginComponent } from "./components/login/login.component";
import { UnauthorizedComponent } from "./components/unauthorized/unauthorized.component";
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
      path: 'unauthorized',
      component: UnauthorizedComponent,
  },
    {
      path: 'homepage',
      component: HomepageComponent,
      canActivate: [RoleGuard],
      data: { expectedRoles: 'ADMIN' },
      children: [
        {
          path: '',
          redirectTo: 'csr',
          pathMatch: 'full',
        },
        {
          path: 'csr',
          component: CreateCsrComponent,
        },
      ],
    }, 
    {
      path: '**',
      component: LoginComponent,
    }
];