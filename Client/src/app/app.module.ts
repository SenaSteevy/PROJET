import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from './material.module';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { ScheduleComponent } from './pages/schedule/schedule.component';
import { UsersComponent } from './pages/users/users.component';
import { StocksComponent } from './pages/stocks/stocks.component';
import { ProductionLineComponent } from './pages/production-line/production-line.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { FooterComponent } from './components/footer/footer.component';
import { DialogComponent } from './components/dialog/dialog.component';
import { CardComponent } from './components/card/card.component';
import { OrdersComponent } from './pages/orders/orders.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    ScheduleComponent,
    UsersComponent,
    StocksComponent,
    ProductionLineComponent,
    SettingsComponent,
    FooterComponent,
    DialogComponent,
    CardComponent,
    OrdersComponent,
    
  
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    MaterialModule,
    ReactiveFormsModule,
    HttpClientModule,
    
  ],
  entryComponents:[
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
