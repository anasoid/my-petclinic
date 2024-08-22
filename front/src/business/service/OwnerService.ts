import { OwnerDto } from '@gensrc/petclinic';
import { OwnerApiClient } from '../client/OwnerApiClient';

export class OwnerService {
    private ownerApiClient: OwnerApiClient = new OwnerApiClient();
    public list() {
        return this.ownerApiClient.list();
    }
    public delete(ownerId: number) {
        return this.ownerApiClient.delete(ownerId);
    }
    public save(owner: OwnerDto) {
        return this.ownerApiClient.save(owner);
    }
}
