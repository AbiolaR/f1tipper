import { Component, OnInit } from '@angular/core';
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from "@angular/platform-browser";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent {

  constructor(private matIconRegistry: MatIconRegistry,
              private domSanitizer: DomSanitizer
              ) { 
                this.matIconRegistry.addSvgIcon(
                  "race",
                  this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/svg-icons/racing-flag.svg")
                );
                this.matIconRegistry.addSvgIcon(
                  "helmet",
                  this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/svg-icons/helmet.svg")
                );
              }
}
