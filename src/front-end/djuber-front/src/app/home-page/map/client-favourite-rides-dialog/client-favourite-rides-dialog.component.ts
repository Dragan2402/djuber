import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LatLngBoundsExpression, LatLngExpression } from 'leaflet';
import { FavouriteRoute } from 'src/app/ride/favouriteRoute';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
import { ClientService } from '../../../client/client.service';
import { MapService } from '../map.service';
import { Route } from '../route';

@Component({
  selector: 'djuber-client-favourite-rides-dialog',
  templateUrl: './client-favourite-rides-dialog.component.html',
  styleUrls: ['./client-favourite-rides-dialog.component.css']
})
export class ClientFavouriteRidesDialogComponent implements OnInit {

  displayedColumns: string[] = [ 'route', 'distance','select', 'delete'];

  routes:FavouriteRoute[];

  constructor(public dialogRef: MatDialogRef<ClientFavouriteRidesDialogComponent>, private mapService:MapService, private snackBar:MatSnackBar) {

   }

  ngOnInit(): void {
    this.loadFavouriteRoutes();
  }

  loadFavouriteRoutes(){
    this.mapService.getClientFavouriteRoutes().subscribe({
      next:(v)=>{
        this.routes = v;
      }
    });
  }

  deleteRoute(route:FavouriteRoute){
    this.mapService.deleteFavouriteRoute(route.id).subscribe({complete:()=>{
      this.snackBar.openFromComponent(SnackbarComponent,{data:"Favourite route deleted."});
      this.loadFavouriteRoutes();
    },error:(err)=>{
      this.snackBar.openFromComponent(SnackbarComponent,{data:"Can't delete route right now."});
    }})
  }

  selectRoute(favRoute:FavouriteRoute){
    let route = new Route();
    route.setDistance(favRoute.distance);
    route.setId(0);
    route.setPoints(favRoute.coordinates.map(point =>{return {lat:point.lat, lng:point.lon} as LatLngExpression}));
    route.setStopNames(favRoute.stopNames);
    this.dialogRef.close(route);
  }
  // constructor(id:number, feature:any){
  //   this.id = id;
  //   this.distance = feature["properties"]["summary"]["distance"];
  //   this.points = [];
  //   const coordinates = feature["geometry"]["coordinates"];
  //   coordinates.forEach(coordinate => {
  //     this.points.push({lat:coordinate[1], lng:coordinate[0]});
  //   })
}
