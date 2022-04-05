import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserData } from 'src/app/model/user-data';
import { UserService } from 'src/app/service/user.service';
import { SwPush } from '@angular/service-worker';
import { PushSubscriber } from 'src/app/model/push-subscriber';
import { NotificationService } from 'src/app/service/notification.service';
import { dashCaseToCamelCase } from '@angular/compiler/src/util';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  public username: string = "";
  public password: string = "";
  private PUBLIC_VAPID_KEY_OF_SERVER = 'BJG_5EXzoPd4VeO3jp12wPKNnvCmmnz9msNUCjzT9yAQ3e8yxmfPcK5J0jHPNZ0bx94RbsxRV-4xHtRsdiP_aW0'; 

  constructor(
    private userService: UserService,
    private notificationService: NotificationService,
    private router: Router, 
    readonly swPush: SwPush) { }

  ngOnInit(): void {
    localStorage.removeItem('currentUser');
  }

  onSubmit() {
    this.userService.login(this.username, this.password).subscribe({
      next: (userData: UserData) => {
        localStorage.setItem('currentUser', JSON.stringify({ username: this.username.toLowerCase(), userData: userData}));
        if (userData.roles.find(role => role == "FORMULA_ADMIN")) {
          this.subscribeToPush();
        }
        this.router.navigateByUrl('/')
      },
      error: (error: HttpErrorResponse) => {
        if (error.status == 401) {
          alert('Wrong user or password.');
        } else {
          throw error;
        }
      }
    })    
  }

  private async subscribeToPush() {
    try {
      const sub = await this.swPush.requestSubscription({
        serverPublicKey: this.PUBLIC_VAPID_KEY_OF_SERVER,
      });
      var key = sub.getKey('p256dh');
      var auth = sub.getKey('auth');
      var pushSubscriber = new PushSubscriber(
          sub.endpoint, 
          this.toBase64(key!!), 
          this.toBase64(auth!!),
          this.username.toLowerCase()
        )  

      this.notificationService.savePushSubscriber(pushSubscriber).subscribe(result => {
        //this.showNotificationOnPush();
      });
      
      
    } catch (err) {
    }
  }

  private toBase64(value: ArrayBufferLike) {
    return btoa(String.fromCharCode.apply(null, Array.from(new Uint8Array(value))))
  }

  private showNotificationOnPush() {
    self.addEventListener('push', function(event: any) {
      const payload = event.data ? event.data.text() : 'no payload';
      console.log('push message received:')
      console.log(event)
      console.log(event.data.title)
      console.log(payload)
      new Notification(event.data.title, {body: payload})
    });
  }

}
