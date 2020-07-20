import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

// FORMS
import { FormGroup, FormBuilder, AbstractControl } from '@angular/forms';
import { CustomValidators } from '../../utils/custom-validators';

// SERVICES
import { UserService } from './../../services/user.service';
import { AlertService } from './../../services/alert.service';

// INTERFACES
import { User } from './../../interfaces/user';

// ngBootstrap
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-user-modal',
  templateUrl: './user-modal.component.html',
  styleUrls: ['./user-modal.component.css']
})
export class UserModalComponent implements OnInit {

  public userForm: FormGroup;
  public pictureError: string;
  public updatedPicture: string;

  constructor(private _userService: UserService,
    public modalService: NgbActiveModal,
    private fb: FormBuilder,
    private _alertService: AlertService,
    private router: Router) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      password: ['', [
        CustomValidators.minLength(6)
      ]],
      password_confirmation: [''],
      name: [this.user.name, [
        CustomValidators.minLength(3),
        CustomValidators.maxLength(200)
      ]],
      born_date: [this.user.born_date, [
        CustomValidators.bornDate
      ]],
      mins_before: [this.user.mins_before, [
        CustomValidators.min(0),
        CustomValidators.max(255)
      ]]
    }, { validator: CustomValidators.MustMatch('password', 'password_confirmation') });
  }

  public update(): void{
    this._userService.update(this.dataToUpdate())
    .subscribe(()=>{
      this._alertService.simpleText("Correctamente actualizado", "", "success")
      .then(()=>this.modalService.close());
    });
  }

  public uploadPicture(picture: HTMLInputElement){
    this.updatedPicture = null;
    this.pictureError = null;
    this._userService.uploadPicture(picture.files[0])
    .subscribe({
      next: ()=>{
        picture.value = ""; // clear the input field
        this.updatedPicture = "Imagen correctamente actualizada";
      },
      error: (err)=>{
        console.log(err);
        picture.value = ""; // clear the input field
        if(err.error.message === "Not a valid file extension") this.pictureError = "Has de seleccionar un archivo de imagen válido. Se permiten las extensiones .png, .jpg y .jpeg";
      }
    });
  }

  public delete(){
    this._alertService.choice("¿Estas seguro?", "Se eliminará tu cuenta de forma permanente", "error", "Eliminar", "Cancelar")
    .then((result)=>{
      if(result.value){
        this._userService.delete()
        .subscribe({
          next: (resp)=>{
            this.modalService.close();
            this.router.navigate(['/auth']);
            this._alertService.simpleText("Correctamente eliminado", "Tu usuario ha sido eliminado correctamente", "success");
          },
          error: (err)=>{ // really weird to happen
            console.log(err);
          }
        });
      }
    });
  }

  /**
   * returns only the updated user fields
   */
  private dataToUpdate(): User{
    let user: User = {};

    if(this.password.value){
      user.password = this.password.value;
    }

    if(this.name.value && this.name.value != this.user.name){
      user.name = this.name.value;
    }

    if(this.born_date.value && this.born_date.value != this.user.born_date){
      user.born_date = this.born_date.value;
    }

    if(this.mins_before.value > -1 && this.mins_before.value != this.user.mins_before){
      user.mins_before = this.mins_before.value;
    }

    return user;
  }

  // GETTERS
  get user(): User{
    return this._userService.user;
  }

  get password(): AbstractControl {
    return this.userForm.get('password');
  }

  get password_confirmation(): AbstractControl {
    return this.userForm.get('password_confirmation');
  }

  get name(): AbstractControl {
    return this.userForm.get('name');
  }

  get born_date(): AbstractControl {
    return this.userForm.get('born_date');
  }

  get mins_before(): AbstractControl {
    return this.userForm.get('mins_before');
  }

}
