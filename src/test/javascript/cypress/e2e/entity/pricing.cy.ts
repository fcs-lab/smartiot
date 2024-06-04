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

describe('Pricing e2e test', () => {
  const pricingPageUrl = '/pricing';
  const pricingPageUrlPattern = new RegExp('/pricing(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const pricingSample = { serviceType: 'cheery that presume', price: 14817.71 };

  let pricing;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pricings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pricings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pricings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (pricing) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pricings/${pricing.id}`,
      }).then(() => {
        pricing = undefined;
      });
    }
  });

  it('Pricings menu should load Pricings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pricing');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Pricing').should('exist');
    cy.url().should('match', pricingPageUrlPattern);
  });

  describe('Pricing page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pricingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Pricing page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pricing/new$'));
        cy.getEntityCreateUpdateHeading('Pricing');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pricingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pricings',
          body: pricingSample,
        }).then(({ body }) => {
          pricing = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pricings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pricings?page=0&size=20>; rel="last",<http://localhost/api/pricings?page=0&size=20>; rel="first"',
              },
              body: [pricing],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pricingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Pricing page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pricing');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pricingPageUrlPattern);
      });

      it('edit button click should load edit Pricing page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pricing');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pricingPageUrlPattern);
      });

      it('edit button click should load edit Pricing page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pricing');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pricingPageUrlPattern);
      });

      it('last delete button click should delete instance of Pricing', () => {
        cy.intercept('GET', '/api/pricings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('pricing').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pricingPageUrlPattern);

        pricing = undefined;
      });
    });
  });

  describe('new Pricing page', () => {
    beforeEach(() => {
      cy.visit(`${pricingPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Pricing');
    });

    it('should create an instance of Pricing', () => {
      cy.get(`[data-cy="serviceType"]`).type('prosecutor exonerate');
      cy.get(`[data-cy="serviceType"]`).should('have.value', 'prosecutor exonerate');

      cy.get(`[data-cy="price"]`).type('19108.85');
      cy.get(`[data-cy="price"]`).should('have.value', '19108.85');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pricing = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', pricingPageUrlPattern);
    });
  });
});
