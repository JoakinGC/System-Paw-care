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

describe('Factores e2e test', () => {
  const factoresPageUrl = '/factores';
  const factoresPageUrlPattern = new RegExp('/factores(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const factoresSample = {};

  let factores;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/factores+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/factores').as('postEntityRequest');
    cy.intercept('DELETE', '/api/factores/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (factores) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/factores/${factores.id}`,
      }).then(() => {
        factores = undefined;
      });
    }
  });

  it('Factores menu should load Factores page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('factores');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Factores').should('exist');
    cy.url().should('match', factoresPageUrlPattern);
  });

  describe('Factores page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(factoresPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Factores page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/factores/new$'));
        cy.getEntityCreateUpdateHeading('Factores');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', factoresPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/factores',
          body: factoresSample,
        }).then(({ body }) => {
          factores = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/factores+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/factores?page=0&size=20>; rel="last",<http://localhost/api/factores?page=0&size=20>; rel="first"',
              },
              body: [factores],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(factoresPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Factores page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('factores');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', factoresPageUrlPattern);
      });

      it('edit button click should load edit Factores page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Factores');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', factoresPageUrlPattern);
      });

      it('edit button click should load edit Factores page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Factores');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', factoresPageUrlPattern);
      });

      it('last delete button click should delete instance of Factores', () => {
        cy.intercept('GET', '/api/factores/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('factores').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', factoresPageUrlPattern);

        factores = undefined;
      });
    });
  });

  describe('new Factores page', () => {
    beforeEach(() => {
      cy.visit(`${factoresPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Factores');
    });

    it('should create an instance of Factores', () => {
      cy.get(`[data-cy="nombre"]`).type('trim');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'trim');

      cy.get(`[data-cy="tipo"]`).type('actually regarding world');
      cy.get(`[data-cy="tipo"]`).should('have.value', 'actually regarding world');

      cy.get(`[data-cy="descripcion"]`).type('gee');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'gee');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        factores = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', factoresPageUrlPattern);
    });
  });
});
