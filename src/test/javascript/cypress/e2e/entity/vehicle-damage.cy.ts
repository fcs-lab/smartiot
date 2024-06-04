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

describe('VehicleDamage e2e test', () => {
  const vehicleDamagePageUrl = '/vehicle-damage';
  const vehicleDamagePageUrlPattern = new RegExp('/vehicle-damage(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleDamageSample = {
    damageDescription: 'press vivaciously with',
    reportedAt: '2024-06-03T23:32:07.542Z',
    damageStatus: 'REPORTED',
  };

  let vehicleDamage;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-damages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-damages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-damages/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleDamage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-damages/${vehicleDamage.id}`,
      }).then(() => {
        vehicleDamage = undefined;
      });
    }
  });

  it('VehicleDamages menu should load VehicleDamages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-damage');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleDamage').should('exist');
    cy.url().should('match', vehicleDamagePageUrlPattern);
  });

  describe('VehicleDamage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleDamagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleDamage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-damage/new$'));
        cy.getEntityCreateUpdateHeading('VehicleDamage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleDamagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-damages',
          body: vehicleDamageSample,
        }).then(({ body }) => {
          vehicleDamage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-damages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vehicle-damages?page=0&size=20>; rel="last",<http://localhost/api/vehicle-damages?page=0&size=20>; rel="first"',
              },
              body: [vehicleDamage],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleDamagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleDamage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleDamage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleDamagePageUrlPattern);
      });

      it('edit button click should load edit VehicleDamage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleDamage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleDamagePageUrlPattern);
      });

      it('edit button click should load edit VehicleDamage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleDamage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleDamagePageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleDamage', () => {
        cy.intercept('GET', '/api/vehicle-damages/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleDamage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleDamagePageUrlPattern);

        vehicleDamage = undefined;
      });
    });
  });

  describe('new VehicleDamage page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleDamagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleDamage');
    });

    it('should create an instance of VehicleDamage', () => {
      cy.get(`[data-cy="damageDescription"]`).type('reflate behind');
      cy.get(`[data-cy="damageDescription"]`).should('have.value', 'reflate behind');

      cy.get(`[data-cy="reportedAt"]`).type('2024-06-03T21:34');
      cy.get(`[data-cy="reportedAt"]`).blur();
      cy.get(`[data-cy="reportedAt"]`).should('have.value', '2024-06-03T21:34');

      cy.get(`[data-cy="damageStatus"]`).select('IN_PROGRESS');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vehicleDamage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleDamagePageUrlPattern);
    });
  });
});
