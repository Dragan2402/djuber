import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { AdminService } from '../../admin.service';
import {Driver} from '../../../driver/driver';
import { MatTableDataSource } from '@angular/material/table';


@Component({
  selector: 'djuber-drivers-preview',
  templateUrl: './drivers-preview.component.html',
  styleUrls: ['./drivers-preview.component.css']
})
export class DriversPreviewComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'email', 'city', 'carType' , 'licensePlate','block'];

  length = 10;
  pageSize = 10;
  pageIndex = 0;

  pageSizes:number[] = [5,10,20];

  drivers:Driver[];

  filterValue:string='';

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private adminService : AdminService) { }

  ngAfterViewInit(): void {

    this.adminService.getDriversPage(0,10 ,'').subscribe({
      next: (pageResponse) => {
        this.drivers = pageResponse['content'];
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
    this.adminService.filterSearch(this.paginator.pageSize,filterValueLower).subscribe({
      next: (pageResponse) =>{
        this.drivers = pageResponse['content'];
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
    this.adminService.blockDriver(id).subscribe({
      error: (e) => console.log(e),
      complete: () =>{
        let driver = this.drivers.find(driver => driver.id===id);
        driver.isBlocked = true;
        this.drivers.map(obj => obj.id === id ? driver : obj);
      }
    });
  }

  unblock(id:number){
    this.adminService.unblockDriver(id).subscribe({
      error: (e) => console.log(e),
      complete: () =>{
        let driver = this.drivers.find(driver => driver.id===id);
        driver.isBlocked = false;
        this.drivers.map(obj => obj.id === id ? driver : obj);
      }
    });
  }

  handlePageEvent(e: PageEvent) {

    this.adminService.getDriversPage(e.pageIndex, e.pageSize, this.filterValue).subscribe({
      next: (pageResponse) => {
        this.drivers = pageResponse['content'];
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
