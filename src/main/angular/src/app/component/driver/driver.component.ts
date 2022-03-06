import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { DriverService } from '../../service/driver.service';
import { Driver, EmptyDriver } from '../../model/driver';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-driver',
  templateUrl: './driver.component.html',
  styleUrls: ['./driver.component.scss']
})
export class DriverComponent implements OnInit {

  drivers: Driver[] = [];

  constructor(private driverService: DriverService, public dialogRef: MatDialogRef<DriverComponent>,
              @Inject(MAT_DIALOG_DATA) public excludeDrivers: number[]){}

  ngOnInit(): void {
   this.getDrivers();   
  }

  getDrivers(): void {
    this.driverService.getDrivers().subscribe(
      (response: Driver[]) => {
        this.drivers = response;
        this.drivers = this.drivers.filter(driver => {
          return !this.excludeDrivers.includes(driver.id);
        })
        console.log(this.drivers)
      }
    );
  }

  returnDriver(driver: Driver) {
    this.dialogRef.close(driver)
  }

  removeDriver() {
    this.returnDriver(new EmptyDriver())
  }

}
