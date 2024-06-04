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

describe('WaterUsageLog e2e test', () => {
  const waterUsageLogPageUrl = '/water-usage-log';
  const waterUsageLogPageUrlPattern = new RegExp('/water-usage-log(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const waterUsageLogSample = { usageDate: '2024-06-03T04:34:42.627Z', amountUsed: 12890.61 };

  let waterUsageLog;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/water-usage-logs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/water-usage-logs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/water-usage-logs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (waterUsageLog) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/water-usage-logs/${waterUsageLog.id}`,
      }).then(() => {
        waterUsageLog = undefined;
      });
    }
  });

  it('WaterUsageLogs menu should load WaterUsageLogs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('water-usage-log');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WaterUsageLog').should('exist');
    cy.url().should('match', waterUsageLogPageUrlPattern);
  });

  describe('WaterUsageLog page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(waterUsageLogPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WaterUsageLog page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/water-usage-log/new$'));
        cy.getEntityCreateUpdateHeading('WaterUsageLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterUsageLogPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/water-usage-logs',
          body: waterUsageLogSample,
        }).then(({ body }) => {
          waterUsageLog = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/water-usage-logs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/water-usage-logs?page=0&size=20>; rel="last",<http://localhost/api/water-usage-logs?page=0&size=20>; rel="first"',
              },
              body: [waterUsageLog],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(waterUsageLogPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WaterUsageLog page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('waterUsageLog');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterUsageLogPageUrlPattern);
      });

      it('edit button click should load edit WaterUsageLog page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WaterUsageLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterUsageLogPageUrlPattern);
      });

      it('edit button click should load edit WaterUsageLog page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WaterUsageLog');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterUsageLogPageUrlPattern);
      });

      it('last delete button click should delete instance of WaterUsageLog', () => {
        cy.intercept('GET', '/api/water-usage-logs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('waterUsageLog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterUsageLogPageUrlPattern);

        waterUsageLog = undefined;
      });
    });
  });

  describe('new WaterUsageLog page', () => {
    beforeEach(() => {
      cy.visit(`${waterUsageLogPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WaterUsageLog');
    });

    it('should create an instance of WaterUsageLog', () => {
      cy.get(`[data-cy="usageDate"]`).type('2024-06-03T13:03');
      cy.get(`[data-cy="usageDate"]`).blur();
      cy.get(`[data-cy="usageDate"]`).should('have.value', '2024-06-03T13:03');

      cy.get(`[data-cy="amountUsed"]`).type('12398.9');
      cy.get(`[data-cy="amountUsed"]`).should('have.value', '12398.9');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        waterUsageLog = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', waterUsageLogPageUrlPattern);
    });
  });
});
