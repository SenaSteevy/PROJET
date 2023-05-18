import {NgModule} from '@angular/core';
// Angular Material Components
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import {MatTabsModule} from '@angular/material/tabs';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatDialogModule} from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import {MatCardModule} from '@angular/material/card';


@NgModule({
    imports: [
        MatSidenavModule,
        MatIconModule,
        MatButtonModule,
        MatListModule,
        MatToolbarModule,
        MatMenuModule,
        MatTabsModule,
        MatFormFieldModule,
        MatDialogModule,
        MatSelectModule,
        MatInputModule,
        MatCardModule
    ],
    exports: [
        MatSidenavModule,
        MatIconModule,
        MatListModule,
        MatToolbarModule,
        MatMenuModule,
        MatButtonModule,
        MatTabsModule,
        MatFormFieldModule,
        MatDialogModule,
        MatSelectModule,
        MatInputModule,
        MatCardModule
    ]
})
export class MaterialModule {
}