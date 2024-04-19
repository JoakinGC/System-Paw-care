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

describe('Raza e2e test', () => {
  const razaPageUrl = '/raza';
  const razaPageUrlPattern = new RegExp('/raza(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const razaSample = {};

  let raza;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/razas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/razas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/razas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (raza) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/razas/${raza.id}`,
      }).then(() => {
        raza = undefined;
      });
    }
  });

  it('Razas menu should load Razas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('raza');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Raza').should('exist');
    cy.url().should('match', razaPageUrlPattern);
  });

  describe('Raza page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(razaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Raza page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/raza/new$'));
        cy.getEntityCreateUpdateHeading('Raza');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', razaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/razas',
          body: razaSample,
        }).then(({ body }) => {
          raza = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/razas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/razas?page=0&size=20>; rel="last",<http://localhost/api/razas?page=0&size=20>; rel="first"',
              },
              body: [raza],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(razaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Raza page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('raza');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', razaPageUrlPattern);
      });

      it('edit button click should load edit Raza page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Raza');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', razaPageUrlPattern);
      });

      it('edit button click should load edit Raza page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Raza');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', razaPageUrlPattern);
      });

      it('last delete button click should delete instance of Raza', () => {
        cy.intercept('GET', '/api/razas/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('raza').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', razaPageUrlPattern);

        raza = undefined;
      });
    });
  });

  describe('new Raza page', () => {
    beforeEach(() => {
      cy.visit(`${razaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Raza');
    });

    it('should create an instance of Raza', () => {
      cy.get(`[data-cy="nombre"]`).type('past');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'past');

      cy.get(`[data-cy="nombreCientifico"]`).type('apud extraneous mmm');
      cy.get(`[data-cy="nombreCientifico"]`).should('have.value', 'apud extraneous mmm');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        raza = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', razaPageUrlPattern);
    });
  });
});
