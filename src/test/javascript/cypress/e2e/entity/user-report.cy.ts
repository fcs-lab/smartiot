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

describe('UserReport e2e test', () => {
  const userReportPageUrl = '/user-report';
  const userReportPageUrlPattern = new RegExp('/user-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userReportSample = {
    reportType: 'incidentally',
    generatedAt: '2024-06-03T23:05:37.163Z',
    reportContent: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
  };

  let userReport;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-reports/${userReport.id}`,
      }).then(() => {
        userReport = undefined;
      });
    }
  });

  it('UserReports menu should load UserReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserReport').should('exist');
    cy.url().should('match', userReportPageUrlPattern);
  });

  describe('UserReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserReport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-report/new$'));
        cy.getEntityCreateUpdateHeading('UserReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-reports',
          body: userReportSample,
        }).then(({ body }) => {
          userReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-reports?page=0&size=20>; rel="last",<http://localhost/api/user-reports?page=0&size=20>; rel="first"',
              },
              body: [userReport],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userReport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });

      it('edit button click should load edit UserReport page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });

      it('edit button click should load edit UserReport page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserReport');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });

      it('last delete button click should delete instance of UserReport', () => {
        cy.intercept('GET', '/api/user-reports/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);

        userReport = undefined;
      });
    });
  });

  describe('new UserReport page', () => {
    beforeEach(() => {
      cy.visit(`${userReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserReport');
    });

    it('should create an instance of UserReport', () => {
      cy.get(`[data-cy="reportType"]`).type('via');
      cy.get(`[data-cy="reportType"]`).should('have.value', 'via');

      cy.get(`[data-cy="generatedAt"]`).type('2024-06-03T11:04');
      cy.get(`[data-cy="generatedAt"]`).blur();
      cy.get(`[data-cy="generatedAt"]`).should('have.value', '2024-06-03T11:04');

      cy.get(`[data-cy="reportContent"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="reportContent"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userReport = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userReportPageUrlPattern);
    });
  });
});
