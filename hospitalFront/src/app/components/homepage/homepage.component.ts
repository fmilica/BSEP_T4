import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.sass']
})
export class HomepageComponent implements OnInit {

  role = '';
  subscription!: Subscription;

  constructor(
    private authenticationService: AuthenticationService,
    private toastr: ToastrService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.role = this.authenticationService.getLoggedInUserRole()
    console.log(this.role)
  }

  logout(): void {
    this.authenticationService.logout();
    this.toastr.info('Logged out successfully!');
  }

  toSuperAdmin(): void {
    window.location.href = "https://localhost:4200/homepage/csr";
  }

}
