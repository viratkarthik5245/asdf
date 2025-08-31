import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [FormsModule],
  template: `
    <div class="card" style="max-width:420px;margin:40px auto;">
      <h2>Login</h2>
      <form (ngSubmit)="submit()">
        <label>Email</label>
        <input [(ngModel)]="email" name="email" type="email" required>
        <label>Password</label>
        <input [(ngModel)]="password" name="password" type="password" required>
        <div style="margin-top:12px" class="row">
          <button class="btn" type="submit">Sign in</button>
          <button class="btn" type="button" (click)="register()">Register</button>
        </div>
        <p *ngIf="error" style="color:#ff6b6b">{{error}}</p>
      </form>
    </div>
  `
})
export class LoginComponent {
  email = 'user@bank.local'; password = 'User@123'; error = '';
  constructor(private http: HttpClient, private router: Router) {}
  submit(){
    this.error='';
    this.http.post<{token:string}>('/api/auth/login',{email:this.email,password:this.password}).subscribe({
      next: (r)=>{ localStorage.setItem('jwt', r.token); this.router.navigateByUrl('/'); },
      error: (e)=> this.error = e?.error?.message || 'Login failed'
    });
  }
  register(){
    this.http.post('/api/auth/register',{email:this.email,password:this.password,name:'User'}).subscribe({
      next: ()=> alert('Registered! Now login.'),
      error: (e)=> this.error = e?.error?.message || 'Registration failed'
    });
  }
}
