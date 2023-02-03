import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {ModalComponent} from "../../../../components/modal/modal.component";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Ride} from "../../../../client/client";
import {ClientService} from "../../../../client/client.service";

@Component({
  selector: 'djuber-client-rides-admin',
  templateUrl: './client-rides.component.html',
  styleUrls: ['./client-rides.component.css']
})

export class ClientRidesComponent implements OnInit {
  displayedColumns: string[] = ['id', 'startName', 'endName', 'price', 'start', 'end'];
  length = 10;
  pageSize = 10;
  pageIndex = 0;
  pageSizes: number[] = [5,10,20];
  rides: Ride[];
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('modal') private modalComponent: ModalComponent
  @Input('clientId') clientId: number;

  constructor(private clientService : ClientService, private router: Router, private _snackBar: MatSnackBar) { }

  handlePageEvent(e: PageEvent) {
    this.clientService.getRidesPage(e.pageIndex, e.pageSize, this.clientId).subscribe({
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
    this.clientService.getRidesPage(0, 10, this.clientId).subscribe({
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
