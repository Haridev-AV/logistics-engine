import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShipmentService } from './services/shipment.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="padding: 20px;">
      <h1>RouteOptima Dashboard</h1>
      <ul>
        <li *ngFor="let s of shipments">
          {{ s.origin }} to {{ s.destination }}: <strong>₹{{ s.basePrice }}</strong>
        </li>
      </ul>
    </div>
  `
})
export class AppComponent implements OnInit {
  shipments: any[] = [];

  constructor(private shipmentService: ShipmentService) {}

  ngOnInit() {
    this.shipmentService.getShipments().subscribe(data => {
      this.shipments = data;
    });
  }
}