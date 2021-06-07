import { Routes } from "@angular/router";
import { CreateAlarmComponent } from "./components/create-alarm/create-alarm.component";
import { CreateCsrComponent } from "./components/create-csr/create-csr.component";
import { CreateLogRuleComponent } from "./components/create-log-rule/create-log-rule.component";
import { HomepageComponent } from "./components/homepage/homepage.component";
import { LogAlarmsComponent } from "./components/log-alarms/log-alarms.component";
import { LoginComponent } from "./components/login/login.component";
import { LogsComponent } from "./components/logs/logs.component";
import { PatientAlarmComponent } from "./components/patient-alarm/patient-alarm.component";
import { PatientStatusComponent } from "./components/patient-status/patient-status.component";
import { PatientComponent } from "./components/patient/patient.component";
import { ReportsComponent } from "./components/reports/reports.component";
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
    children: [
      {
        path: 'csr',
        component: CreateCsrComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'ADMIN' },
      },
      {
        path: 'reports',
        component: ReportsComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'ADMIN' },
      },
      {
        path: 'log',
        component: LogsComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'ADMIN|SUPER_ADMIN' },
      },
      {
        path: 'log-alarms',
        component: LogAlarmsComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'ADMIN|SUPER_ADMIN' },
      },
      {
        path: 'patients',
        component: PatientComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'DOCTOR' },
      },
      {
        path: 'patient-statuses',
        component: PatientStatusComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'DOCTOR' },
      },
      {
        path: 'patient-alarms',
        component: PatientAlarmComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'DOCTOR' },
      },
      {
        path: 'create-alarm',
        component: CreateAlarmComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'DOCTOR' },
      },
      {
        path: 'create-log-alarm',
        component: CreateLogRuleComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'ADMIN' },
      },
    ],
  },
  {
    path: '**',
    component: LoginComponent,
  }
];