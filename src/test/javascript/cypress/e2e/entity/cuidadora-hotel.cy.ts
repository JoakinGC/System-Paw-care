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

describe('CuidadoraHotel e2e test', () => {
  const cuidadoraHotelPageUrl = '/cuidadora-hotel';
  const cuidadoraHotelPageUrlPattern = new RegExp('/cuidadora-hotel(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const cuidadoraHotelSample = {};

  let cuidadoraHotel;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cuidadora-hotels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cuidadora-hotels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cuidadora-hotels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cuidadoraHotel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cuidadora-hotels/${cuidadoraHotel.id}`,
      }).then(() => {
        cuidadoraHotel = undefined;
      });
    }
  });

  it('CuidadoraHotels menu should load CuidadoraHotels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cuidadora-hotel');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CuidadoraHotel').should('exist');
    cy.url().should('match', cuidadoraHotelPageUrlPattern);
  });

  describe('CuidadoraHotel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cuidadoraHotelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CuidadoraHotel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cuidadora-hotel/new$'));
        cy.getEntityCreateUpdateHeading('CuidadoraHotel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cuidadoraHotelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cuidadora-hotels',
          body: cuidadoraHotelSample,
        }).then(({ body }) => {
          cuidadoraHotel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cuidadora-hotels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cuidadora-hotels?page=0&size=20>; rel="last",<http://localhost/api/cuidadora-hotels?page=0&size=20>; rel="first"',
              },
              body: [cuidadoraHotel],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cuidadoraHotelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CuidadoraHotel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cuidadoraHotel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cuidadoraHotelPageUrlPattern);
      });

      it('edit button click should load edit CuidadoraHotel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CuidadoraHotel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cuidadoraHotelPageUrlPattern);
      });

      it('edit button click should load edit CuidadoraHotel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CuidadoraHotel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cuidadoraHotelPageUrlPattern);
      });

      it('last delete button click should delete instance of CuidadoraHotel', () => {
        cy.intercept('GET', '/api/cuidadora-hotels/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cuidadoraHotel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cuidadoraHotelPageUrlPattern);

        cuidadoraHotel = undefined;
      });
    });
  });

  describe('new CuidadoraHotel page', () => {
    beforeEach(() => {
      cy.visit(`${cuidadoraHotelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CuidadoraHotel');
    });

    it('should create an instance of CuidadoraHotel', () => {
      cy.get(`[data-cy="nombre"]`).type('till wonderfully');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'till wonderfully');

      cy.get(`[data-cy="direccion"]`).type('button');
      cy.get(`[data-cy="direccion"]`).should('have.value', 'button');

      cy.get(`[data-cy="telefono"]`).type('helplessl');
      cy.get(`[data-cy="telefono"]`).should('have.value', 'helplessl');

      cy.get(`[data-cy="servicioOfrecido"]`).type('provided brr');
      cy.get(`[data-cy="servicioOfrecido"]`).should('have.value', 'provided brr');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        cuidadoraHotel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', cuidadoraHotelPageUrlPattern);
    });
  });
});
