import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenData } from 'src/app/model/token-data';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  public username: string = "";
  public password: string = "";

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    localStorage.removeItem('currentUser');
  }

  onSubmit() {
    this.userService.login(this.username, this.password).subscribe({
      next: (tokenData: TokenData) => {
        localStorage.setItem('currentUser', JSON.stringify({ username: this.username.toLowerCase(), tokenData: tokenData}));
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

}
