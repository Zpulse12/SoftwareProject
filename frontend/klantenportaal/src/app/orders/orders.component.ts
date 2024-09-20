import { ChangeDetectorRef, Component, ElementRef, HostListener, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { IOrder } from '../interfaces/iorder';
import { OrdersService } from '../services/orders.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HighlighterPipe } from "../pipes/highlighter.pipe";
import { RouterModule, RouterOutlet } from '@angular/router';
import { LoginService } from '../services/login.service';

@Component({
    selector: 'app-orders',
    standalone: true,
    templateUrl: './orders.component.html',
    styleUrl: './orders.component.css',
    providers: [OrdersService],
    imports: [CommonModule, FormsModule, HighlighterPipe, RouterOutlet, RouterModule]
})
export class OrdersComponent implements OnInit {
  @ViewChild('content', { static: false }) contentElement!: ElementRef;
  @ViewChild('orderDetail', { static: false }) orderDetailElement!: ElementRef;
  @ViewChild('divider', {static: false}) dividerElement!: ElementRef
  resizing = false;

  startResize(event: MouseEvent) {
    event.preventDefault();
    this.resizing = true;
    document.addEventListener('mousemove', this.resizeContent);
    document.addEventListener('mouseup', this.stopResize); // Add mouseup listener to document
  }

  stopResize = (event: MouseEvent) => {
    event.preventDefault();
    this.resizing = false;
    document.removeEventListener('mousemove', this.resizeContent);
    document.removeEventListener('mouseup', this.stopResize); // Remove mouseup listener from document
  }

  stopResizeOnMouseLeave = (event: MouseEvent) => {
    if (this.resizing) {
      this.stopResize(event);
    }
  }

  resizeContent = (event: MouseEvent) => {
    if (this.resizing) {
      const { left } = this.contentElement.nativeElement.getBoundingClientRect();
      const { paddingLeft, paddingRight } = window.getComputedStyle(this.contentElement.nativeElement);
      const dividerWidth = this.dividerElement.nativeElement.offsetWidth;
      const contentWidth = event.clientX - left - parseFloat(paddingLeft || '0') - parseFloat(paddingRight || '0') - (dividerWidth / 2);
      this.contentElement.nativeElement.style.width = contentWidth + 'px';
      this.orderDetailElement.nativeElement.style.width = `calc(100% - ${contentWidth}px)`;
    }
  }

  allOrders: IOrder[] = [];
  ordersBetween: IOrder[] = [];
  currentPage: number = 1;
  ordersPerPage: number = 15;
  maxPage: number = 1;
  sortColumn: string = "";
  sortDirection: string = "asc"; 
  searchText: string = "";
  isAdmin: boolean = false
  orderFetchTime: number = Date.now();

  selectedOrder: any;

  selectOrder(order: any) {
    this.selectedOrder = order;
  }

  portOptions: string[] = [];
  stateOptions: string[] = [];
  transportTypeOptions: string[] = [];
  CargoTypeOptions: string[] = [];
  filterDeparturePort: string = '';
  filterArrivalPort: string = '';
  filterState: string = '';
  filterTransportType: string = '';
  filterCargoType: string = '';
  filterStartDate: string = '';
  filterEndDate: string = '';
  constructor(private ordersService: OrdersService, private loginService: LoginService) {
  }
  ngOnInit(): void {
    this.updateOrders(true);
    this.isAdmin = this.loginService.isAdmin()
  }
  updateOrdersFilterOptions(orders: IOrder[]): void {
      const departurePorts = new Set<string>();
      const arrivalPorts = new Set<string>();
      const states = new Set<string>();
      const transportTypes = new Set<string>();
      const CargoTypes = new Set<string>();

      orders.forEach(order => {
        if (order.portOfOriginName) departurePorts.add(order.portOfOriginName);
        if (order.portOfDestinationName) arrivalPorts.add(order.portOfDestinationName);
        if (order.state) states.add(order.state);
        if (order.transportType) transportTypes.add(order.transportType);
        if (order.cargoType) CargoTypes.add(order.cargoType);
      });

      this.portOptions = [...new Set<string>([...departurePorts, ...arrivalPorts])]; // Combine and deduplicate
      this.stateOptions = Array.from(states);
      this.transportTypeOptions = Array.from(transportTypes);
      this.CargoTypeOptions = Array.from(CargoTypes);
  }
  applyFilters(orders: IOrder[]): IOrder[] {
    let filtered = orders;
  
    if (this.filterDeparturePort) {
      filtered = filtered.filter(order => order.portOfOriginName === this.filterDeparturePort);
    }
    if (this.filterState) {
      filtered = filtered.filter(order => order.state === this.filterState);
    }
    if (this.filterTransportType) {
      filtered = filtered.filter(order => order.transportType === this.filterTransportType);
    }
    if (this.filterArrivalPort) {
      filtered = filtered.filter(order => order.portOfDestinationName === this.filterArrivalPort);
    }
    if (this.filterCargoType) {
      filtered = filtered.filter(order => order.cargoType === this.filterCargoType);
    }
    return filtered;
  }
  
  searchOrders(orders: IOrder[]): IOrder[] {
    const searchText = this.searchText.toLowerCase();
    if (searchText === "") {
      return orders;
    }
    return orders.filter(order => {
      return (
        order.referenceNumber.toLowerCase().includes(searchText) ||
        order.customerReferenceNumber.toLowerCase().includes(searchText) ||
        (order.customerCode && order.customerCode.toLowerCase().includes(searchText) && this.isAdmin) ||
        order.portOfOriginName.toLowerCase().includes(searchText) ||
        ((order.ets && order.ets.toLowerCase().includes(searchText)) && order.ats == null) ||
        ((order.eta && order.eta.toLowerCase().includes(searchText)) && order.ata == null) ||
        ((order.ats && order.ats.toLowerCase().includes(searchText)) && order.ats != null) ||
        ((order.ata && order.ata.toLowerCase().includes(searchText)) && order.ata != null) ||
        order.portOfDestinationName.toLowerCase().includes(searchText) ||
        order.state.toLowerCase().includes(searchText) ||
        order.transportType.toLowerCase().includes(searchText) ||
        (order.cargoType && order.cargoType.toString().includes(searchText))
      );
    });
  }

  delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
}

  async updateOrders(goToPage1: boolean) {
    if (goToPage1) {
      this.currentPage = 1;
    }
    const startIndex = (this.currentPage - 1) * this.ordersPerPage;
    const endIndex = startIndex + this.ordersPerPage;
    try {
      if (this.orderFetchTime < Date.now()) { // if it has been 30 seconds since last fetch
        this.allOrders = await this.ordersService.getAllOrders();
        this.orderFetchTime = Date.now() + 30000;
      }
      this.updateOrdersFilterOptions(this.allOrders)
      this.applySorting(); // Apply sorting after getting orders
      let filteredOrders = this.applyFilters(this.allOrders);
      let searchedOrders = this.searchOrders(filteredOrders)
      this.ordersBetween = searchedOrders.slice(startIndex, endIndex);
      this.maxPage = Math.ceil(searchedOrders.length / this.ordersPerPage);
    } catch (err) {
      console.log(err);
    }
  }

  applySorting() {
    if (this.sortColumn != "") {
      this.allOrders.sort((a, b) => {
        const valueA = (a as any)[this.sortColumn];
        const valueB = (b as any)[this.sortColumn];
        if (this.sortColumn == "ets" || this.sortColumn == "eta" || this.sortColumn == "ats" || this.sortColumn == "ata") { // if Date column, sort by date
          if (!valueA && !valueB) return 0;
          if (!valueA) return this.sortDirection === "asc" ? 1 : -1;
          if (!valueB) return this.sortDirection === "asc" ? -1 : 1;
          const dateA = this.parseDate(valueA);
          const dateB = this.parseDate(valueB);
          return this.sortDirection === "asc" ? dateA.getTime() - dateB.getTime() : dateB.getTime() - dateA.getTime();
        }
        if (valueA < valueB) {
          return this.sortDirection == "asc" ? -1 : 1;
        } else if (valueA > valueB) {
          return this.sortDirection == "asc" ? 1 : -1;
        }
        return 0;
      });
    }
  }

  updateSortColumn(column: string) {
    if (this.sortColumn == column) { // If you sort the sorted culumn
      if (this.sortDirection == "asc") {
        this.sortDirection = "desc"
      } else {
        this.sortDirection = "asc"
    }
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc'; // Reset to ascending if sorting a new column
    }
    this.updateOrders(true);
  }

  getSortImage(column: string): string {
    if (this.sortColumn === column) {
      return this.sortDirection === 'asc' ? 'assets/sort_asc.png' : 'assets/sort_desc.png';
    } else {
      return 'assets/swap.png';
    }
  }

  parseDate(dateStr: string): Date { // format "27-02-2024 12:52"
    const [day, month, yearTime] = dateStr.split("-");
    const [year, time] = yearTime.split(" ");
    const [hour, minute] = time.split(":");
    
    return new Date(parseInt(year), parseInt(month) - 1, parseInt(day), parseInt(hour), parseInt(minute));
  }

  nextPage() {
    if (this.currentPage < this.maxPage) {
      this.currentPage++;
      this.updateOrders(false);
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updateOrders(false);
    }
  }


  goToPage(newPageNumber: number) {
    if (newPageNumber > this.maxPage) {
      newPageNumber = this.maxPage;
    }
    if (newPageNumber >= 1) {
      this.currentPage = newPageNumber;
      this.updateOrders(false);
    }
  }

  countUniqueItems(itemList: string[]): string {
    const items: { [key: string]: number } = {};

    // Count the occurrences of each item
    itemList.forEach(item => {
        if (items[item]) {
            items[item]++;
        } else {
            items[item] = 1;
        }
    });

    // Construct the output string
    const output: string[] = [];
    let count = 0;
    let optionalBr = ""
    Object.keys(items).forEach(item => {
        count++;
        output.push(`${optionalBr}${items[item]}x ${item}`);
        if (count % 3 === 0) {
          optionalBr = "<br>"
        }else{
        optionalBr = ""
        }
    });

    return output.join(", ");
 }

 addBrAfter(text: string, before: string): string{
  return text.replace(new RegExp(before, 'g'), `${before}<br>`);
 }
}
