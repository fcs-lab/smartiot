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

describe('CostCenter e2e test', () => {
  const costCenterPageUrl = '/cost-center';
  const costCenterPageUrlPattern = new RegExp('/cost-center(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const costCenterSample = { centerName: 'anti usually', budgetAmount: 10713.83 };

  let costCenter;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cost-centers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cost-centers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cost-centers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (costCenter) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cost-centers/${costCenter.id}`,
      }).then(() => {
        costCenter = undefined;
      });
    }
  });

  it('CostCenters menu should load CostCenters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cost-center');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CostCenter').should('exist');
    cy.url().should('match', costCenterPageUrlPattern);
  });

  describe('CostCenter page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(costCenterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CostCenter page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cost-center/new$'));
        cy.getEntityCreateUpdateHeading('CostCenter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', costCenterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cost-centers',
          body: costCenterSample,
        }).then(({ body }) => {
          costCenter = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cost-centers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cost-centers?page=0&size=20>; rel="last",<http://localhost/api/cost-centers?page=0&size=20>; rel="first"',
              },
              body: [costCenter],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(costCenterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CostCenter page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('costCenter');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', costCenterPageUrlPattern);
      });

      it('edit button click should load edit CostCenter page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CostCenter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', costCenterPageUrlPattern);
      });

      it('edit button click should load edit CostCenter page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CostCenter');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', costCenterPageUrlPattern);
      });

      it('last delete button click should delete instance of CostCenter', () => {
        cy.intercept('GET', '/api/cost-centers/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('costCenter').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', costCenterPageUrlPattern);

        costCenter = undefined;
      });
    });
  });

  describe('new CostCenter page', () => {
    beforeEach(() => {
      cy.visit(`${costCenterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CostCenter');
    });

    it('should create an instance of CostCenter', () => {
      cy.get(`[data-cy="centerName"]`).type('awkwardly calcification');
      cy.get(`[data-cy="centerName"]`).should('have.value', 'awkwardly calcification');

      cy.get(`[data-cy="budgetAmount"]`).type('17854.25');
      cy.get(`[data-cy="budgetAmount"]`).should('have.value', '17854.25');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        costCenter = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', costCenterPageUrlPattern);
    });
  });
});
