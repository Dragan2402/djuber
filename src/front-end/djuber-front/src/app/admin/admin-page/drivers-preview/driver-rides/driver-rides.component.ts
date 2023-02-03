import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {ModalComponent} from "../../../../components/modal/modal.component";
import {Router} from "@angular/router";
import {Ride} from "../../../../client/client";
import {DriverService} from "../../../../driver/driver.service";

@Component({
  selector: 'djuber-driver-rides-admin',
  templateUrl: './driver-rides.component.html',
  styleUrls: ['./driver-rides.component.css']
})
export class DriverRidesComponent implements OnInit {
  displayedColumns: string[] = ['id', 'startName', 'endName', 'price', 'start', 'end'];
  length = 10;
  pageSize = 10;
  pageIndex = 0;
  pageSizes: number[] = [5,10,20];
  rides: Ride[];
  @Input('driverId') driverId: number;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('modal') private modalComponent: ModalComponent
  constructor(private driverService : DriverService, private router: Router) { }

  handlePageEvent(e: PageEvent) {
    this.driverService.getRidesPage(e.pageIndex, e.pageSize, this.driverId).subscribe({
      next: (pageResponse) => {
        this.rides = pageResponse['content'];
        this.pageSize = pageResponse["size"];
        if(pageResponse['totalElements']%this.pageSize !== 0){
          this.length = Math.floor(pageResponse['totalElements']/this.pageSize)+1;
        }else{
          this.length = pageResponse['totalElements']/this.pageSize;
        }
        this.pageIndex = pageResponse["pageable"]["pageNumber"];

      },
      error: (e) => console.error(e)})
  }

  handleClickRow(id: string) {
    this.router.navigate(['singleRideMap', id]);
  }
  ngOnInit(): void {
    this.driverService.getRidesPage(0, 10, this.driverId).subscribe({
      next: (pageResponse) => {
        this.rides = pageResponse['content'];
        this.pageSize = pageResponse["size"];
        if (pageResponse['totalElements'] % this.pageSize !== 0) {
          this.length = Math.floor(pageResponse['totalElements'] / this.pageSize) + 1;
        } else {
          this.length = pageResponse['totalElements'] / this.pageSize;
        }
        this.pageIndex = pageResponse["pageable"]["pageNumber"];
      },
      error: (e) => console.error(e)
    })
    }

}
