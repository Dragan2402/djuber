import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {ModalComponent} from "../../../../components/modal/modal.component";
import {Router} from "@angular/router";
import {Ride} from "../../../../client/client";
import {DriverService} from "../../driver.service";
import {Driver} from "../../driver";

@Component({
  selector: 'djuber-driver-rides',
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
  loggedDriverId: number;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('modal') private modalComponent: ModalComponent
  constructor(private driverService : DriverService, private router: Router) { }

  handlePageEvent(e: PageEvent) {
    this.driverService.getRidesPage(e.pageIndex, e.pageSize, this.loggedDriverId).subscribe({
      next: (pageResponse) => {
        console.log(pageResponse)
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
    this.driverService.getLoggedDriver().subscribe({ next: (response: Driver) => {
        this.loggedDriverId = response.identityId
        this.driverService.getRidesPage(0, 10, response.identityId).subscribe({
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
      }})
    }

}
