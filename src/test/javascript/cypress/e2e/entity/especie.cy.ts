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

describe('Especie e2e test', () => {
  const especiePageUrl = '/especie';
  const especiePageUrlPattern = new RegExp('/especie(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const especieSample = {};

  let especie;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/especies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/especies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/especies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (especie) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/especies/${especie.id}`,
      }).then(() => {
        especie = undefined;
      });
    }
  });

  it('Especies menu should load Especies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('especie');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Especie').should('exist');
    cy.url().should('match', especiePageUrlPattern);
  });

  describe('Especie page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(especiePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Especie page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/especie/new$'));
        cy.getEntityCreateUpdateHeading('Especie');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', especiePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/especies',
          body: especieSample,
        }).then(({ body }) => {
          especie = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/especies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/especies?page=0&size=20>; rel="last",<http://localhost/api/especies?page=0&size=20>; rel="first"',
              },
              body: [especie],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(especiePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Especie page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('especie');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', especiePageUrlPattern);
      });

      it('edit button click should load edit Especie page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Especie');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', especiePageUrlPattern);
      });

      it('edit button click should load edit Especie page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Especie');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', especiePageUrlPattern);
      });

      it('last delete button click should delete instance of Especie', () => {
        cy.intercept('GET', '/api/especies/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('especie').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', especiePageUrlPattern);

        especie = undefined;
      });
    });
  });

  describe('new Especie page', () => {
    beforeEach(() => {
      cy.visit(`${especiePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Especie');
    });

    it('should create an instance of Especie', () => {
      cy.get(`[data-cy="nombre"]`).type('nor while');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'nor while');

      cy.get(`[data-cy="nombreCientifico"]`).type('fooey cilantro chunder');
      cy.get(`[data-cy="nombreCientifico"]`).should('have.value', 'fooey cilantro chunder');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        especie = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', especiePageUrlPattern);
    });
  });
});
