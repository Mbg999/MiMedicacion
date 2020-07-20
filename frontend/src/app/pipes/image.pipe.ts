import { Pipe, PipeTransform } from '@angular/core';
import { environment } from './../../environments/environment';

// SERVICES
import { UserService } from './../services/user.service';

@Pipe({
  name: 'image'
})
export class ImagePipe implements PipeTransform {

  constructor(private _userService: UserService){}

  transform(value: string): string {
    if(value){
      return `${environment.backend_url}/api/users/picture/${value}/${this._userService.token}`;
    }

    return 'assets/nopicture.png';
  }

}
