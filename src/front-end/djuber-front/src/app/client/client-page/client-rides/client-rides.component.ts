import {Component, OnInit, ViewChild} from '@angular/core';
import {ClientService} from '../../client.service';
import {Client, ClientRide, Ride} from "../../client";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {ModalComponent} from "../../../components/modal/modal.component";
import {ModalConfig} from "../../../components/modal/modal.config";
import {Router} from "@angular/router";

@Component({
  selector: 'djuber-client-rides',
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
  ride: ClientRide;
  clients: Client[];
  modalConfig: ModalConfig;
  loggedClientId: number;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('modal') private modalComponent: ModalComponent
  constructor(private clientService : ClientService, private router: Router) { }

  handlePageEvent(e: PageEvent) {
    this.clientService.getRidesPage(e.pageIndex, e.pageSize, this.loggedClientId).subscribe({
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
    this.clientService.getLoggedClient().subscribe({ next: (response: Client) => {
        this.loggedClientId = response.identityId
        this.clientService.getRidesPage(0, 10, response.identityId).subscribe({
          next: (pageResponse) => {
            console.log(pageResponse)
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
