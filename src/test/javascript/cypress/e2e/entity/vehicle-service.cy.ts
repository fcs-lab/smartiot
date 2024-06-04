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

describe('VehicleService e2e test', () => {
  const vehicleServicePageUrl = '/vehicle-service';
  const vehicleServicePageUrlPattern = new RegExp('/vehicle-service(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleServiceSample = { serviceName: 'oof psst ick', serviceDate: '2024-06-03' };

  let vehicleService;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-services+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-services').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-services/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleService) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-services/${vehicleService.id}`,
      }).then(() => {
        vehicleService = undefined;
      });
    }
  });

  it('VehicleServices menu should load VehicleServices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-service');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleService').should('exist');
    cy.url().should('match', vehicleServicePageUrlPattern);
  });

  describe('VehicleService page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleServicePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleService page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-service/new$'));
        cy.getEntityCreateUpdateHeading('VehicleService');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleServicePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-services',
          body: vehicleServiceSample,
        }).then(({ body }) => {
          vehicleService = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-services+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vehicle-services?page=0&size=20>; rel="last",<http://localhost/api/vehicle-services?page=0&size=20>; rel="first"',
              },
              body: [vehicleService],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleServicePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleService page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleService');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleServicePageUrlPattern);
      });

      it('edit button click should load edit VehicleService page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleService');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleServicePageUrlPattern);
      });

      it('edit button click should load edit VehicleService page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleService');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleServicePageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleService', () => {
        cy.intercept('GET', '/api/vehicle-services/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleService').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleServicePageUrlPattern);

        vehicleService = undefined;
      });
    });
  });

  describe('new VehicleService page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleServicePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleService');
    });

    it('should create an instance of VehicleService', () => {
      cy.get(`[data-cy="serviceName"]`).type('across pluralise before');
      cy.get(`[data-cy="serviceName"]`).should('have.value', 'across pluralise before');

      cy.get(`[data-cy="serviceDate"]`).type('2024-06-03');
      cy.get(`[data-cy="serviceDate"]`).blur();
      cy.get(`[data-cy="serviceDate"]`).should('have.value', '2024-06-03');

      cy.get(`[data-cy="serviceDescription"]`).type('when');
      cy.get(`[data-cy="serviceDescription"]`).should('have.value', 'when');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vehicleService = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleServicePageUrlPattern);
    });
  });
});
