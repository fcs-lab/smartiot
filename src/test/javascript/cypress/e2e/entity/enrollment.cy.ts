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

describe('Enrollment e2e test', () => {
  const enrollmentPageUrl = '/enrollment';
  const enrollmentPageUrlPattern = new RegExp('/enrollment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const enrollmentSample = { enrollmentType: 'accidentally', enrollmentDate: '2024-06-03T09:27:32.314Z' };

  let enrollment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/enrollments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/enrollments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/enrollments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (enrollment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/enrollments/${enrollment.id}`,
      }).then(() => {
        enrollment = undefined;
      });
    }
  });

  it('Enrollments menu should load Enrollments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('enrollment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Enrollment').should('exist');
    cy.url().should('match', enrollmentPageUrlPattern);
  });

  describe('Enrollment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(enrollmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Enrollment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/enrollment/new$'));
        cy.getEntityCreateUpdateHeading('Enrollment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', enrollmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/enrollments',
          body: enrollmentSample,
        }).then(({ body }) => {
          enrollment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/enrollments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/enrollments?page=0&size=20>; rel="last",<http://localhost/api/enrollments?page=0&size=20>; rel="first"',
              },
              body: [enrollment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(enrollmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Enrollment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('enrollment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', enrollmentPageUrlPattern);
      });

      it('edit button click should load edit Enrollment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enrollment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', enrollmentPageUrlPattern);
      });

      it('edit button click should load edit Enrollment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enrollment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', enrollmentPageUrlPattern);
      });

      it('last delete button click should delete instance of Enrollment', () => {
        cy.intercept('GET', '/api/enrollments/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('enrollment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', enrollmentPageUrlPattern);

        enrollment = undefined;
      });
    });
  });

  describe('new Enrollment page', () => {
    beforeEach(() => {
      cy.visit(`${enrollmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Enrollment');
    });

    it('should create an instance of Enrollment', () => {
      cy.get(`[data-cy="enrollmentType"]`).type('provided shock');
      cy.get(`[data-cy="enrollmentType"]`).should('have.value', 'provided shock');

      cy.get(`[data-cy="enrollmentDate"]`).type('2024-06-03T17:27');
      cy.get(`[data-cy="enrollmentDate"]`).blur();
      cy.get(`[data-cy="enrollmentDate"]`).should('have.value', '2024-06-03T17:27');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        enrollment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', enrollmentPageUrlPattern);
    });
  });
});
