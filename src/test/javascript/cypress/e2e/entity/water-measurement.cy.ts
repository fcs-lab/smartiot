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

describe('WaterMeasurement e2e test', () => {
  const waterMeasurementPageUrl = '/water-measurement';
  const waterMeasurementPageUrlPattern = new RegExp('/water-measurement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const waterMeasurementSample = { measurementDate: '2024-06-04T02:12:48.618Z', waterLevel: 130.12 };

  let waterMeasurement;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/water-measurements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/water-measurements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/water-measurements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (waterMeasurement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/water-measurements/${waterMeasurement.id}`,
      }).then(() => {
        waterMeasurement = undefined;
      });
    }
  });

  it('WaterMeasurements menu should load WaterMeasurements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('water-measurement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WaterMeasurement').should('exist');
    cy.url().should('match', waterMeasurementPageUrlPattern);
  });

  describe('WaterMeasurement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(waterMeasurementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WaterMeasurement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/water-measurement/new$'));
        cy.getEntityCreateUpdateHeading('WaterMeasurement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterMeasurementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/water-measurements',
          body: waterMeasurementSample,
        }).then(({ body }) => {
          waterMeasurement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/water-measurements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/water-measurements?page=0&size=20>; rel="last",<http://localhost/api/water-measurements?page=0&size=20>; rel="first"',
              },
              body: [waterMeasurement],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(waterMeasurementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WaterMeasurement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('waterMeasurement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterMeasurementPageUrlPattern);
      });

      it('edit button click should load edit WaterMeasurement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WaterMeasurement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterMeasurementPageUrlPattern);
      });

      it('edit button click should load edit WaterMeasurement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WaterMeasurement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterMeasurementPageUrlPattern);
      });

      it('last delete button click should delete instance of WaterMeasurement', () => {
        cy.intercept('GET', '/api/water-measurements/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('waterMeasurement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterMeasurementPageUrlPattern);

        waterMeasurement = undefined;
      });
    });
  });

  describe('new WaterMeasurement page', () => {
    beforeEach(() => {
      cy.visit(`${waterMeasurementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WaterMeasurement');
    });

    it('should create an instance of WaterMeasurement', () => {
      cy.get(`[data-cy="measurementDate"]`).type('2024-06-03T17:57');
      cy.get(`[data-cy="measurementDate"]`).blur();
      cy.get(`[data-cy="measurementDate"]`).should('have.value', '2024-06-03T17:57');

      cy.get(`[data-cy="waterLevel"]`).type('16795.91');
      cy.get(`[data-cy="waterLevel"]`).should('have.value', '16795.91');

      cy.get(`[data-cy="waterQuality"]`).type('uselessly square');
      cy.get(`[data-cy="waterQuality"]`).should('have.value', 'uselessly square');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        waterMeasurement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', waterMeasurementPageUrlPattern);
    });
  });
});
