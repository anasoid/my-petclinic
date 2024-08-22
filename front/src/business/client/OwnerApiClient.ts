import { OwnerDto } from '@gensrc/petclinic';
import { AddOwnerRequest, DeleteOwnerRequest, ListOwnersRequest, OwnerClient, UpdateOwnerRequest } from '@gensrc/petclinic/apis';
import { PetClinicConfigure } from './configuration/PetClinicConfigure';

export class OwnerApiClient {
    private configure: PetClinicConfigure;
    constructor() {
        this.configure = new PetClinicConfigure();
    }

    private getOwnerApi() {
        return new OwnerClient(this.configure.getConfiguration());
    }
    public list() {
        const response = this.getOwnerApi().listOwners({} as ListOwnersRequest);
        return response.then((res) => res);
    }
    public delete(ownerId: number) {
        const response = this.getOwnerApi().deleteOwner({ ownerId: ownerId } as DeleteOwnerRequest);
        return response.then((res) => res);
    }
    public save(owner: OwnerDto): Promise<OwnerDto> {
        if (owner.id == undefined) {
            const response = this.getOwnerApi().addOwner({ ownerFieldsDto: owner } as AddOwnerRequest);
            return response.then((res) => res);
        } else {
            const response = this.getOwnerApi().updateOwner({ ownerFieldsDto: owner, ownerId: owner.id } as UpdateOwnerRequest);
            return response.then((res) => res);
        }
    }
}
