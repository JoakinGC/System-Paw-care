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

describe('Terapia e2e test', () => {
  const terapiaPageUrl = '/terapia';
  const terapiaPageUrlPattern = new RegExp('/terapia(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const terapiaSample = {};

  let terapia;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/terapias+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/terapias').as('postEntityRequest');
    cy.intercept('DELETE', '/api/terapias/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (terapia) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/terapias/${terapia.id}`,
      }).then(() => {
        terapia = undefined;
      });
    }
  });

  it('Terapias menu should load Terapias page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('terapia');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Terapia').should('exist');
    cy.url().should('match', terapiaPageUrlPattern);
  });

  describe('Terapia page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(terapiaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Terapia page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/terapia/new$'));
        cy.getEntityCreateUpdateHeading('Terapia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', terapiaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/terapias',
          body: terapiaSample,
        }).then(({ body }) => {
          terapia = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/terapias+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/terapias?page=0&size=20>; rel="last",<http://localhost/api/terapias?page=0&size=20>; rel="first"',
              },
              body: [terapia],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(terapiaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Terapia page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('terapia');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', terapiaPageUrlPattern);
      });

      it('edit button click should load edit Terapia page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Terapia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', terapiaPageUrlPattern);
      });

      it('edit button click should load edit Terapia page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Terapia');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', terapiaPageUrlPattern);
      });

      it('last delete button click should delete instance of Terapia', () => {
        cy.intercept('GET', '/api/terapias/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('terapia').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', terapiaPageUrlPattern);

        terapia = undefined;
      });
    });
  });

  describe('new Terapia page', () => {
    beforeEach(() => {
      cy.visit(`${terapiaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Terapia');
    });

    it('should create an instance of Terapia', () => {
      cy.get(`[data-cy="nombre"]`).type('smolt via willfully');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'smolt via willfully');

      cy.get(`[data-cy="descripcion"]`).type('burn');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'burn');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        terapia = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', terapiaPageUrlPattern);
    });
  });
});
