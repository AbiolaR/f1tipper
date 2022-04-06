import { Component, OnInit } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';
import { UserData } from './model/user-data';
import { AppService } from './service/app.service';
import { UserService } from './service/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  public isLoading: boolean = false;

  

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private appService: AppService) {

    this.matIconRegistry.addSvgIcon(
      "race",
      this.domSanitizer.bypassSecurityTrustResourceUrl("/assets/svg-icons/racing-flag.svg")
    );
    this.matIconRegistry.addSvgIcon(
      "helmet",
      this.domSanitizer.bypassSecurityTrustResourceUrl("/assets/svg-icons/helmet.svg")
    );
    this.matIconRegistry.addSvgIcon(
      "user",
      this.domSanitizer.bypassSecurityTrustResourceUrl("/assets/svg-icons/user.svg")
    );
  }



  ngOnInit(): void {
    this.appService.setLanguage();
  }
}
