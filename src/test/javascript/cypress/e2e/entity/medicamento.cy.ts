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

describe('Medicamento e2e test', () => {
  const medicamentoPageUrl = '/medicamento';
  const medicamentoPageUrlPattern = new RegExp('/medicamento(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const medicamentoSample = { nombre: 'well-lit whereas' };

  let medicamento;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/medicamentos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/medicamentos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/medicamentos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (medicamento) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/medicamentos/${medicamento.id}`,
      }).then(() => {
        medicamento = undefined;
      });
    }
  });

  it('Medicamentos menu should load Medicamentos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('medicamento');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Medicamento').should('exist');
    cy.url().should('match', medicamentoPageUrlPattern);
  });

  describe('Medicamento page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(medicamentoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Medicamento page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/medicamento/new$'));
        cy.getEntityCreateUpdateHeading('Medicamento');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', medicamentoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/medicamentos',
          body: medicamentoSample,
        }).then(({ body }) => {
          medicamento = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/medicamentos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/medicamentos?page=0&size=20>; rel="last",<http://localhost/api/medicamentos?page=0&size=20>; rel="first"',
              },
              body: [medicamento],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(medicamentoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Medicamento page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('medicamento');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', medicamentoPageUrlPattern);
      });

      it('edit button click should load edit Medicamento page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Medicamento');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', medicamentoPageUrlPattern);
      });

      it('edit button click should load edit Medicamento page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Medicamento');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', medicamentoPageUrlPattern);
      });

      it('last delete button click should delete instance of Medicamento', () => {
        cy.intercept('GET', '/api/medicamentos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('medicamento').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', medicamentoPageUrlPattern);

        medicamento = undefined;
      });
    });
  });

  describe('new Medicamento page', () => {
    beforeEach(() => {
      cy.visit(`${medicamentoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Medicamento');
    });

    it('should create an instance of Medicamento', () => {
      cy.get(`[data-cy="nombre"]`).type('fuzzy drat making');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'fuzzy drat making');

      cy.get(`[data-cy="descripcion"]`).type('softly');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'softly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        medicamento = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', medicamentoPageUrlPattern);
    });
  });
});
