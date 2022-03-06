import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-access-denied-dialog',
  templateUrl: './access-denied-dialog.component.html',
  styleUrls: ['./access-denied-dialog.component.scss']
})
export class AccessDeniedDialogComponent implements OnInit {

  constructor(private dialog: MatDialog) { }

  ngOnInit(): void {
  }

  

}
