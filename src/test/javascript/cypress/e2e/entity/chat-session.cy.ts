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

describe('ChatSession e2e test', () => {
  const chatSessionPageUrl = '/chat-session';
  const chatSessionPageUrlPattern = new RegExp('/chat-session(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const chatSessionSample = { sessionId: 'label oil', startTime: '2024-06-03T05:46:31.736Z' };

  let chatSession;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/chat-sessions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/chat-sessions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/chat-sessions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (chatSession) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/chat-sessions/${chatSession.id}`,
      }).then(() => {
        chatSession = undefined;
      });
    }
  });

  it('ChatSessions menu should load ChatSessions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat-session');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChatSession').should('exist');
    cy.url().should('match', chatSessionPageUrlPattern);
  });

  describe('ChatSession page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(chatSessionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChatSession page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/chat-session/new$'));
        cy.getEntityCreateUpdateHeading('ChatSession');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatSessionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/chat-sessions',
          body: chatSessionSample,
        }).then(({ body }) => {
          chatSession = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/chat-sessions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/chat-sessions?page=0&size=20>; rel="last",<http://localhost/api/chat-sessions?page=0&size=20>; rel="first"',
              },
              body: [chatSession],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(chatSessionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ChatSession page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('chatSession');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatSessionPageUrlPattern);
      });

      it('edit button click should load edit ChatSession page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatSession');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatSessionPageUrlPattern);
      });

      it('edit button click should load edit ChatSession page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatSession');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatSessionPageUrlPattern);
      });

      it('last delete button click should delete instance of ChatSession', () => {
        cy.intercept('GET', '/api/chat-sessions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('chatSession').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatSessionPageUrlPattern);

        chatSession = undefined;
      });
    });
  });

  describe('new ChatSession page', () => {
    beforeEach(() => {
      cy.visit(`${chatSessionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ChatSession');
    });

    it('should create an instance of ChatSession', () => {
      cy.get(`[data-cy="sessionId"]`).type('provided quirkily meanwhile');
      cy.get(`[data-cy="sessionId"]`).should('have.value', 'provided quirkily meanwhile');

      cy.get(`[data-cy="startTime"]`).type('2024-06-03T15:11');
      cy.get(`[data-cy="startTime"]`).blur();
      cy.get(`[data-cy="startTime"]`).should('have.value', '2024-06-03T15:11');

      cy.get(`[data-cy="endTime"]`).type('2024-06-04T00:24');
      cy.get(`[data-cy="endTime"]`).blur();
      cy.get(`[data-cy="endTime"]`).should('have.value', '2024-06-04T00:24');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        chatSession = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', chatSessionPageUrlPattern);
    });
  });
});
