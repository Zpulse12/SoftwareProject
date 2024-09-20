import { Component, OnInit } from '@angular/core';
import { AdminService } from '../services/admin.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HighlighterPipe } from "../pipes/highlighter.pipe";

@Component({
    selector: 'app-admin',
    standalone: true,
    templateUrl: './admin.component.html',
    styleUrl: './admin.component.css',
    providers: [AdminService],
    imports: [FormsModule, CommonModule, HighlighterPipe]
})
export class AdminComponent implements OnInit {
  clients: any[] = [];
  searchText: string = '';
  filteredClients: any[] = [];

  constructor(private clientService: AdminService) {}

  ngOnInit() {
    this.clientService.getClients().subscribe(data => {
      this.clients = data.filter((client: { role: string; }) => client.role !== 'admin');
      this.filteredClients = this.clients;
    });
  }

  approve(id:number) {
    const client = this.clients.find(c => c.id === id);
    if (!client) {
      console.error('Client not found for id', id);
      return;
    }
    if (!client.companyCode) {
      alert('Company code is missing for client');
      return;
    }
    this.clientService.updateStatus(id, 'APPROVED').subscribe(() => {
      alert('Registration approved');
      console.log(`Client ${id} status set to APPROVED`);
      if (client) {
        client.status = 'APPROVED';
      }
    });
  }
  
  decline(id: number) {
    this.clientService.updateStatus(id, 'DECLINED').subscribe(() => {
      alert('Registration declined');
      console.log(`Client ${id} status set to DECLINED`);
      const client = this.clients.find(c => c.id === id);
      if (client) {
        client.status = 'DECLINED';
      }
    });
  }
  
  
 
deleteClient(id: number): void {
  if (confirm('Are you sure you want to delete this registration?')) {
    this.clientService.deleteClient(id).subscribe({
      next: () => {
        alert('Registration deleted');
        this.clients = this.clients.filter(client => client.id !== id);
      },
      error: (error) => {
        console.error('Error deleting client', error);
        alert('Failed to delete the client');
      }
    });
  }
}
updateClient(client: any): void {
  if (client.id && (client.vat || client.eori)) {
    this.clientService.updateClient(client).subscribe({
      next: () => {
        alert('Client updated successfully');
      },
      error: (error) => {
        console.error('Error updating client', error);
        alert('Failed to update the client');
      }
    });
  }
}

filterClients(): void {
  if (!this.searchText) {
    this.filteredClients = this.clients;
  } else {
    this.filteredClients = this.clients.filter(client => {
      const name = client.fullName?.toLowerCase() || '';
      const companyName = client.companyName?.toLowerCase() || '';
      const searchTextLower = this.searchText.toLowerCase();

      return name.includes(searchTextLower) || companyName.includes(searchTextLower);
    });
  }
}

  
}
