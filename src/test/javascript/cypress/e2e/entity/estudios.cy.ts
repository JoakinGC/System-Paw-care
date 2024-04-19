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

describe('Estudios e2e test', () => {
  const estudiosPageUrl = '/estudios';
  const estudiosPageUrlPattern = new RegExp('/estudios(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const estudiosSample = { fechaCursado: '2024-04-18' };

  let estudios;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/estudios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/estudios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/estudios/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (estudios) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/estudios/${estudios.id}`,
      }).then(() => {
        estudios = undefined;
      });
    }
  });

  it('Estudios menu should load Estudios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('estudios');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Estudios').should('exist');
    cy.url().should('match', estudiosPageUrlPattern);
  });

  describe('Estudios page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(estudiosPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Estudios page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/estudios/new$'));
        cy.getEntityCreateUpdateHeading('Estudios');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', estudiosPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/estudios',
          body: estudiosSample,
        }).then(({ body }) => {
          estudios = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/estudios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/estudios?page=0&size=20>; rel="last",<http://localhost/api/estudios?page=0&size=20>; rel="first"',
              },
              body: [estudios],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(estudiosPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Estudios page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('estudios');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', estudiosPageUrlPattern);
      });

      it('edit button click should load edit Estudios page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Estudios');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', estudiosPageUrlPattern);
      });

      it('edit button click should load edit Estudios page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Estudios');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', estudiosPageUrlPattern);
      });

      it('last delete button click should delete instance of Estudios', () => {
        cy.intercept('GET', '/api/estudios/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('estudios').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', estudiosPageUrlPattern);

        estudios = undefined;
      });
    });
  });

  describe('new Estudios page', () => {
    beforeEach(() => {
      cy.visit(`${estudiosPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Estudios');
    });

    it('should create an instance of Estudios', () => {
      cy.get(`[data-cy="nombre"]`).type('unless');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'unless');

      cy.get(`[data-cy="fechaCursado"]`).type('2024-04-18');
      cy.get(`[data-cy="fechaCursado"]`).blur();
      cy.get(`[data-cy="fechaCursado"]`).should('have.value', '2024-04-18');

      cy.get(`[data-cy="nombreInsituto"]`).type('peripheral');
      cy.get(`[data-cy="nombreInsituto"]`).should('have.value', 'peripheral');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        estudios = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', estudiosPageUrlPattern);
    });
  });
});
