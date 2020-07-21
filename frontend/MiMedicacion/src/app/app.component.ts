import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

// SERVICES
import { UserService } from './services/user.service';
import { AlertService } from './services/alert.service';

// INTERFACES
import { User } from './interfaces/user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private _userService: UserService,
    private _alertService: AlertService,
    private router: Router){}
  
  /**
   * Check if the user was logged in a previous session
   * and refresh the user data
   */
  ngOnInit(): void {
    this._userService.readAuth().then((result)=>{
      if(result) {
        this._userService.refreshUserData().subscribe({
          error: (err)=>{
            this._userService.logout();
            this.router.navigate(['/auth']);
            this._alertService.simpleText(
              "Token no válido",
              "Puede que el token haya caducado, prueba a volver a iniciar sesión",
              "error"
            );
          }
        });
      }
    });
  }

  // GETTERS
  get user(): User{
    return this._userService.user;
  }
}
