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
import { UserListComponent } from './components/user-list/user-list.component';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { DoughnutChartComponent } from './components/doughnut-chart/doughnut-chart.component';
import { LottieModule } from 'ngx-lottie';
import player from 'lottie-web';
import { LottieAnimationViewComponent } from './components/lottie-animation-view/lottie-animation-view.component';

export function playerFactory() {
  return player;
}


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
    UserListComponent,
    ConfirmDialogComponent,
    UserFormComponent,
    DoughnutChartComponent,
    LottieAnimationViewComponent
    
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
    LottieModule.forRoot({ player: playerFactory })
    
  ],
  entryComponents:[
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
