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

describe('VehicleInfo e2e test', () => {
  const vehicleInfoPageUrl = '/vehicle-info';
  const vehicleInfoPageUrlPattern = new RegExp('/vehicle-info(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleInfoSample = { modelName: 'for tinted', licensePlate: 'community ', vehicleStatus: 'AVAILABLE' };

  let vehicleInfo;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-infos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-infos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-infos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleInfo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-infos/${vehicleInfo.id}`,
      }).then(() => {
        vehicleInfo = undefined;
      });
    }
  });

  it('VehicleInfos menu should load VehicleInfos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-info');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleInfo').should('exist');
    cy.url().should('match', vehicleInfoPageUrlPattern);
  });

  describe('VehicleInfo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleInfoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleInfo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-info/new$'));
        cy.getEntityCreateUpdateHeading('VehicleInfo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleInfoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-infos',
          body: vehicleInfoSample,
        }).then(({ body }) => {
          vehicleInfo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-infos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vehicle-infos?page=0&size=20>; rel="last",<http://localhost/api/vehicle-infos?page=0&size=20>; rel="first"',
              },
              body: [vehicleInfo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleInfoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleInfo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleInfo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleInfoPageUrlPattern);
      });

      it('edit button click should load edit VehicleInfo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleInfo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleInfoPageUrlPattern);
      });

      it('edit button click should load edit VehicleInfo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleInfo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleInfoPageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleInfo', () => {
        cy.intercept('GET', '/api/vehicle-infos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleInfo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleInfoPageUrlPattern);

        vehicleInfo = undefined;
      });
    });
  });

  describe('new VehicleInfo page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleInfoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleInfo');
    });

    it('should create an instance of VehicleInfo', () => {
      cy.get(`[data-cy="modelName"]`).type('inwardly');
      cy.get(`[data-cy="modelName"]`).should('have.value', 'inwardly');

      cy.get(`[data-cy="licensePlate"]`).type('costume ob');
      cy.get(`[data-cy="licensePlate"]`).should('have.value', 'costume ob');

      cy.get(`[data-cy="vehicleStatus"]`).select('MAINTENANCE');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vehicleInfo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleInfoPageUrlPattern);
    });
  });
});
