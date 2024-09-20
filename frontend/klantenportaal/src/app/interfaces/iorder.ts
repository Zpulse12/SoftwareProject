export interface IOrder {
    customerCode: string | null
    referenceNumber: string
    customerReferenceNumber: string
    state: string
    transportType: string
    portOfOriginCode: string
    portOfOriginName: string
    portOfDestinationCode: string
    portOfDestinationName: string
    shipName: string
    ets: string | null
    ats: string | null
    eta: string | null
    ata: string | null
    cargoType: string
    totalWeight: number
    totalContainers: number | null
    containerTypes: string[]
    shipMMSI: string | null
}