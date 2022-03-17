import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { League } from 'src/app/model/league';
import { LeagueService } from 'src/app/service/league.service';

@Component({
  selector: 'app-league-dialog',
  templateUrl: './league-dialog.component.html',
  styleUrls: ['./league-dialog.component.scss']
})
export class LeagueDialogComponent implements OnInit {

  //public leagueName: String = "";

  constructor(@Inject(MAT_DIALOG_DATA) private leagues: League[],
  private dialogRef: MatDialogRef<LeagueDialogComponent>,
  private snackBar: MatSnackBar,
  private leagueService: LeagueService,
  private router: Router) { }

  ngOnInit(): void {
  }

  joinLeague(name: string) {
    if(name) {
      this.leagueService.joinLeague(name).subscribe({next: (data) => {
        if(data) {
          this.dialogRef.close(data)
        } else {
          this.snackBar.open('could not join league', '', {duration: 2500, verticalPosition: 'bottom'})
        }
      }});
      
    }
  }

  createLeague(name: String) {
    if(name) {
      console.log(`creating: ${name}`)
    }
  }

}
