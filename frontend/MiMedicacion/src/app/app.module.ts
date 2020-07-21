import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

// FROMS
import { ReactiveFormsModule } from '@angular/forms';

// HTTP
import { HttpClientModule } from '@angular/common/http';

// ngBootstrap
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
// COMPONENTS
import { AppComponent } from './app.component';
import { AuthComponent } from './components/auth/auth.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { FormErrorMessagesComponent } from './components/shareds/form-error-messages/form-error-messages.component';
import { LoginComponent } from './components/auth/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { NavbarComponent } from './components/shareds/navbar/navbar.component';
import { FooterComponent } from './components/shareds/footer/footer.component';
import { UserModalComponent } from './components/user-modal/user-modal.component';
import { MedicationModalComponent } from './components/medication-modal/medication-modal.component';
import { ToastComponent } from './components/shareds/toast/toast.component';

// PIPES
import { ImagePipe } from './pipes/image.pipe';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    RegisterComponent,
    FormErrorMessagesComponent,
    LoginComponent,
    HomeComponent,
    NavbarComponent,
    FooterComponent,
    ImagePipe,
    UserModalComponent,
    MedicationModalComponent,
    ToastComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
