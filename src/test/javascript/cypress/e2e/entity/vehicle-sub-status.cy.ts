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

describe('VehicleSubStatus e2e test', () => {
  const vehicleSubStatusPageUrl = '/vehicle-sub-status';
  const vehicleSubStatusPageUrlPattern = new RegExp('/vehicle-sub-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleSubStatusSample = { subStatusName: 'winding' };

  let vehicleSubStatus;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-sub-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-sub-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-sub-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleSubStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-sub-statuses/${vehicleSubStatus.id}`,
      }).then(() => {
        vehicleSubStatus = undefined;
      });
    }
  });

  it('VehicleSubStatuses menu should load VehicleSubStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-sub-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleSubStatus').should('exist');
    cy.url().should('match', vehicleSubStatusPageUrlPattern);
  });

  describe('VehicleSubStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleSubStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleSubStatus page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-sub-status/new$'));
        cy.getEntityCreateUpdateHeading('VehicleSubStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleSubStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-sub-statuses',
          body: vehicleSubStatusSample,
        }).then(({ body }) => {
          vehicleSubStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-sub-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vehicle-sub-statuses?page=0&size=20>; rel="last",<http://localhost/api/vehicle-sub-statuses?page=0&size=20>; rel="first"',
              },
              body: [vehicleSubStatus],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleSubStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleSubStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleSubStatus');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleSubStatusPageUrlPattern);
      });

      it('edit button click should load edit VehicleSubStatus page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleSubStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleSubStatusPageUrlPattern);
      });

      it('edit button click should load edit VehicleSubStatus page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleSubStatus');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleSubStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleSubStatus', () => {
        cy.intercept('GET', '/api/vehicle-sub-statuses/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleSubStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleSubStatusPageUrlPattern);

        vehicleSubStatus = undefined;
      });
    });
  });

  describe('new VehicleSubStatus page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleSubStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleSubStatus');
    });

    it('should create an instance of VehicleSubStatus', () => {
      cy.get(`[data-cy="subStatusName"]`).type('geography accountability');
      cy.get(`[data-cy="subStatusName"]`).should('have.value', 'geography accountability');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vehicleSubStatus = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleSubStatusPageUrlPattern);
    });
  });
});
