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

describe('Mascota e2e test', () => {
  const mascotaPageUrl = '/mascota';
  const mascotaPageUrlPattern = new RegExp('/mascota(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const mascotaSample = { nIdentificacionCarnet: 4478, foto: 'but the election', fechaNacimiento: '2024-04-18' };

  let mascota;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/mascotas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/mascotas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/mascotas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (mascota) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/mascotas/${mascota.id}`,
      }).then(() => {
        mascota = undefined;
      });
    }
  });

  it('Mascotas menu should load Mascotas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('mascota');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Mascota').should('exist');
    cy.url().should('match', mascotaPageUrlPattern);
  });

  describe('Mascota page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mascotaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Mascota page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/mascota/new$'));
        cy.getEntityCreateUpdateHeading('Mascota');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mascotaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/mascotas',
          body: mascotaSample,
        }).then(({ body }) => {
          mascota = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/mascotas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/mascotas?page=0&size=20>; rel="last",<http://localhost/api/mascotas?page=0&size=20>; rel="first"',
              },
              body: [mascota],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(mascotaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Mascota page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mascota');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mascotaPageUrlPattern);
      });

      it('edit button click should load edit Mascota page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Mascota');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mascotaPageUrlPattern);
      });

      it('edit button click should load edit Mascota page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Mascota');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mascotaPageUrlPattern);
      });

      it('last delete button click should delete instance of Mascota', () => {
        cy.intercept('GET', '/api/mascotas/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('mascota').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mascotaPageUrlPattern);

        mascota = undefined;
      });
    });
  });

  describe('new Mascota page', () => {
    beforeEach(() => {
      cy.visit(`${mascotaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Mascota');
    });

    it('should create an instance of Mascota', () => {
      cy.get(`[data-cy="nIdentificacionCarnet"]`).type('14927');
      cy.get(`[data-cy="nIdentificacionCarnet"]`).should('have.value', '14927');

      cy.get(`[data-cy="foto"]`).type('mid almost');
      cy.get(`[data-cy="foto"]`).should('have.value', 'mid almost');

      cy.get(`[data-cy="fechaNacimiento"]`).type('2024-04-18');
      cy.get(`[data-cy="fechaNacimiento"]`).blur();
      cy.get(`[data-cy="fechaNacimiento"]`).should('have.value', '2024-04-18');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        mascota = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', mascotaPageUrlPattern);
    });
  });
});
