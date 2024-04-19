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

describe('Enfermedad e2e test', () => {
  const enfermedadPageUrl = '/enfermedad';
  const enfermedadPageUrlPattern = new RegExp('/enfermedad(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const enfermedadSample = {};

  let enfermedad;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/enfermedads+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/enfermedads').as('postEntityRequest');
    cy.intercept('DELETE', '/api/enfermedads/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (enfermedad) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/enfermedads/${enfermedad.id}`,
      }).then(() => {
        enfermedad = undefined;
      });
    }
  });

  it('Enfermedads menu should load Enfermedads page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('enfermedad');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Enfermedad').should('exist');
    cy.url().should('match', enfermedadPageUrlPattern);
  });

  describe('Enfermedad page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(enfermedadPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Enfermedad page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/enfermedad/new$'));
        cy.getEntityCreateUpdateHeading('Enfermedad');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enfermedadPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/enfermedads',
          body: enfermedadSample,
        }).then(({ body }) => {
          enfermedad = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/enfermedads+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/enfermedads?page=0&size=20>; rel="last",<http://localhost/api/enfermedads?page=0&size=20>; rel="first"',
              },
              body: [enfermedad],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(enfermedadPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Enfermedad page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('enfermedad');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enfermedadPageUrlPattern);
      });

      it('edit button click should load edit Enfermedad page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enfermedad');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enfermedadPageUrlPattern);
      });

      it('edit button click should load edit Enfermedad page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enfermedad');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enfermedadPageUrlPattern);
      });

      it('last delete button click should delete instance of Enfermedad', () => {
        cy.intercept('GET', '/api/enfermedads/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('enfermedad').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enfermedadPageUrlPattern);

        enfermedad = undefined;
      });
    });
  });

  describe('new Enfermedad page', () => {
    beforeEach(() => {
      cy.visit(`${enfermedadPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Enfermedad');
    });

    it('should create an instance of Enfermedad', () => {
      cy.get(`[data-cy="nombre"]`).type('tear hopeful');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'tear hopeful');

      cy.get(`[data-cy="descripcion"]`).type('ethical seek');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'ethical seek');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        enfermedad = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', enfermedadPageUrlPattern);
    });
  });
});
