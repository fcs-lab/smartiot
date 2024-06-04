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

describe('AggregatedData e2e test', () => {
  const aggregatedDataPageUrl = '/aggregated-data';
  const aggregatedDataPageUrlPattern = new RegExp('/aggregated-data(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const aggregatedDataSample = { dataType: 'surprisingly', value: 'stab print', aggregationTime: '2024-06-03T09:19:44.296Z' };

  let aggregatedData;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/aggregated-data+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/aggregated-data').as('postEntityRequest');
    cy.intercept('DELETE', '/api/aggregated-data/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (aggregatedData) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/aggregated-data/${aggregatedData.id}`,
      }).then(() => {
        aggregatedData = undefined;
      });
    }
  });

  it('AggregatedData menu should load AggregatedData page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('aggregated-data');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AggregatedData').should('exist');
    cy.url().should('match', aggregatedDataPageUrlPattern);
  });

  describe('AggregatedData page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(aggregatedDataPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AggregatedData page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/aggregated-data/new$'));
        cy.getEntityCreateUpdateHeading('AggregatedData');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', aggregatedDataPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/aggregated-data',
          body: aggregatedDataSample,
        }).then(({ body }) => {
          aggregatedData = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/aggregated-data+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/aggregated-data?page=0&size=20>; rel="last",<http://localhost/api/aggregated-data?page=0&size=20>; rel="first"',
              },
              body: [aggregatedData],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(aggregatedDataPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AggregatedData page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('aggregatedData');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', aggregatedDataPageUrlPattern);
      });

      it('edit button click should load edit AggregatedData page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AggregatedData');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', aggregatedDataPageUrlPattern);
      });

      it('edit button click should load edit AggregatedData page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AggregatedData');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', aggregatedDataPageUrlPattern);
      });

      it('last delete button click should delete instance of AggregatedData', () => {
        cy.intercept('GET', '/api/aggregated-data/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('aggregatedData').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', aggregatedDataPageUrlPattern);

        aggregatedData = undefined;
      });
    });
  });

  describe('new AggregatedData page', () => {
    beforeEach(() => {
      cy.visit(`${aggregatedDataPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AggregatedData');
    });

    it('should create an instance of AggregatedData', () => {
      cy.get(`[data-cy="dataType"]`).type('astride as excitedly');
      cy.get(`[data-cy="dataType"]`).should('have.value', 'astride as excitedly');

      cy.get(`[data-cy="value"]`).type('fragrant');
      cy.get(`[data-cy="value"]`).should('have.value', 'fragrant');

      cy.get(`[data-cy="aggregationTime"]`).type('2024-06-03T20:58');
      cy.get(`[data-cy="aggregationTime"]`).blur();
      cy.get(`[data-cy="aggregationTime"]`).should('have.value', '2024-06-03T20:58');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        aggregatedData = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', aggregatedDataPageUrlPattern);
    });
  });
});
