import {Component, TemplateRef} from '@angular/core';

// SERVICES
import { ToastService } from './../../../services/toast.service';
/**
 * https://www.freakyjolly.com/angular-bootstrap-adding-toasts-in-angular-app-using-ng-bootstrap-tutorial/#.XsmZO2gzaUk
 */
@Component({
  selector: 'app-toasts',
  template: `
    <ngb-toast
      *ngFor="let toast of toastService.toasts"
      [header]="toast.headertext"
      [class]="toast.classname"
      [autohide]="toast.autohide"
      [delay]="toast.delay || 5000"
      (hide)="toastService.remove(toast)"
    >
      <ng-template [ngIf]="isTemplate(toast)" [ngIfElse]="text">
        <ng-template [ngTemplateOutlet]="toast.textOrTpl"></ng-template>
      </ng-template>

      <ng-template #text>
        <span>{{ toast.textOrTpl }}</span>
      </ng-template>
    </ngb-toast>
  `,
  host: {'[class.ngb-toasts]': 'true'}
})
export class ToastComponent {
  constructor(public toastService: ToastService) {}

  isTemplate(toast) { return toast.textOrTpl instanceof TemplateRef; }
}