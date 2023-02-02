import {Component, OnInit} from '@angular/core';
import {ClientService} from '../../client.service';
import {Ride} from "../../client";

@Component({
  selector: 'djuber-client-reports',
  templateUrl: './client-reports.component.html',
  styleUrls: ['./client-reports.component.css']
})
export class ClientReportsComponent implements OnInit {
  data: {name: string, series: {value: number, name: string}[]}[]
  priceSum: number
  average: number
  rides: Ride[]
  constructor(private clientService : ClientService) { }
  ngOnInit(): void {

    this.clientService.getRides().subscribe({next: (pageResponse: Ride[]) => {
        const dateList: string[] = [...new Set(pageResponse.map(ride => new Date(ride.start).toLocaleDateString()))]
        const countMap = new Map()
        for (const date of dateList) countMap.set(date, 0)
        for (const ride of pageResponse) {
          const dateString = new Date(ride.start).toLocaleDateString()
          if (dateList.includes(dateString)) countMap.set(dateString, countMap.get(dateString) + 1)
        }
        const data: {name: string, series: {value: number, name: string}[]}[] = [{name: "Rides", series: []}]
        countMap.forEach((value, key) => data[0].series.push({value, name: key}))
        this.data = data

        let priceSum = 0
        for (const ride of pageResponse) {
          priceSum += ride.price
        }
        this.priceSum = priceSum
        this.average = priceSum / pageResponse.length

      },
      error: (e) => console.error(e)})

  }

}
