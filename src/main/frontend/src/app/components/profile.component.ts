import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="card">
      <h3>Profile</h3>
      <pre>{{me | json}}</pre>
    </div>
  `
})
export class ProfileComponent {
  me:any = {};
  constructor(private http: HttpClient) { this.http.get('/api/users/me').subscribe(v => this.me = v); }
}
