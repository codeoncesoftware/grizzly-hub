import { Swagger } from './Swagger';

export class Microservice {
    id: string;
    title: string;
    description: string;
    faq: any[];
    documentation : any[];
    swaggersVersions: any[];
    owner: string;
    type: string;
    teamIds: any[];
}