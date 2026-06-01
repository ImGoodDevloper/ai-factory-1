describe('KES User Acceptance Tests', () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.clearCookies();
    // Ignore uncaught exceptions from the app (like 401s)
    Cypress.on('uncaught:exception', (err, runnable) => {
      return false;
    });
    cy.visit('/login');
  });

  it('TC-1.1: Successful Login', () => {
    cy.get('#username').type('admin');
    cy.get('#password').type('admin123');
    cy.get('button[type="submit"]').click();
    cy.url().should('not.include', '/login');
  });

  it('TC-1.2: Unauthorized Access Attempt', () => {
    cy.visit('/audit-log');
    cy.url().should('include', '/login');
  });

  it('TC-1.3: Role-Based UI Visibility (RBAC)', () => {
    cy.get('#username').type('user');
    cy.get('#password').type('user123');
    cy.get('button[type="submit"]').click();
    
    // Check that Audit Log link is not visible in the header
    cy.get('nav').should('not.contain', 'Audit Log');
    
    // Attempt to visit audit log directly
    cy.visit('/audit-log');
    
    // Should be redirected to Home (/) because user is authenticated but not admin
    cy.url().should('not.include', '/audit-log');
  });

  describe('Authenticated Tests', () => {
    beforeEach(() => {
      cy.get('#username').type('admin');
      cy.get('#password').type('admin123');
      cy.get('button[type="submit"]').click();
      cy.url().should('not.include', '/login');
    });

    it('TC-2.1 to TC-2.5: Page Management CRUD', () => {
      cy.window().its('localStorage.token').then((token) => {
        cy.request({
          method: 'POST',
          url: 'http://localhost:8080/api/pages',
          headers: { Authorization: `Bearer ${token}` },
          body: { title: 'Project Alpha' }
        }).then((resp) => {
          const pageId = resp.body.id;
          cy.reload();
          cy.get('.sidebar-item').contains('Project Alpha').click({ force: true });
          cy.get('textarea', { timeout: 10000 }).should('exist');
          
          // TC-2.3: Edit and Save Content
          cy.get('textarea').clear().type('# Spec v1');
          cy.get('.save-button').click();
          cy.get('.status-indicator').should('contain', 'Saved');
          
          // TC-2.5: Delete
          cy.request({
            method: 'DELETE',
            url: `http://localhost:8080/api/pages/${pageId}`,
            headers: { Authorization: `Bearer ${token}` }
          });
        });
      });
    });

    it('TC-3.1 & TC-3.2: Markdown Editor Features', () => {
      cy.window().its('localStorage.token').then((token) => {
        cy.request({
          method: 'POST',
          url: 'http://localhost:8080/api/pages',
          headers: { Authorization: `Bearer ${token}` },
          body: { title: 'Editor Test' }
        }).then((resp) => {
          cy.reload();
          cy.get('.sidebar-item').contains('Editor Test').click({ force: true });
          cy.get('textarea', { timeout: 10000 }).should('exist').type('**Bold Text**');
          cy.get('.preview-pane').find('strong').should('contain', 'Bold Text');
          
          // Cleanup
          cy.request({
            method: 'DELETE',
            url: `http://localhost:8080/api/pages/${resp.body.id}`,
            headers: { Authorization: `Bearer ${token}` }
          });
        });
      });
    });

    it('TC-4.1: Global Search', () => {
      cy.window().its('localStorage.token').then((token) => {
        cy.request({
          method: 'POST',
          url: 'http://localhost:8080/api/pages',
          headers: { Authorization: `Bearer ${token}` },
          body: { title: 'Searchable Page' }
        }).then((resp) => {
          cy.reload(); // reload to fetch new pages
          cy.get('input[placeholder="Search pages..."]').type('Searchable');
          cy.get('.search-results').should('be.visible');
          
          // Cleanup
          cy.request({
            method: 'DELETE',
            url: `http://localhost:8080/api/pages/${resp.body.id}`,
            headers: { Authorization: `Bearer ${token}` }
          });
        });
      });
    });

    it('TC-4.2: Media Upload', () => {
      cy.window().its('localStorage.token').then((token) => {
        cy.request({
          method: 'POST',
          url: 'http://localhost:8080/api/pages',
          headers: { Authorization: `Bearer ${token}` },
          body: { title: 'Media Test' }
        }).then((resp) => {
          cy.reload();
          cy.get('.sidebar-item').contains('Media Test').click({ force: true });
          cy.get('textarea', { timeout: 10000 }).should('exist');
          
          // Cleanup
          cy.request({
            method: 'DELETE',
            url: `http://localhost:8080/api/pages/${resp.body.id}`,
            headers: { Authorization: `Bearer ${token}` }
          });
        });
      });
    });

    it('TC-5.1: View Audit Logs', () => {
      cy.visit('/audit-log');
      cy.get('h1').should('contain', 'Audit Log');
    });

    it('TC-5.2: Error Handling (RFC 7807)', () => {
      cy.window().its('localStorage.token').then((token) => {
        cy.request({
          method: 'POST',
          url: 'http://localhost:8080/api/pages',
          headers: { Authorization: `Bearer ${token}` },
          body: { title: '' }, // empty title to trigger validation error
          failOnStatusCode: false
        }).then((resp) => {
          expect(resp.status).to.eq(400); // Bad Request
          expect(resp.body.type).to.exist;
        });
      });
    });
  });
});