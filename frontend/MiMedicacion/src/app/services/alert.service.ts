import { Injectable } from '@angular/core';

// SWEETALERT
import Swal, { SweetAlertResult, SweetAlertIcon } from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  /**
   * Shows a simple text alert
   * 
   * @param title string
   * @param text string
   * @param icon SweetAlertIcon
   */
  public simpleText(title: string, text: string, icon: SweetAlertIcon){
    return Swal.fire(title, text, icon);
  }

  /**
   * Shows a choice alert, select between accept or cancel buttons
   * 
   * @param title string
   * @param text string
   * @param icon SweetAlertIcon
   * @param confirmButtonText string 
   * @param cancelButtonText string
   * @param confirmButtonColor string, default #dc3545 (danger)
   * @param cancelButtonColor string, default #007bff (primary)
   */
  public choice(title: string, text: string, icon: SweetAlertIcon, confirmButtonText: string,
    cancelButtonText: string, confirmButtonColor: string = "#dc3545", cancelButtonColor: string = "#007bff"){

      return Swal.fire({
        title,
        text,
        icon,
        showCancelButton: true,
        focusCancel: true,
        confirmButtonText,
        confirmButtonColor,
        cancelButtonText,
        cancelButtonColor
      });
  }

}
