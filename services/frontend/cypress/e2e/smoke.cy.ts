describe('Smoke Test', () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.clearCookies();
  });

  it('should load the login page', () => {
    cy.visit('/login');
    cy.contains('Login').should('be.visible');
  });
});
