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

describe('Dueno e2e test', () => {
  const duenoPageUrl = '/dueno';
  const duenoPageUrlPattern = new RegExp('/dueno(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const duenoSample = {};

  let dueno;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/duenos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/duenos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/duenos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dueno) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/duenos/${dueno.id}`,
      }).then(() => {
        dueno = undefined;
      });
    }
  });

  it('Duenos menu should load Duenos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('dueno');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Dueno').should('exist');
    cy.url().should('match', duenoPageUrlPattern);
  });

  describe('Dueno page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(duenoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Dueno page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/dueno/new$'));
        cy.getEntityCreateUpdateHeading('Dueno');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', duenoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/duenos',
          body: duenoSample,
        }).then(({ body }) => {
          dueno = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/duenos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/duenos?page=0&size=20>; rel="last",<http://localhost/api/duenos?page=0&size=20>; rel="first"',
              },
              body: [dueno],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(duenoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Dueno page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dueno');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', duenoPageUrlPattern);
      });

      it('edit button click should load edit Dueno page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dueno');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', duenoPageUrlPattern);
      });

      it('edit button click should load edit Dueno page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dueno');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', duenoPageUrlPattern);
      });

      it('last delete button click should delete instance of Dueno', () => {
        cy.intercept('GET', '/api/duenos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('dueno').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', duenoPageUrlPattern);

        dueno = undefined;
      });
    });
  });

  describe('new Dueno page', () => {
    beforeEach(() => {
      cy.visit(`${duenoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Dueno');
    });

    it('should create an instance of Dueno', () => {
      cy.get(`[data-cy="nombre"]`).type('clean pump scoff');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'clean pump scoff');

      cy.get(`[data-cy="apellido"]`).type('although');
      cy.get(`[data-cy="apellido"]`).should('have.value', 'although');

      cy.get(`[data-cy="direccion"]`).type('since when yowza');
      cy.get(`[data-cy="direccion"]`).should('have.value', 'since when yowza');

      cy.get(`[data-cy="telefono"]`).type('intensely');
      cy.get(`[data-cy="telefono"]`).should('have.value', 'intensely');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        dueno = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', duenoPageUrlPattern);
    });
  });
});
