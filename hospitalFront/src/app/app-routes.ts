import { Routes } from "@angular/router";
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
    }, 
];