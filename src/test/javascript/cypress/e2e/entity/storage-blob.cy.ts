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

describe('StorageBlob e2e test', () => {
  const storageBlobPageUrl = '/storage-blob';
  const storageBlobPageUrlPattern = new RegExp('/storage-blob(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const storageBlobSample = {
    fileName: 'autowind yuck gah',
    byteSize: 27736,
    checksum: 'yearly',
    createdAt: '2024-06-03T19:01:15.961Z',
    updatedAt: '2024-06-03T08:32:04.330Z',
    key: 'eve interconnect always',
  };

  let storageBlob;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/storage-blobs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/storage-blobs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/storage-blobs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (storageBlob) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/storage-blobs/${storageBlob.id}`,
      }).then(() => {
        storageBlob = undefined;
      });
    }
  });

  it('StorageBlobs menu should load StorageBlobs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('storage-blob');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StorageBlob').should('exist');
    cy.url().should('match', storageBlobPageUrlPattern);
  });

  describe('StorageBlob page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(storageBlobPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StorageBlob page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/storage-blob/new$'));
        cy.getEntityCreateUpdateHeading('StorageBlob');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageBlobPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/storage-blobs',
          body: storageBlobSample,
        }).then(({ body }) => {
          storageBlob = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/storage-blobs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/storage-blobs?page=0&size=20>; rel="last",<http://localhost/api/storage-blobs?page=0&size=20>; rel="first"',
              },
              body: [storageBlob],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(storageBlobPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StorageBlob page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('storageBlob');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageBlobPageUrlPattern);
      });

      it('edit button click should load edit StorageBlob page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StorageBlob');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageBlobPageUrlPattern);
      });

      it('edit button click should load edit StorageBlob page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StorageBlob');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageBlobPageUrlPattern);
      });

      it('last delete button click should delete instance of StorageBlob', () => {
        cy.intercept('GET', '/api/storage-blobs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('storageBlob').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageBlobPageUrlPattern);

        storageBlob = undefined;
      });
    });
  });

  describe('new StorageBlob page', () => {
    beforeEach(() => {
      cy.visit(`${storageBlobPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StorageBlob');
    });

    it('should create an instance of StorageBlob', () => {
      cy.get(`[data-cy="fileName"]`).type('impolite whose whoa');
      cy.get(`[data-cy="fileName"]`).should('have.value', 'impolite whose whoa');

      cy.get(`[data-cy="contentType"]`).type('whisker');
      cy.get(`[data-cy="contentType"]`).should('have.value', 'whisker');

      cy.get(`[data-cy="byteSize"]`).type('20193');
      cy.get(`[data-cy="byteSize"]`).should('have.value', '20193');

      cy.get(`[data-cy="checksum"]`).type('gee till');
      cy.get(`[data-cy="checksum"]`).should('have.value', 'gee till');

      cy.get(`[data-cy="createdAt"]`).type('2024-06-03T09:58');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-06-03T09:58');

      cy.get(`[data-cy="updatedAt"]`).type('2024-06-03T10:40');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-06-03T10:40');

      cy.get(`[data-cy="lastModifiedBy"]`).type('sherry');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'sherry');

      cy.get(`[data-cy="key"]`).type('which');
      cy.get(`[data-cy="key"]`).should('have.value', 'which');

      cy.get(`[data-cy="metadata"]`).type('outrageous');
      cy.get(`[data-cy="metadata"]`).should('have.value', 'outrageous');

      cy.get(`[data-cy="deletedAt"]`).type('2024-06-03T15:56');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-06-03T15:56');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        storageBlob = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', storageBlobPageUrlPattern);
    });
  });
});
