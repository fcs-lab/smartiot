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

describe('DeviceTelemetry e2e test', () => {
  const deviceTelemetryPageUrl = '/device-telemetry';
  const deviceTelemetryPageUrlPattern = new RegExp('/device-telemetry(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const deviceTelemetrySample = { telemetryTimestamp: '2024-06-03T23:33:14.121Z', latitude: 25813.47, longitude: 26633.09 };

  let deviceTelemetry;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/device-telemetries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/device-telemetries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/device-telemetries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (deviceTelemetry) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/device-telemetries/${deviceTelemetry.id}`,
      }).then(() => {
        deviceTelemetry = undefined;
      });
    }
  });

  it('DeviceTelemetries menu should load DeviceTelemetries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('device-telemetry');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DeviceTelemetry').should('exist');
    cy.url().should('match', deviceTelemetryPageUrlPattern);
  });

  describe('DeviceTelemetry page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(deviceTelemetryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DeviceTelemetry page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/device-telemetry/new$'));
        cy.getEntityCreateUpdateHeading('DeviceTelemetry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', deviceTelemetryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/device-telemetries',
          body: deviceTelemetrySample,
        }).then(({ body }) => {
          deviceTelemetry = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/device-telemetries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/device-telemetries?page=0&size=20>; rel="last",<http://localhost/api/device-telemetries?page=0&size=20>; rel="first"',
              },
              body: [deviceTelemetry],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(deviceTelemetryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DeviceTelemetry page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('deviceTelemetry');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', deviceTelemetryPageUrlPattern);
      });

      it('edit button click should load edit DeviceTelemetry page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DeviceTelemetry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', deviceTelemetryPageUrlPattern);
      });

      it('edit button click should load edit DeviceTelemetry page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DeviceTelemetry');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', deviceTelemetryPageUrlPattern);
      });

      it('last delete button click should delete instance of DeviceTelemetry', () => {
        cy.intercept('GET', '/api/device-telemetries/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('deviceTelemetry').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', deviceTelemetryPageUrlPattern);

        deviceTelemetry = undefined;
      });
    });
  });

  describe('new DeviceTelemetry page', () => {
    beforeEach(() => {
      cy.visit(`${deviceTelemetryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DeviceTelemetry');
    });

    it('should create an instance of DeviceTelemetry', () => {
      cy.get(`[data-cy="telemetryTimestamp"]`).type('2024-06-03T11:24');
      cy.get(`[data-cy="telemetryTimestamp"]`).blur();
      cy.get(`[data-cy="telemetryTimestamp"]`).should('have.value', '2024-06-03T11:24');

      cy.get(`[data-cy="latitude"]`).type('19253.71');
      cy.get(`[data-cy="latitude"]`).should('have.value', '19253.71');

      cy.get(`[data-cy="longitude"]`).type('11611.18');
      cy.get(`[data-cy="longitude"]`).should('have.value', '11611.18');

      cy.get(`[data-cy="speed"]`).type('32473.77');
      cy.get(`[data-cy="speed"]`).should('have.value', '32473.77');

      cy.get(`[data-cy="fuelLevel"]`).type('17279.87');
      cy.get(`[data-cy="fuelLevel"]`).should('have.value', '17279.87');

      cy.get(`[data-cy="engineStatus"]`).type('but thoughtfully untrue');
      cy.get(`[data-cy="engineStatus"]`).should('have.value', 'but thoughtfully untrue');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        deviceTelemetry = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', deviceTelemetryPageUrlPattern);
    });
  });
});
