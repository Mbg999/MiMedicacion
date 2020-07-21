import { Observable } from 'rxjs';

import { Injectable } from '@angular/core';

// SERVICES
import { HttpService } from './http.service';
import { UserService } from './user.service';

// INTERFACES
import { Medication } from './../interfaces/medication';

@Injectable({
  providedIn: 'root'
})
export class MedicationService {

  constructor(private http: HttpService,
    private _userService: UserService) { }

  // HTTP CALLS
  /**
   * Get all the medications from the auth user
   */
  public getAllAuthMedications(): Observable<any>{
    return this.http.get('medications/all/user', {Authorization: this._userService.token});
  }

  /**
   * Store a new medication
   * 
   * @param data Medication
   */
  public store(data: Medication){
    return this.http.post('medications', data, {Authorization: this._userService.token});
  }

  /**
   * Update a medication
   * the data will only have the data to update,
   * so it will not has the id
   * 
   * @param id number
   * @param data Medication
   */
  public update(id: number, data: Medication){
    return this.http.put(`medications/${id}`, data, {Authorization: this._userService.token});
  }

  /**
   * Delete a medication
   * 
   * @param id number
   */
  public delete(id: number){
    return this.http.delete(`medications/${id}`, {Authorization: this._userService.token});
  }

}
