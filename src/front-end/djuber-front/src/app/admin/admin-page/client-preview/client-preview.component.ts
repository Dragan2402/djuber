import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Client } from 'src/app/client/client';
import { AdminService } from '../../admin.service';

@Component({
  selector: 'djuber-client-preview',
  templateUrl: './client-preview.component.html',
  styleUrls: ['./client-preview.component.css']
})
export class ClientPreviewComponent implements OnInit {
  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'email', 'city', 'block'];

  length = 10;
  pageSize = 10;
  pageIndex = 0;

  pageSizes:number[] = [5,10,20];

  clients:Client[];

  filterValue:string='';

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private adminService : AdminService) { }

  ngAfterViewInit(): void {

    this.adminService.getClientsPage(0,10 ,'').subscribe({
      next: (pageResponse) => {
        this.clients = pageResponse['content'];
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

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    const filterValueLower = filterValue.trim().toLowerCase();
    this.filterValue = filterValueLower;
    this.adminService.filterSearchClients(this.paginator.pageSize,filterValueLower).subscribe({
      next: (pageResponse) =>{
        this.clients = pageResponse['content'];
        this.pageSize = pageResponse["size"];
        if(pageResponse['totalElements']%this.pageSize !== 0){
          this.length = Math.floor(pageResponse['totalElements']/this.pageSize)+1;
        }else{
          this.length = pageResponse['totalElements']/this.pageSize;
        }
        this.pageIndex = pageResponse["pageable"]["pageNumber"];
      },
      error:(e)=> {console.log(e)}
    });
  }

  block(id:number){
    this.adminService.blockClient(id).subscribe({
      error: (e) => console.log(e),
      complete: () =>{
        let client = this.clients.find(client => client.id===id);
        client.isBlocked = true;
        this.clients.map(obj => obj.id === id ? client : obj);
      }
    });
  }

  unblock(id:number){
    this.adminService.unblockClient(id).subscribe({
      error: (e) => console.log(e),
      complete: () =>{
        let client = this.clients.find(client => client.id===id);
        client.isBlocked = false;
        this.clients.map(obj => obj.id === id ? client : obj);
      }
    });
  }

  handlePageEvent(e: PageEvent) {

    this.adminService.getClientsPage(e.pageIndex, e.pageSize, this.filterValue).subscribe({
      next: (pageResponse) => {
        this.clients = pageResponse['content'];
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

  ngOnInit(): void {
  }

}
