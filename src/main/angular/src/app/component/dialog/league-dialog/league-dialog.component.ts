import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { League } from 'src/app/model/league';

@Component({
  selector: 'app-league-dialog',
  templateUrl: './league-dialog.component.html',
  styleUrls: ['./league-dialog.component.scss']
})
export class LeagueDialogComponent implements OnInit {

  //public leagueName: String = "";

  constructor(@Inject(MAT_DIALOG_DATA) private leagues: League[]) { }

  ngOnInit(): void {
  }

  joinLeague(name: String) {
    if(name) {
      console.log(`joining: ${name}`)
    }
  }

  createLeague(name: String) {
    if(name) {
      console.log(`creating: ${name}`)
    }
  }

}
