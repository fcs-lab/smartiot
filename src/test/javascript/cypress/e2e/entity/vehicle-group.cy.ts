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

describe('VehicleGroup e2e test', () => {
  const vehicleGroupPageUrl = '/vehicle-group';
  const vehicleGroupPageUrlPattern = new RegExp('/vehicle-group(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleGroupSample = { groupName: 'consequently drat enlist' };

  let vehicleGroup;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-groups').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-groups/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-groups/${vehicleGroup.id}`,
      }).then(() => {
        vehicleGroup = undefined;
      });
    }
  });

  it('VehicleGroups menu should load VehicleGroups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleGroup').should('exist');
    cy.url().should('match', vehicleGroupPageUrlPattern);
  });

  describe('VehicleGroup page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleGroupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleGroup page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-group/new$'));
        cy.getEntityCreateUpdateHeading('VehicleGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleGroupPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-groups',
          body: vehicleGroupSample,
        }).then(({ body }) => {
          vehicleGroup = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vehicle-groups?page=0&size=20>; rel="last",<http://localhost/api/vehicle-groups?page=0&size=20>; rel="first"',
              },
              body: [vehicleGroup],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleGroupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleGroup page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleGroup');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleGroupPageUrlPattern);
      });

      it('edit button click should load edit VehicleGroup page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleGroupPageUrlPattern);
      });

      it('edit button click should load edit VehicleGroup page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleGroup');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleGroupPageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleGroup', () => {
        cy.intercept('GET', '/api/vehicle-groups/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleGroup').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleGroupPageUrlPattern);

        vehicleGroup = undefined;
      });
    });
  });

  describe('new VehicleGroup page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleGroupPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleGroup');
    });

    it('should create an instance of VehicleGroup', () => {
      cy.get(`[data-cy="groupName"]`).type('camera pace best');
      cy.get(`[data-cy="groupName"]`).should('have.value', 'camera pace best');

      cy.get(`[data-cy="groupDescription"]`).type('quarter');
      cy.get(`[data-cy="groupDescription"]`).should('have.value', 'quarter');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vehicleGroup = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleGroupPageUrlPattern);
    });
  });
});
