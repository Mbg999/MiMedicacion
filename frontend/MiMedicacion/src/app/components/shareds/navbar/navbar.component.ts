import { UserModalComponent } from './../../user-modal/user-modal.component';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

// SERVICES
import { UserService } from './../../../services/user.service';
import { AlertService } from './../../../services/alert.service';

// INTERFACES
import { User } from './../../../interfaces/user';

// ngBootstrap
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  public isCollapsed: boolean = true;

  constructor(private _userService: UserService,
    private router: Router,
    private modalService: NgbModal,
    private _alertService: AlertService) { }


  public openUserModal(){
    this.isCollapsed = true;
    this.modalService.open(UserModalComponent, {
      scrollable: true,
      size: 'xl',
      windowClass: 'animated fadeInDown faster' // for a smooth presentation
    });
  }

  public logout(){
    this._alertService.choice("Cerrar sesión", "¿Desea cerrar sesión?", "question" ,"Si", "Cancelar")
    .then((result)=>{
      if(result.value){
        this.isCollapsed = true;
        this._userService.logout();
        this.router.navigate(['/auth']);
      }
    });
  }

  // GETTERS
  get user(): User{
    return this._userService.user;
  }

}
