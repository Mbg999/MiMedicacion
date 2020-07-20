import { Injectable } from '@angular/core';

// SWEETALERT
import Swal, { SweetAlertResult, SweetAlertIcon } from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  public simpleText(title: string, text: string, icon: SweetAlertIcon){
    return Swal.fire(title, text, icon);
  }

}
