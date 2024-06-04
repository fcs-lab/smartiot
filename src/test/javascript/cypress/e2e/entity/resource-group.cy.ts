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

describe('ResourceGroup e2e test', () => {
  const resourceGroupPageUrl = '/resource-group';
  const resourceGroupPageUrlPattern = new RegExp('/resource-group(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const resourceGroupSample = { name: 'but', description: 'trolley gripping' };

  let resourceGroup;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/resource-groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/resource-groups').as('postEntityRequest');
    cy.intercept('DELETE', '/api/resource-groups/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (resourceGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/resource-groups/${resourceGroup.id}`,
      }).then(() => {
        resourceGroup = undefined;
      });
    }
  });

  it('ResourceGroups menu should load ResourceGroups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('resource-group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ResourceGroup').should('exist');
    cy.url().should('match', resourceGroupPageUrlPattern);
  });

  describe('ResourceGroup page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(resourceGroupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ResourceGroup page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/resource-group/new$'));
        cy.getEntityCreateUpdateHeading('ResourceGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceGroupPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/resource-groups',
          body: resourceGroupSample,
        }).then(({ body }) => {
          resourceGroup = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/resource-groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/resource-groups?page=0&size=20>; rel="last",<http://localhost/api/resource-groups?page=0&size=20>; rel="first"',
              },
              body: [resourceGroup],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(resourceGroupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ResourceGroup page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('resourceGroup');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceGroupPageUrlPattern);
      });

      it('edit button click should load edit ResourceGroup page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ResourceGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceGroupPageUrlPattern);
      });

      it('edit button click should load edit ResourceGroup page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ResourceGroup');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceGroupPageUrlPattern);
      });

      it('last delete button click should delete instance of ResourceGroup', () => {
        cy.intercept('GET', '/api/resource-groups/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('resourceGroup').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceGroupPageUrlPattern);

        resourceGroup = undefined;
      });
    });
  });

  describe('new ResourceGroup page', () => {
    beforeEach(() => {
      cy.visit(`${resourceGroupPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ResourceGroup');
    });

    it('should create an instance of ResourceGroup', () => {
      cy.get(`[data-cy="name"]`).type('eek');
      cy.get(`[data-cy="name"]`).should('have.value', 'eek');

      cy.get(`[data-cy="description"]`).type('hear costly whether');
      cy.get(`[data-cy="description"]`).should('have.value', 'hear costly whether');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        resourceGroup = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', resourceGroupPageUrlPattern);
    });
  });
});
