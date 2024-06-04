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

describe('UserAccount e2e test', () => {
  const userAccountPageUrl = '/user-account';
  const userAccountPageUrlPattern = new RegExp('/user-account(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userAccountSample = {
    accountName: 'Credit Card Account',
    emailAddress: 'jV|C`A@Zu[+;..9',
    admissionDate: '2024-06-03',
    createdAt: '2024-06-03T14:12:41.686Z',
    updatedAt: '2024-06-03T15:58:58.421Z',
    isActive: true,
    password: 'as cough athwart',
  };

  let userAccount;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-accounts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-accounts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-accounts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userAccount) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-accounts/${userAccount.id}`,
      }).then(() => {
        userAccount = undefined;
      });
    }
  });

  it('UserAccounts menu should load UserAccounts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-account');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserAccount').should('exist');
    cy.url().should('match', userAccountPageUrlPattern);
  });

  describe('UserAccount page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userAccountPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserAccount page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-account/new$'));
        cy.getEntityCreateUpdateHeading('UserAccount');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAccountPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-accounts',
          body: userAccountSample,
        }).then(({ body }) => {
          userAccount = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-accounts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-accounts?page=0&size=20>; rel="last",<http://localhost/api/user-accounts?page=0&size=20>; rel="first"',
              },
              body: [userAccount],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userAccountPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserAccount page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userAccount');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAccountPageUrlPattern);
      });

      it('edit button click should load edit UserAccount page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserAccount');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAccountPageUrlPattern);
      });

      it('edit button click should load edit UserAccount page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserAccount');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAccountPageUrlPattern);
      });

      it('last delete button click should delete instance of UserAccount', () => {
        cy.intercept('GET', '/api/user-accounts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userAccount').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAccountPageUrlPattern);

        userAccount = undefined;
      });
    });
  });

  describe('new UserAccount page', () => {
    beforeEach(() => {
      cy.visit(`${userAccountPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserAccount');
    });

    it('should create an instance of UserAccount', () => {
      cy.get(`[data-cy="accountName"]`).type('Investment Account');
      cy.get(`[data-cy="accountName"]`).should('have.value', 'Investment Account');

      cy.get(`[data-cy="emailAddress"]`).type('j&d5$@R60.M5');
      cy.get(`[data-cy="emailAddress"]`).should('have.value', 'j&d5$@R60.M5');

      cy.get(`[data-cy="admissionDate"]`).type('2024-06-03');
      cy.get(`[data-cy="admissionDate"]`).blur();
      cy.get(`[data-cy="admissionDate"]`).should('have.value', '2024-06-03');

      cy.get(`[data-cy="createdAt"]`).type('2024-06-03T04:26');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-06-03T04:26');

      cy.get(`[data-cy="updatedAt"]`).type('2024-06-03T13:40');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-06-03T13:40');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="mobilePhone"]`).type('snoopy sputter reassuringly');
      cy.get(`[data-cy="mobilePhone"]`).should('have.value', 'snoopy sputter reassuringly');

      cy.get(`[data-cy="rpushFeedbackId"]`).type('7375');
      cy.get(`[data-cy="rpushFeedbackId"]`).should('have.value', '7375');

      cy.get(`[data-cy="execCommands"]`).should('not.be.checked');
      cy.get(`[data-cy="execCommands"]`).click();
      cy.get(`[data-cy="execCommands"]`).should('be.checked');

      cy.get(`[data-cy="isBlocked"]`).should('not.be.checked');
      cy.get(`[data-cy="isBlocked"]`).click();
      cy.get(`[data-cy="isBlocked"]`).should('be.checked');

      cy.get(`[data-cy="employerName"]`).type('boo evince');
      cy.get(`[data-cy="employerName"]`).should('have.value', 'boo evince');

      cy.get(`[data-cy="pushConfiguration"]`).type('15544');
      cy.get(`[data-cy="pushConfiguration"]`).should('have.value', '15544');

      cy.get(`[data-cy="traveledDistance"]`).type('430.34');
      cy.get(`[data-cy="traveledDistance"]`).should('have.value', '430.34');

      cy.get(`[data-cy="language"]`).type('authorize an');
      cy.get(`[data-cy="language"]`).should('have.value', 'authorize an');

      cy.get(`[data-cy="blockedReason"]`).type('but');
      cy.get(`[data-cy="blockedReason"]`).should('have.value', 'but');

      cy.get(`[data-cy="blockedById"]`).type('11254');
      cy.get(`[data-cy="blockedById"]`).should('have.value', '11254');

      cy.get(`[data-cy="blockedAt"]`).type('2024-06-03T17:39');
      cy.get(`[data-cy="blockedAt"]`).blur();
      cy.get(`[data-cy="blockedAt"]`).should('have.value', '2024-06-03T17:39');

      cy.get(`[data-cy="deletedReason"]`).type('gah ouch');
      cy.get(`[data-cy="deletedReason"]`).should('have.value', 'gah ouch');

      cy.get(`[data-cy="deletedAt"]`).type('2024-06-03T06:59');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-06-03T06:59');

      cy.get(`[data-cy="deletedById"]`).type('21282');
      cy.get(`[data-cy="deletedById"]`).should('have.value', '21282');

      cy.get(`[data-cy="lastModifiedBy"]`).type('per brr old');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'per brr old');

      cy.get(`[data-cy="registrationCode"]`).type('almost since sizzling');
      cy.get(`[data-cy="registrationCode"]`).should('have.value', 'almost since sizzling');

      cy.get(`[data-cy="password"]`).type('sneaky versus');
      cy.get(`[data-cy="password"]`).should('have.value', 'sneaky versus');

      cy.get(`[data-cy="passwordHint"]`).type('bite-sized abaft eek');
      cy.get(`[data-cy="passwordHint"]`).should('have.value', 'bite-sized abaft eek');

      cy.get(`[data-cy="featureFlags"]`).type('reproachfully geez lest');
      cy.get(`[data-cy="featureFlags"]`).should('have.value', 'reproachfully geez lest');

      cy.get(`[data-cy="zipCode"]`).type('68668-8808');
      cy.get(`[data-cy="zipCode"]`).should('have.value', '68668-8808');

      cy.get(`[data-cy="publicPlace"]`).type('attribute consequently potentially');
      cy.get(`[data-cy="publicPlace"]`).should('have.value', 'attribute consequently potentially');

      cy.get(`[data-cy="addressNumber"]`).type('measurement');
      cy.get(`[data-cy="addressNumber"]`).should('have.value', 'measurement');

      cy.get(`[data-cy="streetName"]`).type('Ritchie Fields');
      cy.get(`[data-cy="streetName"]`).should('have.value', 'Ritchie Fields');

      cy.get(`[data-cy="addressComplement"]`).type('dimple relieved');
      cy.get(`[data-cy="addressComplement"]`).should('have.value', 'dimple relieved');

      cy.get(`[data-cy="cityName"]`).type('which');
      cy.get(`[data-cy="cityName"]`).should('have.value', 'which');

      cy.get(`[data-cy="stateName"]`).type('duh');
      cy.get(`[data-cy="stateName"]`).should('have.value', 'duh');

      cy.get(`[data-cy="cnhImage"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="cnhImage"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="profileImage"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="profileImage"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="cnhExpirationDate"]`).type('2024-06-04');
      cy.get(`[data-cy="cnhExpirationDate"]`).blur();
      cy.get(`[data-cy="cnhExpirationDate"]`).should('have.value', '2024-06-04');

      cy.get(`[data-cy="cnhStatus"]`).select('INVALID');

      cy.get(`[data-cy="registrationStatus"]`).select('APPROVED');

      cy.get(`[data-cy="analyzedBy"]`).type('over urgently');
      cy.get(`[data-cy="analyzedBy"]`).should('have.value', 'over urgently');

      cy.get(`[data-cy="analyzedAt"]`).type('2024-06-03T06:42');
      cy.get(`[data-cy="analyzedAt"]`).blur();
      cy.get(`[data-cy="analyzedAt"]`).should('have.value', '2024-06-03T06:42');

      cy.get(`[data-cy="signatureImage"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="signatureImage"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="residenceProofImage"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="residenceProofImage"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userAccount = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userAccountPageUrlPattern);
    });
  });
});
