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

describe('CarRide e2e test', () => {
  const carRidePageUrl = '/car-ride';
  const carRidePageUrlPattern = new RegExp('/car-ride(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const carRideSample = {
    startTime: '2024-06-03T17:18:37.589Z',
    endTime: '2024-06-03T23:37:18.338Z',
    origin: 'forenenst',
    destination: 'pish what',
    availableSeats: 3994,
  };

  let carRide;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/car-rides+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/car-rides').as('postEntityRequest');
    cy.intercept('DELETE', '/api/car-rides/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (carRide) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/car-rides/${carRide.id}`,
      }).then(() => {
        carRide = undefined;
      });
    }
  });

  it('CarRides menu should load CarRides page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('car-ride');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CarRide').should('exist');
    cy.url().should('match', carRidePageUrlPattern);
  });

  describe('CarRide page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(carRidePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CarRide page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/car-ride/new$'));
        cy.getEntityCreateUpdateHeading('CarRide');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', carRidePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/car-rides',
          body: carRideSample,
        }).then(({ body }) => {
          carRide = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/car-rides+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/car-rides?page=0&size=20>; rel="last",<http://localhost/api/car-rides?page=0&size=20>; rel="first"',
              },
              body: [carRide],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(carRidePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CarRide page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('carRide');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', carRidePageUrlPattern);
      });

      it('edit button click should load edit CarRide page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarRide');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', carRidePageUrlPattern);
      });

      it('edit button click should load edit CarRide page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarRide');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', carRidePageUrlPattern);
      });

      it('last delete button click should delete instance of CarRide', () => {
        cy.intercept('GET', '/api/car-rides/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('carRide').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', carRidePageUrlPattern);

        carRide = undefined;
      });
    });
  });

  describe('new CarRide page', () => {
    beforeEach(() => {
      cy.visit(`${carRidePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CarRide');
    });

    it('should create an instance of CarRide', () => {
      cy.get(`[data-cy="startTime"]`).type('2024-06-03T22:11');
      cy.get(`[data-cy="startTime"]`).blur();
      cy.get(`[data-cy="startTime"]`).should('have.value', '2024-06-03T22:11');

      cy.get(`[data-cy="endTime"]`).type('2024-06-03T17:25');
      cy.get(`[data-cy="endTime"]`).blur();
      cy.get(`[data-cy="endTime"]`).should('have.value', '2024-06-03T17:25');

      cy.get(`[data-cy="origin"]`).type('minor desensitize');
      cy.get(`[data-cy="origin"]`).should('have.value', 'minor desensitize');

      cy.get(`[data-cy="destination"]`).type('alongside');
      cy.get(`[data-cy="destination"]`).should('have.value', 'alongside');

      cy.get(`[data-cy="availableSeats"]`).type('32597');
      cy.get(`[data-cy="availableSeats"]`).should('have.value', '32597');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        carRide = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', carRidePageUrlPattern);
    });
  });
});
