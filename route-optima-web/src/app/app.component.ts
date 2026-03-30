import { Component, OnInit, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import * as L from 'leaflet';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, AfterViewInit {
  shipments: any[] = [];
  newShipment = { origin: '', destination: '', basePrice: 0 };
  private map!: L.Map;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadShipments();
  }

  ngAfterViewInit() {
    this.initMap();
  }

  private initMap(): void {
    // Center map on a general view of Europe/Asia
    this.map = L.map('map', { center: [20, 0], zoom: 3 });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      attribution: '© OpenStreetMap'
    }).addTo(this.map);
  }

  loadShipments() {
    this.http.get<any[]>('http://localhost:8080/api/shipments')
      .subscribe(data => this.shipments = data);
  }

  submitShipment() {
    this.http.post('http://localhost:8080/api/shipments', this.newShipment)
      .subscribe(() => {
        this.loadShipments();
        this.newShipment = { origin: '', destination: '', basePrice: 0 };
      });
  }
}