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

describe('ManualEntry e2e test', () => {
  const manualEntryPageUrl = '/manual-entry';
  const manualEntryPageUrlPattern = new RegExp('/manual-entry(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const manualEntrySample = { entryType: 'dimly because', value: 'shady viciously', entryDate: '2024-06-04T00:25:19.049Z' };

  let manualEntry;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/manual-entries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/manual-entries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/manual-entries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (manualEntry) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/manual-entries/${manualEntry.id}`,
      }).then(() => {
        manualEntry = undefined;
      });
    }
  });

  it('ManualEntries menu should load ManualEntries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('manual-entry');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ManualEntry').should('exist');
    cy.url().should('match', manualEntryPageUrlPattern);
  });

  describe('ManualEntry page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(manualEntryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ManualEntry page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/manual-entry/new$'));
        cy.getEntityCreateUpdateHeading('ManualEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', manualEntryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/manual-entries',
          body: manualEntrySample,
        }).then(({ body }) => {
          manualEntry = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/manual-entries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/manual-entries?page=0&size=20>; rel="last",<http://localhost/api/manual-entries?page=0&size=20>; rel="first"',
              },
              body: [manualEntry],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(manualEntryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ManualEntry page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('manualEntry');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', manualEntryPageUrlPattern);
      });

      it('edit button click should load edit ManualEntry page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ManualEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', manualEntryPageUrlPattern);
      });

      it('edit button click should load edit ManualEntry page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ManualEntry');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', manualEntryPageUrlPattern);
      });

      it('last delete button click should delete instance of ManualEntry', () => {
        cy.intercept('GET', '/api/manual-entries/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('manualEntry').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', manualEntryPageUrlPattern);

        manualEntry = undefined;
      });
    });
  });

  describe('new ManualEntry page', () => {
    beforeEach(() => {
      cy.visit(`${manualEntryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ManualEntry');
    });

    it('should create an instance of ManualEntry', () => {
      cy.get(`[data-cy="entryType"]`).type('tinge truthfully');
      cy.get(`[data-cy="entryType"]`).should('have.value', 'tinge truthfully');

      cy.get(`[data-cy="value"]`).type('queasy loosely than');
      cy.get(`[data-cy="value"]`).should('have.value', 'queasy loosely than');

      cy.get(`[data-cy="entryDate"]`).type('2024-06-03T04:30');
      cy.get(`[data-cy="entryDate"]`).blur();
      cy.get(`[data-cy="entryDate"]`).should('have.value', '2024-06-03T04:30');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        manualEntry = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', manualEntryPageUrlPattern);
    });
  });
});
