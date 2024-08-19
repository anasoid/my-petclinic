import { Configuration, ConfigurationParameters } from '@gensrc/petclinic';

export class PetClinicConfigure {
    constructor() {}

    public getConfiguration(): Configuration {
        return new Configuration({ basePath: this.getUrl() } as ConfigurationParameters);
    }
    public getUrl(): string {
        return 'http://localhost:8080/petclinic/api';
    }
}
