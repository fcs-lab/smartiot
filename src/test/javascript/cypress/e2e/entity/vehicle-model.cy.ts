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

describe('VehicleModel e2e test', () => {
  const vehicleModelPageUrl = '/vehicle-model';
  const vehicleModelPageUrlPattern = new RegExp('/vehicle-model(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleModelSample = { modelName: 'or glass' };

  let vehicleModel;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-models+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-models').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-models/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleModel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-models/${vehicleModel.id}`,
      }).then(() => {
        vehicleModel = undefined;
      });
    }
  });

  it('VehicleModels menu should load VehicleModels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-model');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleModel').should('exist');
    cy.url().should('match', vehicleModelPageUrlPattern);
  });

  describe('VehicleModel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleModelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleModel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-model/new$'));
        cy.getEntityCreateUpdateHeading('VehicleModel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleModelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-models',
          body: vehicleModelSample,
        }).then(({ body }) => {
          vehicleModel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-models+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vehicle-models?page=0&size=20>; rel="last",<http://localhost/api/vehicle-models?page=0&size=20>; rel="first"',
              },
              body: [vehicleModel],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleModelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleModel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleModel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleModelPageUrlPattern);
      });

      it('edit button click should load edit VehicleModel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleModel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleModelPageUrlPattern);
      });

      it('edit button click should load edit VehicleModel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleModel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleModelPageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleModel', () => {
        cy.intercept('GET', '/api/vehicle-models/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleModel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleModelPageUrlPattern);

        vehicleModel = undefined;
      });
    });
  });

  describe('new VehicleModel page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleModelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleModel');
    });

    it('should create an instance of VehicleModel', () => {
      cy.get(`[data-cy="modelName"]`).type('spider unabashedly whenever');
      cy.get(`[data-cy="modelName"]`).should('have.value', 'spider unabashedly whenever');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vehicleModel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleModelPageUrlPattern);
    });
  });
});
