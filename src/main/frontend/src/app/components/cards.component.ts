import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h3>Credit Cards</h3>
      <div class="row">
        <div *ngFor="let c of cards" class="card" style="min-width:280px">
          <div><b>{{c.cardNumberMasked}}</b></div>
          <div>Limit: {{c.limitAmount | number:'1.0-0'}}</div>
          <div>Outstanding: {{c.outstanding | number:'1.2-2'}}</div>
          <label>Pay Amount</label><input type="number" [(ngModel)]="payAmt[c.id]">
          <button class="btn" (click)="pay(c.id)">Pay</button>
        </div>
      </div>
    </div>

    <div class="card" style="max-width:520px">
      <h3>Add Card</h3>
      <label>Masked Number</label><input [(ngModel)]="masked">
      <label>Expiry (MM/YY)</label><input [(ngModel)]="expiry">
      <label>Limit</label><input type="number" [(ngModel)]="limitAmount">
      <div style="margin-top:10px"><button class="btn" (click)="add()">Add</button></div>
      <p *ngIf="msg">{{msg}}</p>
    </div>
  `
})
export class CardsComponent implements OnInit {
  cards:any[]=[]; masked='**** **** **** 4242'; expiry='12/29'; limitAmount=50000; msg=''; payAmt:any={};
  constructor(private http: HttpClient){}
  ngOnInit(){ this.load(); }
  load(){ this.http.get<any[]>('/api/cards').subscribe(c=> this.cards=c); }
  add(){ this.http.post('/api/cards',{cardNumberMasked:this.masked,expiry:this.expiry,limitAmount:Number(this.limitAmount)}).subscribe({next:_=>{this.msg='Added';this.load();}, error:e=>this.msg='Error'}); }
  pay(id:number){ const a=Number(this.payAmt[id]||0); if(a<=0){this.msg='Invalid amount'; return;} this.http.post(`/api/cards/${id}/pay`,{amount:a}).subscribe({next:_=>{this.msg='Paid';this.load();}, error:e=>this.msg='Error'}); }
}
