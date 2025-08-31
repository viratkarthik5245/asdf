import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h3>Accounts</h3>
      <div class="row">
        <div *ngFor="let a of accounts" class="card" style="min-width:280px">
          <div><b>{{a.accountNumber}}</b></div>
          <div>{{a.accountType}}</div>
          <div>Balance: {{a.balance | number:'1.2-2'}}</div>
        </div>
      </div>
    </div>

    <div class="card" style="max-width:520px">
      <h3>Add Account</h3>
      <label>Account Number</label><input [(ngModel)]="accountNumber">
      <label>Type</label>
      <select [(ngModel)]="accountType">
        <option value="SAVINGS">SAVINGS</option>
        <option value="CURRENT">CURRENT</option>
      </select>
      <label>Initial Balance</label><input type="number" [(ngModel)]="initialBalance">
      <div style="margin-top:10px"><button class="btn" (click)="create()">Create</button></div>
      <p *ngIf="msg">{{msg}}</p>
    </div>
  `
})
export class AccountsComponent implements OnInit {
  accounts:any[]=[]; accountNumber=''; accountType='SAVINGS'; initialBalance=0; msg='';
  constructor(private http: HttpClient) {}
  ngOnInit(){ this.load(); }
  load(){ this.http.get<any[]>('/api/accounts').subscribe(a => this.accounts = a); }
  create(){
    this.http.post('/api/accounts',{accountNumber:this.accountNumber,accountType:this.accountType,initialBalance:Number(this.initialBalance)}).subscribe({
      next: _=>{ this.msg='Created'; this.accountNumber=''; this.initialBalance=0; this.load(); },
      error: e=> this.msg = e?.error?.error || 'Error'
    });
  }
}
