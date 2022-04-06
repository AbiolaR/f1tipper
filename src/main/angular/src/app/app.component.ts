import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  public isLoading: boolean = false;

  constructor(private translate: TranslateService) {
    translate.setDefaultLang('en');
  }

  ngOnInit(): void {
    this.setLanguage();
  }

  private setLanguage() {
    switch(navigator.language) {
      case 'en': {
        this.translate.use('en');
        break;
      }
      case 'de': {
        this.translate.use('de');
        break;
      }
      case 'de-DE': {
        this.translate.use('de');
        break;
      }
    }
  }
}
