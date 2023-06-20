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
import {MatDividerModule} from '@angular/material/divider';
import {MatTableModule} from '@angular/material/table';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatSort, MatSortModule} from '@angular/material/sort';
import {MatExpansionModule} from '@angular/material/expansion';
import { MatDatepickerModule } from '@angular/material/datepicker';




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
        MatCardModule,
        MatDividerModule,
        MatTableModule,
        MatPaginatorModule,
        MatProgressBarModule,
        MatSortModule,
        MatExpansionModule,
        MatDatepickerModule,
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
        MatCardModule,
        MatDividerModule,
        MatTableModule,
        MatPaginatorModule,
        MatProgressBarModule,
        MatSortModule,
        MatExpansionModule,
        MatDatepickerModule,
        
    ]
   
})
export class MaterialModule {
}