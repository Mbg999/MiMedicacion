import { Component, OnInit } from '@angular/core';

// SERVICES
import { MedicationService } from './../../services/medication.service';
import { AlertService } from './../../services/alert.service';
import { TakenService } from './../../services/taken.service';

// INTERFACES
import { Medication } from './../../interfaces/medication';

// COMPONENT
import { MedicationModalComponent } from './../medication-modal/medication-modal.component';

// ngBootstrap
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public medications: Medication[];
  private notificationInterval: any;

  constructor(private _medicationService: MedicationService,
    private modalService: NgbModal,
    private _alertService: AlertService,
    private _takenService: TakenService) { }

  ngOnInit(): void {
    this._medicationService.getAllAuthMedications()
    .subscribe((resp)=>{
      this.medications = resp.data;
      if(this.medications[0]) this.getTakens(0);
      this.startNotificationLoop();
      console.log(this.medications);
    });
  }

  private startNotificationLoop(){
    // this.notificationInterval = setInterval(()=>{

    // }, 2500);
  }

  public stopNotifications(){
    clearInterval(this.notificationInterval);
    this.notificationInterval = null;
  }

  public create(): void{
    this.openMedicationModal()
    .then((result)=>{
      console.log(result);
      this.medications.unshift(result.medication);
    }).catch((err)=>{}); // catch for ngBootstrap modal requirement
  }

  public update(i: number): void{
    this.openMedicationModal(i)
    .then((result)=>{
      console.log(result);
      this.medications[i] = result.medication;
    }).catch((err)=>{}); // catch for ngBootstrap modal requirement
  }

  /**
   * Open the medication modal
   * index = update, no index = create
   * 
   * @param index number
   */
  private openMedicationModal(i: number = null): Promise<any>{
    const modalRef: NgbModalRef = this.modalService.open(MedicationModalComponent, {
      scrollable: true,
      size: 'xl',
      windowClass: 'animated fadeInDown faster' // for a smooth presentation
    });

    modalRef.componentInstance.medication = this.medications[i];

    return modalRef.result;
  }

  public toggleFinished(i: number): void{
    this._medicationService.update(this.medications[i].id, {finished: !this.medications[i].finished})
    .subscribe({
      next: (resp)=>{
        this.medications[i].finished = resp.data.finished;
        this.medications[i].updated_at = resp.data.updated_at;
        console.log(resp);
      },
      error: (err)=>{
        console.log(err);
      }
    });
  }

  public delete(i: number): void{
    this._alertService.choice("¿Eliminar medicación?", "El registro será eliminado permanentemente", "error", "Eliminar", "Cancelar")
    .then((result)=>{
      if(result.value){
        this._medicationService.delete(this.medications[i].id)
        .subscribe({
          next: (resp)=>{
            this.medications.splice(i, 1);
          },
          error: (err)=>{
            console.log(err);
          }
        });
      }
    });
  }

  public taken(i: number): void{
    this._takenService.setLastTaken(this.medications[i].id)
    .subscribe({
      next: (resp)=>{
        console.log(resp);
        this.medications[i].last_taken = resp.data;
        if(this.medications[i].takens){
          this.medications[i].takens.unshift(resp.data);
        } else {
          this.medications[i].takens = [resp.data];
        }
      },
      error: (err)=>{
        console.log(err);
      }
    });
  }

  public getTakens(i: number): void{
    this._takenService.getAllTakensOfAMedication(this.medications[i].id)
    .subscribe({
      next: (resp)=>{
        this.medications[i].takens = resp.data;
      },
      error: (err)=>{
        console.log(err);
      }
    });
  }

  public deleteTaken(medicationI: number, takenI: number): void{
    this._alertService.choice("¿Eliminar registro de toma?", "El registro será eliminado permanentemente", "error", "Eliminar", "Cancelar")
    .then((result)=>{
      if(result.value){
        this._takenService.delete(this.medications[medicationI].takens[takenI].id)
        .subscribe({
          next: (resp)=>{
            if(this.medications[medicationI].last_taken.id == this.medications[medicationI].takens[takenI].id){
              if(this.medications[medicationI].takens[1]){
                this.medications[medicationI].last_taken = this.medications[medicationI].takens[1];
              } else {
                this.medications[medicationI].last_taken = null;
              }
            }
            this.medications[medicationI].takens.splice(takenI, 1);
            console.log(resp);
          },
          error: (err)=>{
            console.log(err);
          }
        });
      }
    });
  }

}
