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

describe('Tratamiento e2e test', () => {
  const tratamientoPageUrl = '/tratamiento';
  const tratamientoPageUrlPattern = new RegExp('/tratamiento(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tratamientoSample = { fechaInicio: '2024-04-18', fechaFin: '2024-04-18' };

  let tratamiento;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tratamientos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tratamientos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tratamientos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tratamiento) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tratamientos/${tratamiento.id}`,
      }).then(() => {
        tratamiento = undefined;
      });
    }
  });

  it('Tratamientos menu should load Tratamientos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tratamiento');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Tratamiento').should('exist');
    cy.url().should('match', tratamientoPageUrlPattern);
  });

  describe('Tratamiento page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tratamientoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Tratamiento page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tratamiento/new$'));
        cy.getEntityCreateUpdateHeading('Tratamiento');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tratamientoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tratamientos',
          body: tratamientoSample,
        }).then(({ body }) => {
          tratamiento = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tratamientos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tratamientos?page=0&size=20>; rel="last",<http://localhost/api/tratamientos?page=0&size=20>; rel="first"',
              },
              body: [tratamiento],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(tratamientoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Tratamiento page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tratamiento');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tratamientoPageUrlPattern);
      });

      it('edit button click should load edit Tratamiento page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tratamiento');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tratamientoPageUrlPattern);
      });

      it('edit button click should load edit Tratamiento page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tratamiento');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tratamientoPageUrlPattern);
      });

      it('last delete button click should delete instance of Tratamiento', () => {
        cy.intercept('GET', '/api/tratamientos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('tratamiento').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tratamientoPageUrlPattern);

        tratamiento = undefined;
      });
    });
  });

  describe('new Tratamiento page', () => {
    beforeEach(() => {
      cy.visit(`${tratamientoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Tratamiento');
    });

    it('should create an instance of Tratamiento', () => {
      cy.get(`[data-cy="fechaInicio"]`).type('2024-04-18');
      cy.get(`[data-cy="fechaInicio"]`).blur();
      cy.get(`[data-cy="fechaInicio"]`).should('have.value', '2024-04-18');

      cy.get(`[data-cy="fechaFin"]`).type('2024-04-18');
      cy.get(`[data-cy="fechaFin"]`).blur();
      cy.get(`[data-cy="fechaFin"]`).should('have.value', '2024-04-18');

      cy.get(`[data-cy="notas"]`).type('upright rightsize');
      cy.get(`[data-cy="notas"]`).should('have.value', 'upright rightsize');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        tratamiento = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', tratamientoPageUrlPattern);
    });
  });
});
