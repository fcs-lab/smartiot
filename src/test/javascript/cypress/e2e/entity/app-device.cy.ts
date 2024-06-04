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

describe('AppDevice e2e test', () => {
  const appDevicePageUrl = '/app-device';
  const appDevicePageUrlPattern = new RegExp('/app-device(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const appDeviceSample = { deviceId: 'whose contrast disastrous', deviceType: 'OTHER' };

  let appDevice;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/app-devices+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/app-devices').as('postEntityRequest');
    cy.intercept('DELETE', '/api/app-devices/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (appDevice) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/app-devices/${appDevice.id}`,
      }).then(() => {
        appDevice = undefined;
      });
    }
  });

  it('AppDevices menu should load AppDevices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('app-device');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AppDevice').should('exist');
    cy.url().should('match', appDevicePageUrlPattern);
  });

  describe('AppDevice page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(appDevicePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AppDevice page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/app-device/new$'));
        cy.getEntityCreateUpdateHeading('AppDevice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', appDevicePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/app-devices',
          body: appDeviceSample,
        }).then(({ body }) => {
          appDevice = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/app-devices+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/app-devices?page=0&size=20>; rel="last",<http://localhost/api/app-devices?page=0&size=20>; rel="first"',
              },
              body: [appDevice],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(appDevicePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AppDevice page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('appDevice');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', appDevicePageUrlPattern);
      });

      it('edit button click should load edit AppDevice page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AppDevice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', appDevicePageUrlPattern);
      });

      it('edit button click should load edit AppDevice page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AppDevice');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', appDevicePageUrlPattern);
      });

      it('last delete button click should delete instance of AppDevice', () => {
        cy.intercept('GET', '/api/app-devices/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('appDevice').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', appDevicePageUrlPattern);

        appDevice = undefined;
      });
    });
  });

  describe('new AppDevice page', () => {
    beforeEach(() => {
      cy.visit(`${appDevicePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AppDevice');
    });

    it('should create an instance of AppDevice', () => {
      cy.get(`[data-cy="deviceId"]`).type('ajar');
      cy.get(`[data-cy="deviceId"]`).should('have.value', 'ajar');

      cy.get(`[data-cy="deviceType"]`).select('GPS');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        appDevice = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', appDevicePageUrlPattern);
    });
  });
});
