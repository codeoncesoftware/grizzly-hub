import { Swagger } from 'src/app/shared/models/Swagger';

export interface SwaggerState {
    swaggers: Swagger[];
    active: Swagger;
    success: boolean;
}
