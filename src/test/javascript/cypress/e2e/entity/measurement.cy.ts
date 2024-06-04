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

describe('Measurement e2e test', () => {
  const measurementPageUrl = '/measurement';
  const measurementPageUrlPattern = new RegExp('/measurement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const measurementSample = { measurementType: 'now', value: 'and apropos meh', measurementTime: '2024-06-03T06:03:55.891Z' };

  let measurement;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/measurements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/measurements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/measurements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (measurement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/measurements/${measurement.id}`,
      }).then(() => {
        measurement = undefined;
      });
    }
  });

  it('Measurements menu should load Measurements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('measurement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Measurement').should('exist');
    cy.url().should('match', measurementPageUrlPattern);
  });

  describe('Measurement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(measurementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Measurement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/measurement/new$'));
        cy.getEntityCreateUpdateHeading('Measurement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', measurementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/measurements',
          body: measurementSample,
        }).then(({ body }) => {
          measurement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/measurements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/measurements?page=0&size=20>; rel="last",<http://localhost/api/measurements?page=0&size=20>; rel="first"',
              },
              body: [measurement],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(measurementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Measurement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('measurement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', measurementPageUrlPattern);
      });

      it('edit button click should load edit Measurement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Measurement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', measurementPageUrlPattern);
      });

      it('edit button click should load edit Measurement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Measurement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', measurementPageUrlPattern);
      });

      it('last delete button click should delete instance of Measurement', () => {
        cy.intercept('GET', '/api/measurements/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('measurement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', measurementPageUrlPattern);

        measurement = undefined;
      });
    });
  });

  describe('new Measurement page', () => {
    beforeEach(() => {
      cy.visit(`${measurementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Measurement');
    });

    it('should create an instance of Measurement', () => {
      cy.get(`[data-cy="measurementType"]`).type('independence ah despite');
      cy.get(`[data-cy="measurementType"]`).should('have.value', 'independence ah despite');

      cy.get(`[data-cy="value"]`).type('once');
      cy.get(`[data-cy="value"]`).should('have.value', 'once');

      cy.get(`[data-cy="measurementTime"]`).type('2024-06-03T16:17');
      cy.get(`[data-cy="measurementTime"]`).blur();
      cy.get(`[data-cy="measurementTime"]`).should('have.value', '2024-06-03T16:17');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        measurement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', measurementPageUrlPattern);
    });
  });
});
