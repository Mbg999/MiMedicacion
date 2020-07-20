import { UserService } from './user.service';

import { Injectable } from '@angular/core';

// SERVICES
import { HttpService } from './http.service';

@Injectable({
  providedIn: 'root'
})
export class MedicationService {

  constructor(private http: HttpService,
    private _userService: UserService) { }
}
