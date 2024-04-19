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

describe('Historial e2e test', () => {
  const historialPageUrl = '/historial';
  const historialPageUrlPattern = new RegExp('/historial(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const historialSample = { fechaConsulta: '2024-04-18', diagnostico: 'fairly hence earnest' };

  let historial;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/historials+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/historials').as('postEntityRequest');
    cy.intercept('DELETE', '/api/historials/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (historial) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/historials/${historial.id}`,
      }).then(() => {
        historial = undefined;
      });
    }
  });

  it('Historials menu should load Historials page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('historial');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Historial').should('exist');
    cy.url().should('match', historialPageUrlPattern);
  });

  describe('Historial page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(historialPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Historial page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/historial/new$'));
        cy.getEntityCreateUpdateHeading('Historial');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historialPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/historials',
          body: historialSample,
        }).then(({ body }) => {
          historial = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/historials+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/historials?page=0&size=20>; rel="last",<http://localhost/api/historials?page=0&size=20>; rel="first"',
              },
              body: [historial],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(historialPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Historial page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('historial');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historialPageUrlPattern);
      });

      it('edit button click should load edit Historial page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Historial');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historialPageUrlPattern);
      });

      it('edit button click should load edit Historial page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Historial');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historialPageUrlPattern);
      });

      it('last delete button click should delete instance of Historial', () => {
        cy.intercept('GET', '/api/historials/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('historial').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historialPageUrlPattern);

        historial = undefined;
      });
    });
  });

  describe('new Historial page', () => {
    beforeEach(() => {
      cy.visit(`${historialPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Historial');
    });

    it('should create an instance of Historial', () => {
      cy.get(`[data-cy="fechaConsulta"]`).type('2024-04-17');
      cy.get(`[data-cy="fechaConsulta"]`).blur();
      cy.get(`[data-cy="fechaConsulta"]`).should('have.value', '2024-04-17');

      cy.get(`[data-cy="diagnostico"]`).type('collateral now engage');
      cy.get(`[data-cy="diagnostico"]`).should('have.value', 'collateral now engage');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        historial = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', historialPageUrlPattern);
    });
  });
});
