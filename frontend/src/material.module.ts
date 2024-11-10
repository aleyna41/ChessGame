import { NgModule } from "@angular/core";
import { MatInputModule } from '@angular/material/input'
import { MatSelectModule } from '@angular/material/select'
import { MatCardModule } from '@angular/material/card'
import { MatCheckboxModule } from '@angular/material/checkbox'
import { MatRadioModule } from '@angular/material/radio'
import { MatTableModule} from '@angular/material/table'
import { MatSortModule } from '@angular/material/sort'
import { MatPaginatorModule } from '@angular/material/paginator'
import { MatDialogModule } from '@angular/material/dialog'
import { MatButtonModule } from "@angular/material/button"
import { MatDividerModule} from "@angular/material/divider"
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';


@NgModule({
    exports:[
        MatInputModule,
        MatSelectModule,
        MatCardModule,
        MatCheckboxModule,
        MatRadioModule,
        MatTableModule,
        MatSortModule,
        MatPaginatorModule,
        MatDialogModule,
        MatButtonModule,
        MatDividerModule,
    ]


})
export class MaterialModule {  }
