import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

// INTERFACES
import { User } from '../interfaces/user';

// SERVICES
import { HttpService } from './http.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private _user: User;
  private _token: string;

  constructor(private http: HttpService) {}

  /**
   * centralize the storage of the auth user data:
   * user and token into variables and localStorage for next sesions
   * 
   * @param user User
   * @param token string
   */
  private saveAuth(user: User, token: string = null): void{
    this._user = user;
    localStorage.setItem("user", JSON.stringify(this._user));
    if(token) {
      this._token = token;
      localStorage.setItem("token", token);
    }
  }

  /**
   * read localStorage to get previously stored authentication data
   */
  public readAuth(): Promise<boolean>{
    return new Promise((resolve)=>{
      // in case of not found, the variables will just be null
      this._user = JSON.parse(localStorage.getItem("user"));
      this._token = localStorage.getItem("token");
      if(this._user && this._token) resolve(true);
      else resolve(false);
    });
    
  }

  /**
   * Logout, clears the user, token localStorage and variables
   */
  public logout(): void{
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    this._user = null;
    this._token = null;
  }

  // HTTP CALLS
  /**
   * Register a new user
   * 
   * @param user User
   */
  public register(user: User): Observable<any>{
    return this.http.post('auth/register', user)
    .pipe(
      map((resp:any)=>{
        if(resp.ok) this.saveAuth(resp.user, resp.token);
        return resp;
      })
    );
  }

  /**
   * Login
   * 
   * @param user User
   */
  public login(user: User): Observable<any>{
    return this.http.post(`auth/login`, user)
    .pipe(
      map((resp:any)=>{
        if(resp.ok) this.saveAuth(resp.user, resp.token);
        return resp;
      })
    );
  }

  /**
   * Refresh the auth user data, used OnInit at app.component
   */
  public refreshUserData(): Observable<any>{
    return this.http.get('auth/me', {Authorization: this.token})
    .pipe(
      map((resp)=>{
        if(resp.ok){
          this.saveAuth(resp.user);
        }
        return resp;
      })
    );
  }

  public update(user: User): Observable<any>{
    return this.http.put('users', user, {Authorization: this.token})
    .pipe(
      map((resp)=>{
        if(resp.ok){
          this.saveAuth(resp.data);
        }
        return resp;
      })
    );
  }

  public uploadPicture(picture: File): Observable<any>{
    return this.http.post('users/picture', {picture}, {Authorization: this.token}, true)
    .pipe(
      map((resp)=>{
        if(resp.ok){
          this.saveAuth(resp.data);
        }
        return resp;
      })
    );
  }


  // GETTERS
  get user(): User{
    return this._user;
  }

  get token(): string{
    return this._token;
  }
}
