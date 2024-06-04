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

describe('VehicleManufacturer e2e test', () => {
  const vehicleManufacturerPageUrl = '/vehicle-manufacturer';
  const vehicleManufacturerPageUrlPattern = new RegExp('/vehicle-manufacturer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleManufacturerSample = { manufacturerName: 'generalise meanwhile mathematics', manufacturerCountry: 'asymmetry' };

  let vehicleManufacturer;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-manufacturers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-manufacturers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-manufacturers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleManufacturer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-manufacturers/${vehicleManufacturer.id}`,
      }).then(() => {
        vehicleManufacturer = undefined;
      });
    }
  });

  it('VehicleManufacturers menu should load VehicleManufacturers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-manufacturer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleManufacturer').should('exist');
    cy.url().should('match', vehicleManufacturerPageUrlPattern);
  });

  describe('VehicleManufacturer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleManufacturerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleManufacturer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-manufacturer/new$'));
        cy.getEntityCreateUpdateHeading('VehicleManufacturer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleManufacturerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-manufacturers',
          body: vehicleManufacturerSample,
        }).then(({ body }) => {
          vehicleManufacturer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-manufacturers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vehicle-manufacturers?page=0&size=20>; rel="last",<http://localhost/api/vehicle-manufacturers?page=0&size=20>; rel="first"',
              },
              body: [vehicleManufacturer],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleManufacturerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleManufacturer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleManufacturer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleManufacturerPageUrlPattern);
      });

      it('edit button click should load edit VehicleManufacturer page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleManufacturer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleManufacturerPageUrlPattern);
      });

      it('edit button click should load edit VehicleManufacturer page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleManufacturer');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleManufacturerPageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleManufacturer', () => {
        cy.intercept('GET', '/api/vehicle-manufacturers/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleManufacturer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleManufacturerPageUrlPattern);

        vehicleManufacturer = undefined;
      });
    });
  });

  describe('new VehicleManufacturer page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleManufacturerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleManufacturer');
    });

    it('should create an instance of VehicleManufacturer', () => {
      cy.get(`[data-cy="manufacturerName"]`).type('abandoned whenever round');
      cy.get(`[data-cy="manufacturerName"]`).should('have.value', 'abandoned whenever round');

      cy.get(`[data-cy="manufacturerCountry"]`).type('careful furthermore');
      cy.get(`[data-cy="manufacturerCountry"]`).should('have.value', 'careful furthermore');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vehicleManufacturer = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleManufacturerPageUrlPattern);
    });
  });
});
