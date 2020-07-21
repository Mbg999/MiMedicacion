import { Component, OnInit } from '@angular/core';

// SERVICES
import { MedicationService } from './../../services/medication.service';
import { AlertService } from './../../services/alert.service';
import { TakenService } from './../../services/taken.service';
import { UserService } from './../../services/user.service';
import { ToastService } from './../../services/toast.service';

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
  private dingSound: any;

  constructor(private _medicationService: MedicationService,
    private modalService: NgbModal,
    private _alertService: AlertService,
    private _takenService: TakenService,
    private _userService: UserService,
    private _toastService: ToastService) { }

  /**
   * Start the dingSound audio object
   * get all the auth user medications
   * if the user has some medications,
   * the notification loop will start
   */
  ngOnInit(): void {
    this.dingSound = new Audio();
    this.dingSound.src = "assets/ding.mp3";
    this._medicationService.getAllAuthMedications()
    .subscribe((resp)=>{
      this.medications = resp.data;
      if(this.medications.length > 0) {
        this.getTakens(0);
        this.startNotificationLoop();
      }
    });
  }

  /**
   * Parses the last taken date time to hours and subtracts the current date to hours
   * if the result is greater or equal to the hours interval between taking the medication
   * the user will be notified with a toast and a sound "ding", and the medication will be
   * marked as notified
   */
  private startNotificationLoop(){
    let ltd: Date;
    this.notificationInterval = setInterval(()=>{
      this.medications.map((medication)=>{ // reads every medication
        if(medication.last_taken && !medication.finished && !medication.notified){ // if had last_taken time and is not finished
          ltd = new Date(medication.last_taken.taken_at); // set a Date object
          ltd.setSeconds(0); // im not going to get care about the exact seconds
          ltd.setMinutes(ltd.getMinutes() - this._userService.user.mins_before); // set the mins_before to notificate
          if(this.hoursDiff(ltd.getTime()) >= medication.hours_interval){ // if the hours since last taken is greater or equal to the hours interval between taking the medication
            this._toastService.show(`Hora de tomar ${medication.name}`, { // show notification toast
              classname: 'bg-info text-light',
              delay: 3500 ,
              autohide: true
            });
            if(this.dingSound.paused) this.dingSound.play(); // play "ding" sound
            medication.notified = true; // set medication as notified
          }
        } 
      });
    }, 2000);
  }

  /**
   * Stops the notification interval and set the variable to null
   */
  private stopNotifications(){
    clearInterval(this.notificationInterval);
    this.notificationInterval = null;
  }
  

  /**
   * Parses the last taken date time to hours and subtracts the current date to hours
   * 
   * @param ltd number
   */
  private hoursDiff(ltd: number): number{
    return Math.trunc(Math.trunc((new Date().getTime()-ltd)/1000/60/60) % 24);
  }

  /**
   * Open the medication modal for a new medication
   */
  public create(): void{
    this.openMedicationModal()
    .then((result)=>{
      this.medications.unshift(result.medication);
      if(this.medications.length < 2) this.getTakens(0);
      if(!this.notificationInterval) this.startNotificationLoop();
    }).catch((err)=>{}); // catch for ngBootstrap modal requirement
  }

  /**
   * Open the medication modal for update a medication
   * 
   * @param i number 
   */
  public update(i: number): void{
    this.openMedicationModal(i)
    .then((result)=>{
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

  /**
   * Toggles a medication between finished and not and update it
   * 
   * @param i number
   */
  public toggleFinished(i: number): void{
    this._medicationService.update(this.medications[i].id, {finished: !this.medications[i].finished})
    .subscribe((resp)=>{
      this.medications[i].finished = resp.data.finished;
      this.medications[i].updated_at = resp.data.updated_at;
      this.medications[i].notified = false;
    });
  }

  /**
   * Delete a medication
   * 
   * @param i number
   */
  public delete(i: number): void{
    this._alertService.choice("¿Eliminar medicación?", "El registro será eliminado permanentemente", "error", "Eliminar", "Cancelar")
    .then((result)=>{
      if(result.value){
        this._medicationService.delete(this.medications[i].id)
        .subscribe(()=>{
          this.medications.splice(i, 1);
          if(this.medications.length < 1) this.stopNotifications();
        });
      }
    });
  }

  /**
   * create a last taken medication time
   * 
   * @param i number
   */
  public taken(i: number): void{
    this._takenService.setLastTaken(this.medications[i].id)
    .subscribe((resp)=>{
      this.medications[i].last_taken = resp.data;
      this.medications[i].notified = false;
      if(this.medications[i].takens){
        this.medications[i].takens.unshift(resp.data);
      } else {
        this.medications[i].takens = [resp.data];
      }
    });
  }

  /**
   * Get the taken times for a medication
   * 
   * @param i number
   */
  public getTakens(i: number): void{
    this._takenService.getAllTakensOfAMedication(this.medications[i].id)
    .subscribe((resp)=>{
      this.medications[i].takens = resp.data;
    });
  }

  /**
   * Delete a taken time from a medication
   * 
   * @param medicationI number
   * @param takenI number
   */
  public deleteTaken(medicationI: number, takenI: number): void{
    this._alertService.choice("¿Eliminar registro de toma?", "El registro será eliminado permanentemente", "error", "Eliminar", "Cancelar")
    .then((result)=>{
      if(result.value){
        this._takenService.delete(this.medications[medicationI].takens[takenI].id)
        .subscribe(()=>{
          if(this.medications[medicationI].last_taken.id == this.medications[medicationI].takens[takenI].id){
            if(this.medications[medicationI].takens[1]){
              this.medications[medicationI].last_taken = this.medications[medicationI].takens[1];
            } else {
              this.medications[medicationI].last_taken = null;
            }
          }
          this.medications[medicationI].takens.splice(takenI, 1);
        });
      }
    });
  }

}
