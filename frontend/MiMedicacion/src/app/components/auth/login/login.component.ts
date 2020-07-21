import { Component } from '@angular/core';
import { Router } from '@angular/router';

// FROMS
import { FormBuilder, FormGroup, AbstractControl } from '@angular/forms';
import { CustomValidators } from '../../../utils/custom-validators'

// SERVICES
import { UserService } from './../../../services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  public loginForm: FormGroup;
  public emailNotFound: string;
  public incorrectPassword: string;

  constructor(private fb: FormBuilder,
    private _userService: UserService,
    private router: Router) {

    /*
      terminated in l, of login, for do not have problems of unique
      names for present login and register components in the same view
    */
    this.loginForm = fb.group({
      emaill: ['', [
        CustomValidators.required,
        CustomValidators.email,
        CustomValidators.maxLength(250)
      ]],
      passwordl: ['', [
        CustomValidators.required,
        CustomValidators.minLength(6)
      ]]
    });
  }

  public login(){
    this.emailNotFound = null;
    this.incorrectPassword = null;
    // the keys was changed for do not have problems on views, but backend needs email and password as keys
    let data: any = {
      email: this.emaill.value,
      password: this.passwordl.value
    };
    this._userService.login(data)
    .subscribe({
      next: ()=>{
        this.router.navigate(['/home']);
      },
      error: (err)=>{
        if(err.error.errors.email) this.emailNotFound = "Este correo no ha sido registrado";
        if(err.error.errors.password) this.incorrectPassword = "Contraseña incorrecta";
      }
    });
  }

  // GETTERS
  get emaill(): AbstractControl {
    return this.loginForm.get('emaill');
  }

  get passwordl(): AbstractControl {
    return this.loginForm.get('passwordl');
  }

}
