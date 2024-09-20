import { Component, ViewChild, ElementRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {
  imoInput = new FormControl(''); 

  @ViewChild('vesselMapIframe', { static: true }) iframe!: ElementRef<HTMLIFrameElement>;

  constructor() {
    this.imoInput.valueChanges.subscribe(imo => {
      this.updateIframeSrc(imo ?? ''); 
    });
  }

  ngOnInit(): void {
    this.updateIframeSrc('');
  }

  updateIframeSrc(imo: string): void {
    const src = imo ? `https://www.vesselfinder.com/aismap?imo=${imo}&width=800&height=500&names=true` :
                      'https://www.vesselfinder.com/aismap?width=1200&height=500&names=true';
    this.iframe.nativeElement.src = src;
  }
}
