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

describe('VehicleStatusLog e2e test', () => {
  const vehicleStatusLogPageUrl = '/vehicle-status-log';
  const vehicleStatusLogPageUrlPattern = new RegExp('/vehicle-status-log(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleStatusLogSample = { statusChangeDate: '2024-06-03T18:44:38.648Z', newStatus: 'MAINTENANCE' };

  let vehicleStatusLog;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-status-logs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-status-logs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-status-logs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleStatusLog) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-status-logs/${vehicleStatusLog.id}`,
      }).then(() => {
        vehicleStatusLog = undefined;
      });
    }
  });

  it('VehicleStatusLogs menu should load VehicleStatusLogs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-status-log');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleStatusLog').should('exist');
    cy.url().should('match', vehicleStatusLogPageUrlPattern);
  });

  describe('VehicleStatusLog page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleStatusLogPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleStatusLog page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-status-log/new$'));
        cy.getEntityCreateUpdateHeading('VehicleStatusLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleStatusLogPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-status-logs',
          body: vehicleStatusLogSample,
        }).then(({ body }) => {
          vehicleStatusLog = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-status-logs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vehicle-status-logs?page=0&size=20>; rel="last",<http://localhost/api/vehicle-status-logs?page=0&size=20>; rel="first"',
              },
              body: [vehicleStatusLog],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleStatusLogPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleStatusLog page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleStatusLog');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleStatusLogPageUrlPattern);
      });

      it('edit button click should load edit VehicleStatusLog page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleStatusLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleStatusLogPageUrlPattern);
      });

      it('edit button click should load edit VehicleStatusLog page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleStatusLog');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleStatusLogPageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleStatusLog', () => {
        cy.intercept('GET', '/api/vehicle-status-logs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleStatusLog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleStatusLogPageUrlPattern);

        vehicleStatusLog = undefined;
      });
    });
  });

  describe('new VehicleStatusLog page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleStatusLogPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleStatusLog');
    });

    it('should create an instance of VehicleStatusLog', () => {
      cy.get(`[data-cy="statusChangeDate"]`).type('2024-06-03T21:19');
      cy.get(`[data-cy="statusChangeDate"]`).blur();
      cy.get(`[data-cy="statusChangeDate"]`).should('have.value', '2024-06-03T21:19');

      cy.get(`[data-cy="newStatus"]`).select('MAINTENANCE');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vehicleStatusLog = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleStatusLogPageUrlPattern);
    });
  });
});
