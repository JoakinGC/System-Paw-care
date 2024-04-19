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

describe('DatelleCompra e2e test', () => {
  const datelleCompraPageUrl = '/datelle-compra';
  const datelleCompraPageUrlPattern = new RegExp('/datelle-compra(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const datelleCompraSample = { cantidad: 21636.88, precioUnitario: 3325.18, totalProducto: 22693.67 };

  let datelleCompra;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/datelle-compras+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/datelle-compras').as('postEntityRequest');
    cy.intercept('DELETE', '/api/datelle-compras/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (datelleCompra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/datelle-compras/${datelleCompra.id}`,
      }).then(() => {
        datelleCompra = undefined;
      });
    }
  });

  it('DatelleCompras menu should load DatelleCompras page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('datelle-compra');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DatelleCompra').should('exist');
    cy.url().should('match', datelleCompraPageUrlPattern);
  });

  describe('DatelleCompra page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(datelleCompraPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DatelleCompra page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/datelle-compra/new$'));
        cy.getEntityCreateUpdateHeading('DatelleCompra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', datelleCompraPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/datelle-compras',
          body: datelleCompraSample,
        }).then(({ body }) => {
          datelleCompra = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/datelle-compras+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/datelle-compras?page=0&size=20>; rel="last",<http://localhost/api/datelle-compras?page=0&size=20>; rel="first"',
              },
              body: [datelleCompra],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(datelleCompraPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DatelleCompra page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('datelleCompra');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', datelleCompraPageUrlPattern);
      });

      it('edit button click should load edit DatelleCompra page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DatelleCompra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', datelleCompraPageUrlPattern);
      });

      it('edit button click should load edit DatelleCompra page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DatelleCompra');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', datelleCompraPageUrlPattern);
      });

      it('last delete button click should delete instance of DatelleCompra', () => {
        cy.intercept('GET', '/api/datelle-compras/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('datelleCompra').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', datelleCompraPageUrlPattern);

        datelleCompra = undefined;
      });
    });
  });

  describe('new DatelleCompra page', () => {
    beforeEach(() => {
      cy.visit(`${datelleCompraPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DatelleCompra');
    });

    it('should create an instance of DatelleCompra', () => {
      cy.get(`[data-cy="cantidad"]`).type('14981.81');
      cy.get(`[data-cy="cantidad"]`).should('have.value', '14981.81');

      cy.get(`[data-cy="precioUnitario"]`).type('15142.47');
      cy.get(`[data-cy="precioUnitario"]`).should('have.value', '15142.47');

      cy.get(`[data-cy="totalProducto"]`).type('21984.05');
      cy.get(`[data-cy="totalProducto"]`).should('have.value', '21984.05');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        datelleCompra = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', datelleCompraPageUrlPattern);
    });
  });
});
