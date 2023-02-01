import {Component, OnInit, ViewChild} from '@angular/core';
import {ClientService} from '../../client.service';
import {Ride} from "../../client";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'djuber-client-rides',
  templateUrl: './client-rides.component.html',
  styleUrls: ['./client-rides.component.css']
})
export class ClientRidesComponent implements OnInit {

  displayedColumns: string[] = ['id', 'startCoordinateName', 'endCoordinateName', 'price', 'start', 'finish'];
  length = 10;
  pageSize = 10;
  pageIndex = 0;
  pageSizes: number[] = [5,10,20];
  rides: Ride[];
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  constructor(private clientService : ClientService) { }
  ngAfterViewInit(): void {
    this.clientService.getRidesPage(0,10).subscribe({
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

  handlePageEvent(e: PageEvent) {
    this.clientService.getRidesPage(e.pageIndex, e.pageSize).subscribe({
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
    console.log(id)
  }

  ngOnInit(): void {
  }

}
