import { UserModalComponent } from './../../user-modal/user-modal.component';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

// SERVICES
import { UserService } from './../../../services/user.service';

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
    private modalService: NgbModal) { }


  public openUserModal(){
    this.isCollapsed = true;
    this.modalService.open(UserModalComponent, {
      scrollable: true,
      size: 'xl',
      windowClass: 'animated fadeInDown faster'
    });
  }

  public logout(){
    this.isCollapsed = true;
    this._userService.logout();
    this.router.navigate(['/auth']);
  }

  // getters
  get user(): User{
    return this._userService.user;
  }

}
