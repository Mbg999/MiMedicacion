import { Component } from '@angular/core';
import { Router } from '@angular/router';

// FROMS
import { FormBuilder, FormGroup, AbstractControl } from '@angular/forms';
import { CustomValidators } from '../../../utils/custom-validators'

// SERVICES
import { UserService } from './../../../services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  public registerForm: FormGroup;
  public takenEmail: string;

  constructor(private fb: FormBuilder,
    private _userService: UserService,
    private router: Router) {

    this.registerForm = fb.group({
      email: ['', [
        CustomValidators.required,
        CustomValidators.email,
        CustomValidators.maxLength(250)
      ]],
      password: ['', [
        CustomValidators.required,
        CustomValidators.minLength(6)
      ]],
      password_confirmation: ['', CustomValidators.required],
      name: ['', [
        CustomValidators.required,
        CustomValidators.minLength(3),
        CustomValidators.maxLength(200)
      ]],
      born_date: ['', [
        CustomValidators.required,
        CustomValidators.bornDate
      ]]
    }, { validator: CustomValidators.MustMatch('password', 'password_confirmation') });
  }

  public register(){
    this.takenEmail = null;
    this._userService.register(this.registerForm.value)
    .subscribe({
      next: ()=>{
        this.router.navigate(['/home']);
      },
      error: (err)=>{
        if(err.error.errors.email) this.takenEmail = "Correo ya en uso";
      }
    });
  }

  // GETTERS
  get email(): AbstractControl {
    return this.registerForm.get('email');
  }

  get password(): AbstractControl {
    return this.registerForm.get('password');
  }

  get password_confirmation(): AbstractControl {
    return this.registerForm.get('password_confirmation');
  }

  get name(): AbstractControl {
    return this.registerForm.get('name');
  }

  get born_date(): AbstractControl {
    return this.registerForm.get('born_date');
  }

}
