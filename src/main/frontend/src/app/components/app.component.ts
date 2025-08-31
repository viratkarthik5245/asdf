import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <div class="card" style="border-radius:0">
      <div class="row" style="align-items:center;justify-content:space-between">
        <div><b>Bank of APIs</b></div>
        <nav class="row">
          <a routerLink="/">Dashboard</a>
          <a routerLink="/accounts">Accounts</a>
          <a routerLink="/transfer">Transfer</a>
          <a routerLink="/cards">Cards</a>
          <a routerLink="/profile">Profile</a>
          <a routerLink="/login">Login</a>
        </nav>
      </div>
    </div>
    <div style="padding:16px"><router-outlet /></div>
  `
})
export class AppComponent {}
