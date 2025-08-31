import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  standalone: true,
  imports: [FormsModule, CommonModule],
  template: `
    <div class="card" style="max-width:520px">
      <h3>Transfer Funds</h3>
      <label>From Account</label><input [(ngModel)]="fromAccountNumber">
      <label>To Account</label><input [(ngModel)]="toAccountNumber">
      <label>Amount</label><input type="number" [(ngModel)]="amount">
      <label>Remarks</label><input [(ngModel)]="remarks">
      <div style="margin-top:10px"><button class="btn" (click)="transfer()">Transfer</button></div>
      <p *ngIf="msg">{{msg}}</p>
    </div>

    <div class="card">
      <h3>My Transactions</h3>
      <div *ngFor="let t of txns" class="card" style="min-width:260px">
        <div><b>{{t.type}}</b> — {{t.amount | number:'1.2-2'}}</div>
        <div>{{t.description}}</div>
        <small>{{t.createdAt}}</small>
      </div>
    </div>
  `
})
export class TransferComponent {
  fromAccountNumber='100001234567'; toAccountNumber='200001234567'; amount=10; remarks='';
  msg=''; txns:any[]=[];
  constructor(private http: HttpClient){ this.refresh(); }
  transfer(){
    this.http.post<any>('/api/transfers',{fromAccountNumber:this.fromAccountNumber,toAccountNumber:this.toAccountNumber,amount:Number(this.amount),remarks:this.remarks}).subscribe({
      next: (r)=>{ this.msg = 'Success Ref: '+r.referenceId; this.refresh(); },
      error: e=> this.msg = e?.error?.error || 'Error'
    });
  }
  refresh(){ this.http.get<any[]>('/api/transactions').subscribe(t=> this.txns = t.reverse()); }
}
