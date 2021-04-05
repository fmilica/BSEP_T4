import { Routes } from "@angular/router";
import { CreateCsrComponent } from "./components/create-csr/create-csr.component";
import { HomepageComponent } from "./components/homepage/homepage.component";
import { LoginComponent } from "./components/login/login.component";


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
          component: CreateCsrComponent,
          //canActivate: [LoginGuard],
        },
      ],
    }, 
];