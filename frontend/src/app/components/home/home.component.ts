import { Component, OnInit } from '@angular/core';

// SERVICES
import { MedicationService } from './../../services/medication.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private _medicationService: MedicationService) { }

  ngOnInit(): void {
  }

}
