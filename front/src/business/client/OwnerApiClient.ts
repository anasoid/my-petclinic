import { DeleteOwnerRequest, ListOwnersRequest, OwnerClient } from '@gensrc/petclinic/apis';
import { PetClinicConfigure } from './configuration/PetClinicConfigure';

export class OwnerApiClient {
    private configure: PetClinicConfigure;
    constructor() {
        this.configure = new PetClinicConfigure();
    }

    private getOwnerApi() {
        return new OwnerClient(this.configure.getConfiguration());
    }
    public listOwners() {
        const response = this.getOwnerApi().listOwners({} as ListOwnersRequest);
        return response.then((res) => res);
    }
    public deleteOwner(ownerId: number) {
        const response = this.getOwnerApi().deleteOwner({ ownerId: ownerId } as DeleteOwnerRequest);
        return response.then((res) => res);
    }
}
