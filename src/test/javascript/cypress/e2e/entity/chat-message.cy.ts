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

describe('ChatMessage e2e test', () => {
  const chatMessagePageUrl = '/chat-message';
  const chatMessagePageUrlPattern = new RegExp('/chat-message(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const chatMessageSample = {
    messageId: 'powerless another monopoly',
    messageContent: 'legalize gee gadzooks',
    messageTimestamp: '2024-06-03T23:46:55.347Z',
  };

  let chatMessage;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/chat-messages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/chat-messages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/chat-messages/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (chatMessage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/chat-messages/${chatMessage.id}`,
      }).then(() => {
        chatMessage = undefined;
      });
    }
  });

  it('ChatMessages menu should load ChatMessages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat-message');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChatMessage').should('exist');
    cy.url().should('match', chatMessagePageUrlPattern);
  });

  describe('ChatMessage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(chatMessagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChatMessage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/chat-message/new$'));
        cy.getEntityCreateUpdateHeading('ChatMessage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatMessagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/chat-messages',
          body: chatMessageSample,
        }).then(({ body }) => {
          chatMessage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/chat-messages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/chat-messages?page=0&size=20>; rel="last",<http://localhost/api/chat-messages?page=0&size=20>; rel="first"',
              },
              body: [chatMessage],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(chatMessagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ChatMessage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('chatMessage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatMessagePageUrlPattern);
      });

      it('edit button click should load edit ChatMessage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatMessage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatMessagePageUrlPattern);
      });

      it('edit button click should load edit ChatMessage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatMessage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatMessagePageUrlPattern);
      });

      it('last delete button click should delete instance of ChatMessage', () => {
        cy.intercept('GET', '/api/chat-messages/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('chatMessage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatMessagePageUrlPattern);

        chatMessage = undefined;
      });
    });
  });

  describe('new ChatMessage page', () => {
    beforeEach(() => {
      cy.visit(`${chatMessagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ChatMessage');
    });

    it('should create an instance of ChatMessage', () => {
      cy.get(`[data-cy="messageId"]`).type('sans project');
      cy.get(`[data-cy="messageId"]`).should('have.value', 'sans project');

      cy.get(`[data-cy="messageContent"]`).type('lightly convert hurry');
      cy.get(`[data-cy="messageContent"]`).should('have.value', 'lightly convert hurry');

      cy.get(`[data-cy="messageTimestamp"]`).type('2024-06-03T22:53');
      cy.get(`[data-cy="messageTimestamp"]`).blur();
      cy.get(`[data-cy="messageTimestamp"]`).should('have.value', '2024-06-03T22:53');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        chatMessage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', chatMessagePageUrlPattern);
    });
  });
});
