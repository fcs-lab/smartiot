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

describe('WaterAlert e2e test', () => {
  const waterAlertPageUrl = '/water-alert';
  const waterAlertPageUrlPattern = new RegExp('/water-alert(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const waterAlertSample = { alertType: 'than but square', alertDescription: 'strident', createdDate: '2024-06-03T09:48:08.063Z' };

  let waterAlert;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/water-alerts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/water-alerts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/water-alerts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (waterAlert) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/water-alerts/${waterAlert.id}`,
      }).then(() => {
        waterAlert = undefined;
      });
    }
  });

  it('WaterAlerts menu should load WaterAlerts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('water-alert');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WaterAlert').should('exist');
    cy.url().should('match', waterAlertPageUrlPattern);
  });

  describe('WaterAlert page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(waterAlertPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WaterAlert page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/water-alert/new$'));
        cy.getEntityCreateUpdateHeading('WaterAlert');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterAlertPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/water-alerts',
          body: waterAlertSample,
        }).then(({ body }) => {
          waterAlert = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/water-alerts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/water-alerts?page=0&size=20>; rel="last",<http://localhost/api/water-alerts?page=0&size=20>; rel="first"',
              },
              body: [waterAlert],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(waterAlertPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WaterAlert page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('waterAlert');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterAlertPageUrlPattern);
      });

      it('edit button click should load edit WaterAlert page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WaterAlert');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterAlertPageUrlPattern);
      });

      it('edit button click should load edit WaterAlert page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WaterAlert');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterAlertPageUrlPattern);
      });

      it('last delete button click should delete instance of WaterAlert', () => {
        cy.intercept('GET', '/api/water-alerts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('waterAlert').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', waterAlertPageUrlPattern);

        waterAlert = undefined;
      });
    });
  });

  describe('new WaterAlert page', () => {
    beforeEach(() => {
      cy.visit(`${waterAlertPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WaterAlert');
    });

    it('should create an instance of WaterAlert', () => {
      cy.get(`[data-cy="alertType"]`).type('than south');
      cy.get(`[data-cy="alertType"]`).should('have.value', 'than south');

      cy.get(`[data-cy="alertDescription"]`).type('for eek');
      cy.get(`[data-cy="alertDescription"]`).should('have.value', 'for eek');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-03T09:26');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-03T09:26');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        waterAlert = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', waterAlertPageUrlPattern);
    });
  });
});
