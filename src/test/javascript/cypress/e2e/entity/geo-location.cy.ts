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

describe('GeoLocation e2e test', () => {
  const geoLocationPageUrl = '/geo-location';
  const geoLocationPageUrlPattern = new RegExp('/geo-location(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const geoLocationSample = { latitude: 24640.98, longitude: 8202.65 };

  let geoLocation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/geo-locations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/geo-locations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/geo-locations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (geoLocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/geo-locations/${geoLocation.id}`,
      }).then(() => {
        geoLocation = undefined;
      });
    }
  });

  it('GeoLocations menu should load GeoLocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('geo-location');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GeoLocation').should('exist');
    cy.url().should('match', geoLocationPageUrlPattern);
  });

  describe('GeoLocation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(geoLocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GeoLocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/geo-location/new$'));
        cy.getEntityCreateUpdateHeading('GeoLocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', geoLocationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/geo-locations',
          body: geoLocationSample,
        }).then(({ body }) => {
          geoLocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/geo-locations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/geo-locations?page=0&size=20>; rel="last",<http://localhost/api/geo-locations?page=0&size=20>; rel="first"',
              },
              body: [geoLocation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(geoLocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GeoLocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('geoLocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', geoLocationPageUrlPattern);
      });

      it('edit button click should load edit GeoLocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GeoLocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', geoLocationPageUrlPattern);
      });

      it('edit button click should load edit GeoLocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GeoLocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', geoLocationPageUrlPattern);
      });

      it('last delete button click should delete instance of GeoLocation', () => {
        cy.intercept('GET', '/api/geo-locations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('geoLocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', geoLocationPageUrlPattern);

        geoLocation = undefined;
      });
    });
  });

  describe('new GeoLocation page', () => {
    beforeEach(() => {
      cy.visit(`${geoLocationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('GeoLocation');
    });

    it('should create an instance of GeoLocation', () => {
      cy.get(`[data-cy="latitude"]`).type('84.06');
      cy.get(`[data-cy="latitude"]`).should('have.value', '84.06');

      cy.get(`[data-cy="longitude"]`).type('13435.07');
      cy.get(`[data-cy="longitude"]`).should('have.value', '13435.07');

      cy.get(`[data-cy="fullAddress"]`).type('imply hopeful');
      cy.get(`[data-cy="fullAddress"]`).should('have.value', 'imply hopeful');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        geoLocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', geoLocationPageUrlPattern);
    });
  });
});
