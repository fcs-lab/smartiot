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

describe('DeviceCommand e2e test', () => {
  const deviceCommandPageUrl = '/device-command';
  const deviceCommandPageUrlPattern = new RegExp('/device-command(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const deviceCommandSample = { commandType: 'although wearily absent', sentAt: '2024-06-03T08:42:49.040Z', commandStatus: 'EXECUTED' };

  let deviceCommand;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/device-commands+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/device-commands').as('postEntityRequest');
    cy.intercept('DELETE', '/api/device-commands/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (deviceCommand) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/device-commands/${deviceCommand.id}`,
      }).then(() => {
        deviceCommand = undefined;
      });
    }
  });

  it('DeviceCommands menu should load DeviceCommands page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('device-command');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DeviceCommand').should('exist');
    cy.url().should('match', deviceCommandPageUrlPattern);
  });

  describe('DeviceCommand page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(deviceCommandPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DeviceCommand page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/device-command/new$'));
        cy.getEntityCreateUpdateHeading('DeviceCommand');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', deviceCommandPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/device-commands',
          body: deviceCommandSample,
        }).then(({ body }) => {
          deviceCommand = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/device-commands+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/device-commands?page=0&size=20>; rel="last",<http://localhost/api/device-commands?page=0&size=20>; rel="first"',
              },
              body: [deviceCommand],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(deviceCommandPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DeviceCommand page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('deviceCommand');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', deviceCommandPageUrlPattern);
      });

      it('edit button click should load edit DeviceCommand page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DeviceCommand');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', deviceCommandPageUrlPattern);
      });

      it('edit button click should load edit DeviceCommand page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DeviceCommand');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', deviceCommandPageUrlPattern);
      });

      it('last delete button click should delete instance of DeviceCommand', () => {
        cy.intercept('GET', '/api/device-commands/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('deviceCommand').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', deviceCommandPageUrlPattern);

        deviceCommand = undefined;
      });
    });
  });

  describe('new DeviceCommand page', () => {
    beforeEach(() => {
      cy.visit(`${deviceCommandPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DeviceCommand');
    });

    it('should create an instance of DeviceCommand', () => {
      cy.get(`[data-cy="commandType"]`).type('gadzooks restfully');
      cy.get(`[data-cy="commandType"]`).should('have.value', 'gadzooks restfully');

      cy.get(`[data-cy="sentAt"]`).type('2024-06-03T13:56');
      cy.get(`[data-cy="sentAt"]`).blur();
      cy.get(`[data-cy="sentAt"]`).should('have.value', '2024-06-03T13:56');

      cy.get(`[data-cy="executedAt"]`).type('2024-06-03T03:03');
      cy.get(`[data-cy="executedAt"]`).blur();
      cy.get(`[data-cy="executedAt"]`).should('have.value', '2024-06-03T03:03');

      cy.get(`[data-cy="commandStatus"]`).select('FAILED');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        deviceCommand = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', deviceCommandPageUrlPattern);
    });
  });
});
