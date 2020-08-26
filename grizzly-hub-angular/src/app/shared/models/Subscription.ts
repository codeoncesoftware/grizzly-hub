export class SubscriptionDto {
    id: string;
    microserviceId: string;
    frequence: number;
    env: string[];
    chnagesOptions: string[];
    emailOption: boolean;
}