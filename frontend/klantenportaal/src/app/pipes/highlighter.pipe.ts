import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Pipe({
  name: 'highlighter',
  standalone: true
})
export class HighlighterPipe implements PipeTransform {
  constructor(private sanitizer: DomSanitizer) {}

  transform(value: any, args: any): unknown {
    if (typeof value === 'number') {
      value = value.toString();
    }
    if (args !== "") {
      const highlightedValue = this.highlightText(value, args);
      return this.sanitizer.bypassSecurityTrustHtml(highlightedValue);
    }
    return value;
  }

  private highlightText(value: string, searchText: string): string {
    let includesBreak = false;
    if (value.includes("<br>")) {
      includesBreak = true;
    }
    const segments = value.split("<br>");

    for (let i = 0; i < segments.length; i++) {
        const re = new RegExp("(" + searchText + ")", 'igm');
        segments[i] = segments[i].replace(re, '<span style="background-color: yellow !important;">$1</span>');
    }
    return segments.join("<br>");
  }
}