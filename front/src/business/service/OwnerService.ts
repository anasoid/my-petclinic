import { OwnerApiClient } from '../client/OwnerApiClient';

export class OwnerService {
    private ownerApiClient: OwnerApiClient = new OwnerApiClient();
    public listOwners() {
        return this.ownerApiClient.listOwners();
    }
    public deleteOwner(ownerId: number) {
        return this.ownerApiClient.deleteOwner(ownerId);
    }
}
