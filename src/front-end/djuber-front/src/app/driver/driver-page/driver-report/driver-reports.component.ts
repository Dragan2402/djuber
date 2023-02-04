import {Component, OnInit} from '@angular/core';
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";
import {Ride} from "../../../client/client";
import {DriverService} from "../../driver.service";
import {Driver} from "../../driver";

@Component({
  selector: 'djuber-driver-reports',
  templateUrl: './driver-reports.component.html',
  styleUrls: ['./driver-reports.component.css']
})
export class DriverReportsComponent implements OnInit {
  data: {name: string, series: {value: number, name: string}[]}[]
  priceSum: number
  average: number
  rides: Ride[]
  startDate: Date
  endDate: Date
  constructor(private driverService : DriverService) { }
  ngOnInit(): void {

    this.driverService.getLoggedDriver().subscribe({ next: (response) => {
        this.driverService.getRidesPage(0, 9999, response.identityId).subscribe({next: (pageResponse) => {
            const rides = pageResponse['content'].filter(ride => this.isValidDate(new Date(ride.start)))
            const dateList = [...new Set(rides.map(ride => new Date(ride.start).toLocaleDateString()))]
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
      }})

  }

  isValidDate(value: Date) {
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
