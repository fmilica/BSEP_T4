import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthenticationService } from './services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'hospitalFront';

  constructor(
    private authService: AuthenticationService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void { }
}
