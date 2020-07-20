import { Component, OnInit, Input } from '@angular/core';

// FORMS
import { FormGroup, FormBuilder, AbstractControl, FormControl } from '@angular/forms';
import { CustomValidators } from '../../utils/custom-validators';

// ngBootstrap
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// INTERFACES
import { Medication } from './../../interfaces/medication';

// SERVICES
import { MedicationService } from './../../services/medication.service';

@Component({
  selector: 'app-medication-modal',
  templateUrl: './medication-modal.component.html',
  styleUrls: ['./medication-modal.component.css']
})
export class MedicationModalComponent implements OnInit {
  @Input('medication') public medication: Medication;
  public medicationForm: FormGroup;

  constructor(public modalService: NgbActiveModal,
    private _medicationService: MedicationService,
    private fb: FormBuilder) {

      this.medicationForm = fb.group({
        name: ['', [
          CustomValidators.minLength(1),
          CustomValidators.maxLength(200)
        ]],
        description: ['', [
          CustomValidators.maxLength(1000)
        ]],
        hours_interval: ['', [
          CustomValidators.min(0),
          CustomValidators.max(255)
        ]]
      });
    }

  ngOnInit(): void {
    if(this.medication){
      this.medicationForm.addControl('finished', new FormControl([]));
      this.name.setValue(this.medication.name);
      this.description.setValue(this.medication.description);
      this.hours_interval.setValue(this.medication.hours_interval);
      this.finished.setValue(this.medication.finished);
    } else {
      this.name.setValidators([
        CustomValidators.required,
        CustomValidators.minLength(1),
        CustomValidators.maxLength(200)
      ]);
      this.hours_interval.setValidators([
        CustomValidators.required,
        CustomValidators.min(0),
        CustomValidators.max(255)
      ]);
    }
  }

  public send(): void {
    if(this.medication) this.update();
    else this.store();
  }

  private store(): void {
    this._medicationService.store(this.medicationForm.value)
    .subscribe({
      next: (resp)=>{
        console.log(resp);
        this.modalService.close({medication: resp.data});
      },
      error: (error)=>{
        console.log(error);
      }
    });
  }

  private update(): void {
    this._medicationService.update(this.medication.id, this.medicationForm.value)
    .subscribe({
      next: (resp)=>{
        console.log(resp);
        this.modalService.close({medication: resp.data});
      },
      error: (error)=>{
        console.log(error);
      }
    });
  }

  // getters
  get name(): AbstractControl{
    return this.medicationForm.get('name');
  }

  get description(): AbstractControl{
    return this.medicationForm.get('description');
  }

  get hours_interval(): AbstractControl{
    return this.medicationForm.get('hours_interval');
  }

  get finished(): AbstractControl{
    return this.medicationForm.get('finished');
  }

}
