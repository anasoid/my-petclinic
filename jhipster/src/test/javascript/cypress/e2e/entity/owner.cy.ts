import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Owner e2e test', () => {
  const ownerPageUrl = '/owner';
  const ownerPageUrlPattern = new RegExp('/owner(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ownerSample = { firstName: 'Vivianne', address: 'aw', city: 'Gresham', telephone: 13398 };

  let owner;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/owners+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/owners').as('postEntityRequest');
    cy.intercept('DELETE', '/api/owners/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (owner) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/owners/${owner.id}`,
      }).then(() => {
        owner = undefined;
      });
    }
  });

  it('Owners menu should load Owners page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('owner');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Owner').should('exist');
    cy.url().should('match', ownerPageUrlPattern);
  });

  describe('Owner page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ownerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Owner page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/owner/new$'));
        cy.getEntityCreateUpdateHeading('Owner');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ownerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/owners',
          body: ownerSample,
        }).then(({ body }) => {
          owner = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/owners+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/owners?page=0&size=20>; rel="last",<http://localhost/api/owners?page=0&size=20>; rel="first"',
              },
              body: [owner],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ownerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Owner page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('owner');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ownerPageUrlPattern);
      });

      it('edit button click should load edit Owner page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Owner');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ownerPageUrlPattern);
      });

      it('edit button click should load edit Owner page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Owner');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ownerPageUrlPattern);
      });

      it('last delete button click should delete instance of Owner', () => {
        cy.intercept('GET', '/api/owners/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('owner').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ownerPageUrlPattern);

        owner = undefined;
      });
    });
  });

  describe('new Owner page', () => {
    beforeEach(() => {
      cy.visit(`${ownerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Owner');
    });

    it('should create an instance of Owner', () => {
      cy.get(`[data-cy="firstName"]`).type('Esmeralda');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Esmeralda');

      cy.get(`[data-cy="lastName"]`).type('Collins-Wehner');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Collins-Wehner');

      cy.get(`[data-cy="address"]`).type('fooey difficulty offensively');
      cy.get(`[data-cy="address"]`).should('have.value', 'fooey difficulty offensively');

      cy.get(`[data-cy="city"]`).type('East Luigiberg');
      cy.get(`[data-cy="city"]`).should('have.value', 'East Luigiberg');

      cy.get(`[data-cy="telephone"]`).type('2876');
      cy.get(`[data-cy="telephone"]`).should('have.value', '2876');

      cy.get(`[data-cy="ffff"]`).type('sunbathe sad blah');
      cy.get(`[data-cy="ffff"]`).should('have.value', 'sunbathe sad blah');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        owner = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', ownerPageUrlPattern);
    });
  });
});
