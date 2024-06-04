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

describe('ApplicationUser e2e test', () => {
  const applicationUserPageUrl = '/application-user';
  const applicationUserPageUrlPattern = new RegExp('/application-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const applicationUserSample = { userLogin: 'versus until tender', firstName: 'Leonora', lastName: 'Koelpin', emailAddress: 'where whoa' };

  let applicationUser;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/application-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/application-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/application-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (applicationUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/application-users/${applicationUser.id}`,
      }).then(() => {
        applicationUser = undefined;
      });
    }
  });

  it('ApplicationUsers menu should load ApplicationUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('application-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ApplicationUser').should('exist');
    cy.url().should('match', applicationUserPageUrlPattern);
  });

  describe('ApplicationUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(applicationUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ApplicationUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/application-user/new$'));
        cy.getEntityCreateUpdateHeading('ApplicationUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/application-users',
          body: applicationUserSample,
        }).then(({ body }) => {
          applicationUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/application-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/application-users?page=0&size=20>; rel="last",<http://localhost/api/application-users?page=0&size=20>; rel="first"',
              },
              body: [applicationUser],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(applicationUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ApplicationUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('applicationUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationUserPageUrlPattern);
      });

      it('edit button click should load edit ApplicationUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ApplicationUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationUserPageUrlPattern);
      });

      it('edit button click should load edit ApplicationUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ApplicationUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationUserPageUrlPattern);
      });

      it('last delete button click should delete instance of ApplicationUser', () => {
        cy.intercept('GET', '/api/application-users/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('applicationUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationUserPageUrlPattern);

        applicationUser = undefined;
      });
    });
  });

  describe('new ApplicationUser page', () => {
    beforeEach(() => {
      cy.visit(`${applicationUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ApplicationUser');
    });

    it('should create an instance of ApplicationUser', () => {
      cy.get(`[data-cy="userLogin"]`).type('bristle towards');
      cy.get(`[data-cy="userLogin"]`).should('have.value', 'bristle towards');

      cy.get(`[data-cy="firstName"]`).type('Felicia');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Felicia');

      cy.get(`[data-cy="lastName"]`).type('Paucek');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Paucek');

      cy.get(`[data-cy="emailAddress"]`).type('zealous');
      cy.get(`[data-cy="emailAddress"]`).should('have.value', 'zealous');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        applicationUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', applicationUserPageUrlPattern);
    });
  });
});
