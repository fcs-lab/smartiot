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

describe('Consumer e2e test', () => {
  const consumerPageUrl = '/consumer';
  const consumerPageUrlPattern = new RegExp('/consumer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const consumerSample = {
    name: 'inwardly',
    street: 'Johnson Street',
    neighborhood: 'uh-huh voluminous sill',
    propertyNumber: 6530,
    phone: '(468) 642-4025 x9829',
    email: 'Bonnie60@gmail.com',
  };

  let consumer;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/consumers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/consumers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/consumers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (consumer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/consumers/${consumer.id}`,
      }).then(() => {
        consumer = undefined;
      });
    }
  });

  it('Consumers menu should load Consumers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('consumer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Consumer').should('exist');
    cy.url().should('match', consumerPageUrlPattern);
  });

  describe('Consumer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(consumerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Consumer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/consumer/new$'));
        cy.getEntityCreateUpdateHeading('Consumer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', consumerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/consumers',
          body: consumerSample,
        }).then(({ body }) => {
          consumer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/consumers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/consumers?page=0&size=20>; rel="last",<http://localhost/api/consumers?page=0&size=20>; rel="first"',
              },
              body: [consumer],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(consumerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Consumer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('consumer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', consumerPageUrlPattern);
      });

      it('edit button click should load edit Consumer page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Consumer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', consumerPageUrlPattern);
      });

      it('edit button click should load edit Consumer page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Consumer');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', consumerPageUrlPattern);
      });

      it('last delete button click should delete instance of Consumer', () => {
        cy.intercept('GET', '/api/consumers/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('consumer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', consumerPageUrlPattern);

        consumer = undefined;
      });
    });
  });

  describe('new Consumer page', () => {
    beforeEach(() => {
      cy.visit(`${consumerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Consumer');
    });

    it('should create an instance of Consumer', () => {
      cy.get(`[data-cy="name"]`).type('suddenly legal happily');
      cy.get(`[data-cy="name"]`).should('have.value', 'suddenly legal happily');

      cy.get(`[data-cy="street"]`).type('Washington Boulevard');
      cy.get(`[data-cy="street"]`).should('have.value', 'Washington Boulevard');

      cy.get(`[data-cy="neighborhood"]`).type('apud');
      cy.get(`[data-cy="neighborhood"]`).should('have.value', 'apud');

      cy.get(`[data-cy="propertyNumber"]`).type('15042');
      cy.get(`[data-cy="propertyNumber"]`).should('have.value', '15042');

      cy.get(`[data-cy="phone"]`).type('365-430-0326 x77731');
      cy.get(`[data-cy="phone"]`).should('have.value', '365-430-0326 x77731');

      cy.get(`[data-cy="email"]`).type('Khalid.Schinner@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Khalid.Schinner@hotmail.com');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        consumer = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', consumerPageUrlPattern);
    });
  });
});
