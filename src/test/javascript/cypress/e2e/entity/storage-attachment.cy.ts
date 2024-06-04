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

describe('StorageAttachment e2e test', () => {
  const storageAttachmentPageUrl = '/storage-attachment';
  const storageAttachmentPageUrlPattern = new RegExp('/storage-attachment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const storageAttachmentSample = {
    attachmentName: 'meanwhile',
    recordType: 'confused normal',
    recordId: 18227,
    createdAt: '2024-06-04T00:43:15.146Z',
    updatedAt: '2024-06-03T20:57:04.726Z',
    blobId: 14120,
  };

  let storageAttachment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/storage-attachments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/storage-attachments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/storage-attachments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (storageAttachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/storage-attachments/${storageAttachment.id}`,
      }).then(() => {
        storageAttachment = undefined;
      });
    }
  });

  it('StorageAttachments menu should load StorageAttachments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('storage-attachment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StorageAttachment').should('exist');
    cy.url().should('match', storageAttachmentPageUrlPattern);
  });

  describe('StorageAttachment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(storageAttachmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StorageAttachment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/storage-attachment/new$'));
        cy.getEntityCreateUpdateHeading('StorageAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageAttachmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/storage-attachments',
          body: storageAttachmentSample,
        }).then(({ body }) => {
          storageAttachment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/storage-attachments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/storage-attachments?page=0&size=20>; rel="last",<http://localhost/api/storage-attachments?page=0&size=20>; rel="first"',
              },
              body: [storageAttachment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(storageAttachmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StorageAttachment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('storageAttachment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageAttachmentPageUrlPattern);
      });

      it('edit button click should load edit StorageAttachment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StorageAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageAttachmentPageUrlPattern);
      });

      it('edit button click should load edit StorageAttachment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StorageAttachment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageAttachmentPageUrlPattern);
      });

      it('last delete button click should delete instance of StorageAttachment', () => {
        cy.intercept('GET', '/api/storage-attachments/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('storageAttachment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageAttachmentPageUrlPattern);

        storageAttachment = undefined;
      });
    });
  });

  describe('new StorageAttachment page', () => {
    beforeEach(() => {
      cy.visit(`${storageAttachmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StorageAttachment');
    });

    it('should create an instance of StorageAttachment', () => {
      cy.get(`[data-cy="attachmentName"]`).type('actual metallic');
      cy.get(`[data-cy="attachmentName"]`).should('have.value', 'actual metallic');

      cy.get(`[data-cy="recordType"]`).type('brr sharply');
      cy.get(`[data-cy="recordType"]`).should('have.value', 'brr sharply');

      cy.get(`[data-cy="recordId"]`).type('3654');
      cy.get(`[data-cy="recordId"]`).should('have.value', '3654');

      cy.get(`[data-cy="createdAt"]`).type('2024-06-03T17:01');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-06-03T17:01');

      cy.get(`[data-cy="updatedAt"]`).type('2024-06-03T21:47');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-06-03T21:47');

      cy.get(`[data-cy="lastModifiedBy"]`).type('behind');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'behind');

      cy.get(`[data-cy="blobId"]`).type('25775');
      cy.get(`[data-cy="blobId"]`).should('have.value', '25775');

      cy.get(`[data-cy="deletedAt"]`).type('2024-06-03T05:57');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-06-03T05:57');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        storageAttachment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', storageAttachmentPageUrlPattern);
    });
  });
});
