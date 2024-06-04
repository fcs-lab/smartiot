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

describe('WaterSensor e2e test', () => {
  const waterSensorPageUrl = '/water-sensor';
  const waterSensorPageUrlPattern = new RegExp('/water-sensor(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const waterSensorSample = { sensorId: 'aha yippee budget', sensorStatus: 'INACTIVE' };

  let waterSensor;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/water-sensors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/water-sensors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/water-sensors/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (waterSensor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/water-sensors/${waterSensor.id}`,
      }).then(() => {
        waterSensor = undefined;
      });
    }
  });

  it('WaterSensors menu should load WaterSensors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('water-sensor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WaterSensor').should('exist');
    cy.url().should('match', waterSensorPageUrlPattern);
  });

  describe('WaterSensor page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(waterSensorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WaterSensor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/water-sensor/new$'));
        cy.getEntityCreateUpdateHeading('WaterSensor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterSensorPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/water-sensors',
          body: waterSensorSample,
        }).then(({ body }) => {
          waterSensor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/water-sensors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/water-sensors?page=0&size=20>; rel="last",<http://localhost/api/water-sensors?page=0&size=20>; rel="first"',
              },
              body: [waterSensor],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(waterSensorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WaterSensor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('waterSensor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterSensorPageUrlPattern);
      });

      it('edit button click should load edit WaterSensor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WaterSensor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterSensorPageUrlPattern);
      });

      it('edit button click should load edit WaterSensor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WaterSensor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterSensorPageUrlPattern);
      });

      it('last delete button click should delete instance of WaterSensor', () => {
        cy.intercept('GET', '/api/water-sensors/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('waterSensor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterSensorPageUrlPattern);

        waterSensor = undefined;
      });
    });
  });

  describe('new WaterSensor page', () => {
    beforeEach(() => {
      cy.visit(`${waterSensorPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WaterSensor');
    });

    it('should create an instance of WaterSensor', () => {
      cy.get(`[data-cy="sensorId"]`).type('midst between');
      cy.get(`[data-cy="sensorId"]`).should('have.value', 'midst between');

      cy.get(`[data-cy="sensorStatus"]`).select('MAINTENANCE');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        waterSensor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', waterSensorPageUrlPattern);
    });
  });
});
