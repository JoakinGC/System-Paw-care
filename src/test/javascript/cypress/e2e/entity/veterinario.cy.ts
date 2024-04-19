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

describe('Veterinario e2e test', () => {
  const veterinarioPageUrl = '/veterinario';
  const veterinarioPageUrlPattern = new RegExp('/veterinario(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const veterinarioSample = {};

  let veterinario;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/veterinarios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/veterinarios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/veterinarios/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (veterinario) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/veterinarios/${veterinario.id}`,
      }).then(() => {
        veterinario = undefined;
      });
    }
  });

  it('Veterinarios menu should load Veterinarios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('veterinario');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Veterinario').should('exist');
    cy.url().should('match', veterinarioPageUrlPattern);
  });

  describe('Veterinario page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(veterinarioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Veterinario page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/veterinario/new$'));
        cy.getEntityCreateUpdateHeading('Veterinario');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', veterinarioPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/veterinarios',
          body: veterinarioSample,
        }).then(({ body }) => {
          veterinario = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/veterinarios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/veterinarios?page=0&size=20>; rel="last",<http://localhost/api/veterinarios?page=0&size=20>; rel="first"',
              },
              body: [veterinario],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(veterinarioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Veterinario page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('veterinario');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', veterinarioPageUrlPattern);
      });

      it('edit button click should load edit Veterinario page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Veterinario');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', veterinarioPageUrlPattern);
      });

      it('edit button click should load edit Veterinario page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Veterinario');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', veterinarioPageUrlPattern);
      });

      it('last delete button click should delete instance of Veterinario', () => {
        cy.intercept('GET', '/api/veterinarios/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('veterinario').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', veterinarioPageUrlPattern);

        veterinario = undefined;
      });
    });
  });

  describe('new Veterinario page', () => {
    beforeEach(() => {
      cy.visit(`${veterinarioPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Veterinario');
    });

    it('should create an instance of Veterinario', () => {
      cy.get(`[data-cy="nombre"]`).type('strategy');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'strategy');

      cy.get(`[data-cy="apellido"]`).type('dearly');
      cy.get(`[data-cy="apellido"]`).should('have.value', 'dearly');

      cy.get(`[data-cy="direccion"]`).type('sight hmph');
      cy.get(`[data-cy="direccion"]`).should('have.value', 'sight hmph');

      cy.get(`[data-cy="telefono"]`).type('softly');
      cy.get(`[data-cy="telefono"]`).should('have.value', 'softly');

      cy.get(`[data-cy="especilidad"]`).type('whenever envious ten');
      cy.get(`[data-cy="especilidad"]`).should('have.value', 'whenever envious ten');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        veterinario = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', veterinarioPageUrlPattern);
    });
  });
});
