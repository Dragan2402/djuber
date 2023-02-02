import {Component, OnInit} from '@angular/core';
import {ClientService} from '../../client.service';
import {Ride} from "../../client";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";

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
  startDate: Date
  endDate: Date
  constructor(private clientService : ClientService) { }
  ngOnInit(): void {

    this.clientService.getRides().subscribe({next: (pageResponse: Ride[]) => {
        console.log(pageResponse)
        const rides = pageResponse.filter(ride => this.isValidDate(new Date(ride.start)))
        console.log(rides)
        const dateList: string[] = [...new Set(rides.map(ride => new Date(ride.start).toLocaleDateString()))]
        const countMap = new Map()
        for (const date of dateList) countMap.set(date, 0)
        for (const ride of rides) {
          const dateString = new Date(ride.start).toLocaleDateString()
          if (dateList.includes(dateString)) countMap.set(dateString, countMap.get(dateString) + 1)
        }
        const data: {name: string, series: {value: number, name: string}[]}[] = [{name: "Rides", series: []}]
        countMap.forEach((value, key) => data[0].series.push({value, name: key}))
        this.data = data

        let priceSum = 0
        for (const ride of rides) {
          priceSum += ride.price
        }
        this.priceSum = priceSum
        this.average = priceSum / rides.length

      },
      error: (e) => console.error(e)})

  }

  isValidDate(value: Date) {
    console.log(this.startDate.toLocaleDateString() ,value.toLocaleDateString(),this.endDate.toLocaleDateString())
    return (this.startDate?.getTime() < value.getTime() && value.getTime() < this.endDate?.getTime())
  }

  handleDateSelected(value: {from: NgbDate, to: NgbDate}) {
    const from = value.from
    const to = value.to
    if (!from || !to) return
    this.startDate = new Date(from.year, from.month-1, from.day)
    this.endDate = new Date(to.year, to.month-1, to.day)
    this.ngOnInit()
  }
}
