import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="row">
      <div class="card" style="flex:1;min-width:280px">
        <h3>Overview</h3>
        <div>Total Balance: <b>{{total | number:'1.2-2'}}</b></div>
      </div>
      <div class="card" style="flex:1;min-width:280px">
        <h3>Quick Actions</h3>
        <div>Go to Accounts / Transfer / Cards</div>
      </div>
    </div>
  `
})
export class DashboardComponent implements OnInit {
  total = 0;
  constructor(private http: HttpClient) {}
  ngOnInit(){
    this.http.get<any[]>('/api/accounts').subscribe(list => this.total = list.reduce((s,a)=>s + (a.balance||0), 0));
  }
}
