export interface IOrderDetail {
    referenceNumber: string
    customerReferenceNumber: string
    state: string
    transportType: string
    portOfOriginCode: string
    portOfOriginName: string
    portOfDestinationCode: string
    portOfDestinationName: string
    portCode: string
    shipName: string | null
    shipIMO: string | null
    shipMMSI: string | null
    shipType: string | null
    ets: string | null
    ats: string | null
    eta: string | null
    ata: string | null
    preCarriage: string
    estimatedTimeCargoOnQuay: string | null
    actualTimeCargoLoaded: string | null
    billOfLadingDownloadLink: string | null
    packingListDownloadLink: string | null
    customsDownloadLink: string | null
    cargoType: string
    products: IProduct[]
  }
  
  export interface IProduct {
    hsCode: string
    name: string
    quantity: number
    weight: number
    containerNumber: string | null
    containerSize: string | null
    containerType: string | null
  }
