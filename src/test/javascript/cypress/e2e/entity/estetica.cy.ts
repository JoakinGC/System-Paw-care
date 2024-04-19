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

describe('Estetica e2e test', () => {
  const esteticaPageUrl = '/estetica';
  const esteticaPageUrlPattern = new RegExp('/estetica(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const esteticaSample = {};

  let estetica;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/esteticas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/esteticas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/esteticas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (estetica) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/esteticas/${estetica.id}`,
      }).then(() => {
        estetica = undefined;
      });
    }
  });

  it('Esteticas menu should load Esteticas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('estetica');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Estetica').should('exist');
    cy.url().should('match', esteticaPageUrlPattern);
  });

  describe('Estetica page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(esteticaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Estetica page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/estetica/new$'));
        cy.getEntityCreateUpdateHeading('Estetica');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', esteticaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/esteticas',
          body: esteticaSample,
        }).then(({ body }) => {
          estetica = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/esteticas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/esteticas?page=0&size=20>; rel="last",<http://localhost/api/esteticas?page=0&size=20>; rel="first"',
              },
              body: [estetica],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(esteticaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Estetica page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('estetica');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', esteticaPageUrlPattern);
      });

      it('edit button click should load edit Estetica page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Estetica');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', esteticaPageUrlPattern);
      });

      it('edit button click should load edit Estetica page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Estetica');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', esteticaPageUrlPattern);
      });

      it('last delete button click should delete instance of Estetica', () => {
        cy.intercept('GET', '/api/esteticas/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('estetica').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', esteticaPageUrlPattern);

        estetica = undefined;
      });
    });
  });

  describe('new Estetica page', () => {
    beforeEach(() => {
      cy.visit(`${esteticaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Estetica');
    });

    it('should create an instance of Estetica', () => {
      cy.get(`[data-cy="nombre"]`).type('alarmed always grab');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'alarmed always grab');

      cy.get(`[data-cy="direcion"]`).type('when sandbar randomise');
      cy.get(`[data-cy="direcion"]`).should('have.value', 'when sandbar randomise');

      cy.get(`[data-cy="telefono"]`).type('alienate');
      cy.get(`[data-cy="telefono"]`).should('have.value', 'alienate');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        estetica = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', esteticaPageUrlPattern);
    });
  });
});
