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

describe('UserDashboard e2e test', () => {
  const userDashboardPageUrl = '/user-dashboard';
  const userDashboardPageUrlPattern = new RegExp('/user-dashboard(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userDashboardSample = { dashboardName: 'cacao', widgets: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let userDashboard;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-dashboards+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-dashboards').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-dashboards/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userDashboard) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-dashboards/${userDashboard.id}`,
      }).then(() => {
        userDashboard = undefined;
      });
    }
  });

  it('UserDashboards menu should load UserDashboards page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-dashboard');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserDashboard').should('exist');
    cy.url().should('match', userDashboardPageUrlPattern);
  });

  describe('UserDashboard page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userDashboardPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserDashboard page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-dashboard/new$'));
        cy.getEntityCreateUpdateHeading('UserDashboard');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userDashboardPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-dashboards',
          body: userDashboardSample,
        }).then(({ body }) => {
          userDashboard = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-dashboards+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-dashboards?page=0&size=20>; rel="last",<http://localhost/api/user-dashboards?page=0&size=20>; rel="first"',
              },
              body: [userDashboard],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userDashboardPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserDashboard page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userDashboard');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userDashboardPageUrlPattern);
      });

      it('edit button click should load edit UserDashboard page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserDashboard');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userDashboardPageUrlPattern);
      });

      it('edit button click should load edit UserDashboard page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserDashboard');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userDashboardPageUrlPattern);
      });

      it('last delete button click should delete instance of UserDashboard', () => {
        cy.intercept('GET', '/api/user-dashboards/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userDashboard').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userDashboardPageUrlPattern);

        userDashboard = undefined;
      });
    });
  });

  describe('new UserDashboard page', () => {
    beforeEach(() => {
      cy.visit(`${userDashboardPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserDashboard');
    });

    it('should create an instance of UserDashboard', () => {
      cy.get(`[data-cy="dashboardName"]`).type('interact');
      cy.get(`[data-cy="dashboardName"]`).should('have.value', 'interact');

      cy.get(`[data-cy="widgets"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="widgets"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userDashboard = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userDashboardPageUrlPattern);
    });
  });
});
