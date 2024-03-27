import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCommonModule } from '@angular/material/core';
import {MatButtonModule} from '@angular/material/button';
import { HomeComponent } from './layout/home/home.component';
import { ServicesComponent } from './layout/services/services.component';
import { FooterComponent } from './layout/footer/footer.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatToolbarModule, MatCommonModule, MatButtonModule, HomeComponent, ServicesComponent,
  FooterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'pki-system';
}
