import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

// HTTP
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

// URL
import { environment } from './../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private http: HttpClient) {}

  /**
   * HTTP GET call
   * 
   * @param path string
   * @param options Object, default {}
   */
  public get(path: string, options: Object = {}): Observable<any>{
    return this.http.get(`${environment.backend_url}/api/${path}`, this.setOptions(options));
  }

  /**
   * HTTP POST call
   * 
   * @param path string
   * @param data any
   * @param options Object, default {}
   * @param isMultipart boolean, default false
   */
  public post(path: string, data: any, options: Object = {}, isMultipart: boolean = false): Observable<any>{
    let form: any;
    if(isMultipart){ // let the navigator set the Content-Type automatically
      form = this.processMultipart(data);
    } else {
      form = this.processUrlencoded(data);
    }

    return this.http.post(`${environment.backend_url}/api/${path}`, form, this.setOptions(options));
  }

  /**
   * HTTP PUT call
   * 
   * @param path string
   * @param data any
   * @param options Object, default {}
   * @param isMultipart boolean, default false
   */
  public put(path: string, data: any, options: Object = {}, isMultipart: boolean = false): Observable<any>{
    let form: any;
    if(isMultipart){ // let the navigator set the Content-Type automatically
      form = this.processMultipart(data);
    } else {
      form = this.processUrlencoded(data);
    }

    return this.http.put(`${environment.backend_url}/api/${path}`, form, this.setOptions(options));
  }

  /**
   * HTTP DELETE call
   * 
   * @param path string
   * @param options Object, default {}
   */
  public delete(path: string, options: Object = {}): Observable<any>{
    return this.http.delete(`${environment.backend_url}/api/${path}`, this.setOptions(options));
  }

  /**
   * Sets the HTTP options
   * 
   * currently only sets the headers,
   * the response type will be always JSON (default)
   * 
   * @param options Object
   */
  private setOptions(options: Object): Object{
    let headers = {};

    Object.keys(options).forEach((key)=>{
      headers[key] = options[key];
    });

    return {
      headers: new HttpHeaders(headers)
    }
  }

  /**
   * Process data to build a multipart/form-data form
   * 
   * @param data any
   */
  private processMultipart(data: any): FormData{
    let form = new FormData();

    Object.keys(data).forEach((key)=>{
      if(data[key].name){
        form.set(key, data[key], data[key].name);
      } else {
        form.set(key, data[key]);
      }
    });

    return form;
  }

  /**
   * Process data to build a application/x-www-form-urlencoded form
   * 
   * @param data any
   */
  private processUrlencoded(data: any): HttpParams{
    let form = new HttpParams();

    Object.keys(data).forEach((key)=>{
      form = form.set(key, data[key]);
    });

    return form;
  }
}
