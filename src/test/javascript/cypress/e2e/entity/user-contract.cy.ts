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

describe('UserContract e2e test', () => {
  const userContractPageUrl = '/user-contract';
  const userContractPageUrlPattern = new RegExp('/user-contract(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userContractSample = {
    contractName: 'once amidst film',
    startDate: '2024-06-03T17:44:57.441Z',
    endDate: '2024-06-03T23:06:23.193Z',
  };

  let userContract;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-contracts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-contracts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-contracts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userContract) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-contracts/${userContract.id}`,
      }).then(() => {
        userContract = undefined;
      });
    }
  });

  it('UserContracts menu should load UserContracts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-contract');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserContract').should('exist');
    cy.url().should('match', userContractPageUrlPattern);
  });

  describe('UserContract page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userContractPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserContract page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-contract/new$'));
        cy.getEntityCreateUpdateHeading('UserContract');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userContractPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-contracts',
          body: userContractSample,
        }).then(({ body }) => {
          userContract = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-contracts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-contracts?page=0&size=20>; rel="last",<http://localhost/api/user-contracts?page=0&size=20>; rel="first"',
              },
              body: [userContract],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userContractPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserContract page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userContract');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userContractPageUrlPattern);
      });

      it('edit button click should load edit UserContract page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserContract');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userContractPageUrlPattern);
      });

      it('edit button click should load edit UserContract page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserContract');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userContractPageUrlPattern);
      });

      it('last delete button click should delete instance of UserContract', () => {
        cy.intercept('GET', '/api/user-contracts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userContract').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userContractPageUrlPattern);

        userContract = undefined;
      });
    });
  });

  describe('new UserContract page', () => {
    beforeEach(() => {
      cy.visit(`${userContractPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserContract');
    });

    it('should create an instance of UserContract', () => {
      cy.get(`[data-cy="contractName"]`).type('round shoo');
      cy.get(`[data-cy="contractName"]`).should('have.value', 'round shoo');

      cy.get(`[data-cy="startDate"]`).type('2024-06-03T12:19');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-06-03T12:19');

      cy.get(`[data-cy="endDate"]`).type('2024-06-03T21:46');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-06-03T21:46');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userContract = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userContractPageUrlPattern);
    });
  });
});
