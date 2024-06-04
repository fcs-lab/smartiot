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

describe('ChatUser e2e test', () => {
  const chatUserPageUrl = '/chat-user';
  const chatUserPageUrlPattern = new RegExp('/chat-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const chatUserSample = { userId: 'rubbery', userName: 'pace warmly' };

  let chatUser;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/chat-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/chat-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/chat-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (chatUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/chat-users/${chatUser.id}`,
      }).then(() => {
        chatUser = undefined;
      });
    }
  });

  it('ChatUsers menu should load ChatUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChatUser').should('exist');
    cy.url().should('match', chatUserPageUrlPattern);
  });

  describe('ChatUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(chatUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChatUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/chat-user/new$'));
        cy.getEntityCreateUpdateHeading('ChatUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/chat-users',
          body: chatUserSample,
        }).then(({ body }) => {
          chatUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/chat-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/chat-users?page=0&size=20>; rel="last",<http://localhost/api/chat-users?page=0&size=20>; rel="first"',
              },
              body: [chatUser],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(chatUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ChatUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('chatUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatUserPageUrlPattern);
      });

      it('edit button click should load edit ChatUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatUserPageUrlPattern);
      });

      it('edit button click should load edit ChatUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatUserPageUrlPattern);
      });

      it('last delete button click should delete instance of ChatUser', () => {
        cy.intercept('GET', '/api/chat-users/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('chatUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatUserPageUrlPattern);

        chatUser = undefined;
      });
    });
  });

  describe('new ChatUser page', () => {
    beforeEach(() => {
      cy.visit(`${chatUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ChatUser');
    });

    it('should create an instance of ChatUser', () => {
      cy.get(`[data-cy="userId"]`).type('carefully including that');
      cy.get(`[data-cy="userId"]`).should('have.value', 'carefully including that');

      cy.get(`[data-cy="userName"]`).type('ew since uh-huh');
      cy.get(`[data-cy="userName"]`).should('have.value', 'ew since uh-huh');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        chatUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', chatUserPageUrlPattern);
    });
  });
});
