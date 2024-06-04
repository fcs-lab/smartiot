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

describe('ChatBooking e2e test', () => {
  const chatBookingPageUrl = '/chat-booking';
  const chatBookingPageUrlPattern = new RegExp('/chat-booking(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const chatBookingSample = { bookingTimestamp: '2024-06-03T17:38:16.309Z' };

  let chatBooking;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/chat-bookings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/chat-bookings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/chat-bookings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (chatBooking) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/chat-bookings/${chatBooking.id}`,
      }).then(() => {
        chatBooking = undefined;
      });
    }
  });

  it('ChatBookings menu should load ChatBookings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat-booking');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChatBooking').should('exist');
    cy.url().should('match', chatBookingPageUrlPattern);
  });

  describe('ChatBooking page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(chatBookingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChatBooking page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/chat-booking/new$'));
        cy.getEntityCreateUpdateHeading('ChatBooking');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatBookingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/chat-bookings',
          body: chatBookingSample,
        }).then(({ body }) => {
          chatBooking = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/chat-bookings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/chat-bookings?page=0&size=20>; rel="last",<http://localhost/api/chat-bookings?page=0&size=20>; rel="first"',
              },
              body: [chatBooking],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(chatBookingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ChatBooking page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('chatBooking');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatBookingPageUrlPattern);
      });

      it('edit button click should load edit ChatBooking page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatBooking');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatBookingPageUrlPattern);
      });

      it('edit button click should load edit ChatBooking page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatBooking');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatBookingPageUrlPattern);
      });

      it('last delete button click should delete instance of ChatBooking', () => {
        cy.intercept('GET', '/api/chat-bookings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('chatBooking').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatBookingPageUrlPattern);

        chatBooking = undefined;
      });
    });
  });

  describe('new ChatBooking page', () => {
    beforeEach(() => {
      cy.visit(`${chatBookingPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ChatBooking');
    });

    it('should create an instance of ChatBooking', () => {
      cy.get(`[data-cy="bookingTimestamp"]`).type('2024-06-03T21:55');
      cy.get(`[data-cy="bookingTimestamp"]`).blur();
      cy.get(`[data-cy="bookingTimestamp"]`).should('have.value', '2024-06-03T21:55');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        chatBooking = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', chatBookingPageUrlPattern);
    });
  });
});
