import { Microservice } from 'src/app/shared/models/Microservice';

export interface MicroservicesState {
    microservices: Microservice[];
    active: Microservice;
    success: boolean;
    swaggers: any[];
    subscribed: Microservice[];
    publique : Microservice[];
    shared : Microservice[];
    personnal : Microservice[];
}
