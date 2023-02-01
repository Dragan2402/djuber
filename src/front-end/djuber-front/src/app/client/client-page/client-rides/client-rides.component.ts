import {Component, OnInit, ViewChild} from '@angular/core';
import {ClientService} from '../../client.service';
import {Ride} from "../../client";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {ModalComponent} from "../../../components/modal/modal.component";
import {ModalConfig} from "../../../components/modal/modal.config";

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
  ride: any;
  modalConfig: ModalConfig;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('modal') private modalComponent: ModalComponent
  constructor(private clientService : ClientService) { }
  ngAfterViewInit(): void {
    this.clientService.getRidesPage(0, 10).subscribe({
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
  }

  openModal() {
    return this.modalComponent.open();
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
    this.clientService.getRide(id).subscribe({next: (pageResponse) => {
        this.ride = pageResponse
        this.modalConfig = {modalTitle: 'Ride details', hideCloseButton(): boolean { return true }, hideDismissButton(): boolean { return true}}
        this.openModal()
      }})

  }
  ngOnInit(): void {
  }

}
