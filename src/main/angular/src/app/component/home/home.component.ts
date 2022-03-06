import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  username!: String;
  constructor(private userService: UserService) { }

  ngOnInit(): void {
    const user: User = this.userService.getLocalUser();
    this.username = user.username;
    console.log(user);
  }

}
