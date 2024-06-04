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

describe('SystemAlert e2e test', () => {
  const systemAlertPageUrl = '/system-alert';
  const systemAlertPageUrlPattern = new RegExp('/system-alert(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const systemAlertSample = { createdAt: '2024-06-03T15:43:59.408Z', alertDescription: 'mixed until than', alertType: 'consequently' };

  let systemAlert;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/system-alerts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/system-alerts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/system-alerts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (systemAlert) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/system-alerts/${systemAlert.id}`,
      }).then(() => {
        systemAlert = undefined;
      });
    }
  });

  it('SystemAlerts menu should load SystemAlerts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('system-alert');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SystemAlert').should('exist');
    cy.url().should('match', systemAlertPageUrlPattern);
  });

  describe('SystemAlert page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(systemAlertPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SystemAlert page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/system-alert/new$'));
        cy.getEntityCreateUpdateHeading('SystemAlert');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', systemAlertPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/system-alerts',
          body: systemAlertSample,
        }).then(({ body }) => {
          systemAlert = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/system-alerts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/system-alerts?page=0&size=20>; rel="last",<http://localhost/api/system-alerts?page=0&size=20>; rel="first"',
              },
              body: [systemAlert],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(systemAlertPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SystemAlert page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('systemAlert');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', systemAlertPageUrlPattern);
      });

      it('edit button click should load edit SystemAlert page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SystemAlert');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', systemAlertPageUrlPattern);
      });

      it('edit button click should load edit SystemAlert page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SystemAlert');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', systemAlertPageUrlPattern);
      });

      it('last delete button click should delete instance of SystemAlert', () => {
        cy.intercept('GET', '/api/system-alerts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('systemAlert').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', systemAlertPageUrlPattern);

        systemAlert = undefined;
      });
    });
  });

  describe('new SystemAlert page', () => {
    beforeEach(() => {
      cy.visit(`${systemAlertPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SystemAlert');
    });

    it('should create an instance of SystemAlert', () => {
      cy.get(`[data-cy="createdAt"]`).type('2024-06-03T19:19');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-06-03T19:19');

      cy.get(`[data-cy="updatedAt"]`).type('2024-06-03T10:13');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-06-03T10:13');

      cy.get(`[data-cy="alertDescription"]`).type('woot gee brightly');
      cy.get(`[data-cy="alertDescription"]`).should('have.value', 'woot gee brightly');

      cy.get(`[data-cy="alertType"]`).type('radiant tragic qua');
      cy.get(`[data-cy="alertType"]`).should('have.value', 'radiant tragic qua');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        systemAlert = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', systemAlertPageUrlPattern);
    });
  });
});
