import { Injectable, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { UserData } from '../model/user-data';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  private userData = new UserData;

  constructor(private userService: UserService, private translate: TranslateService) { 
    translate.setDefaultLang('en');
  }

  public setLanguage(language: string = '') {
    this.userData = this.userService.getUserData()
    if (language == '') {
      language = this.userData.selectedLanguage
    }
    if(this.translate.currentLang == language) {
      return
    }
    switch(language || navigator.language) {
      case 'de': {
        this.translate.use('de');
        this.userData.selectedLanguage = 'de';
        break;
      }
      case 'de-DE': {
        this.translate.use('de');
        this.userData.selectedLanguage = 'de';
        break;
      }
      default: {
        this.translate.use('en');
        this.userData.selectedLanguage = 'en';
      }
    }
    this.userService.setUserData(this.userData);
  }
}
