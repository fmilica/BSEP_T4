import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './components/login/login.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { HomepageComponent } from './components/homepage/homepage.component';
import { CreateCsrComponent } from './components/create-csr/create-csr.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { RouterModule } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { AuthInterceptorService } from './interceptors/auth-interceptor.service';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatTreeModule } from '@angular/material/tree';
import { MatStepperModule } from '@angular/material/stepper';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatPaginatorModule} from '@angular/material/paginator';
import { UnauthorizedComponent } from './components/unauthorized/unauthorized.component';
import { LogsComponent } from './components/logs/logs.component';
import { PatientComponent } from './components/patient/patient.component';
import { PatientStatusComponent } from './components/patient-status/patient-status.component';
import { CreateAlarmComponent } from './components/create-alarm/create-alarm.component';
import { PatientAlarmComponent } from './components/patient-alarm/patient-alarm.component';
import { LogAlarmsComponent } from './components/log-alarms/log-alarms.component';
import { ReportsComponent } from './components/reports/reports.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomepageComponent,
    CreateCsrComponent,
    UnauthorizedComponent,
    LogsComponent,
    PatientComponent,
    PatientStatusComponent,
    CreateAlarmComponent,
    PatientAlarmComponent,
    LogAlarmsComponent,
    ReportsComponent,
  ],
  imports: [
    RouterModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatToolbarModule,
    MatTreeModule,
    MatStepperModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatProgressBarModule,
    MatPaginatorModule,
    ToastrModule.forRoot({
      positionClass: 'toast-top-right',
      timeOut: 2500,
    }),
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      // ako multi nije true ovo bi bio jedini interceptor i pregazio bi sve defaultne interceptore
      multi: true,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
