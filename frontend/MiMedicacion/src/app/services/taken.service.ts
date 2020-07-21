import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

// SERVICES
import { HttpService } from './http.service';
import { UserService } from './user.service';


@Injectable({
  providedIn: 'root'
})
export class TakenService {

  constructor(private http: HttpService,
    private _userService: UserService) { }

  /**
    * Returns all the taken rows for a medication
    *  
    * @param id number
    */
  public getAllTakensOfAMedication(id: number): Observable<any>{
    return this.http.get(`taken/all/medication/${id}`, {Authorization: this._userService.token});
  }

  /**
   * Set a last taken for a medication
   * 
   * @param id number
   */
  public setLastTaken(id: number): Observable<any>{
    return this.http.post(`taken/${id}`, {}, {Authorization: this._userService.token});
  }

  /**
   * Delete a taken
   * 
   * @param id number
   */
  public delete(id: number): Observable<any>{
    return this.http.delete(`taken/${id}`, {Authorization: this._userService.token});
  }
}
