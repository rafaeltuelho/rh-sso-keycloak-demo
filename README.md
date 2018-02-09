# RH-SSO Demo

http://keycloak.example.test:8080/auth


http://localhost:8580
http://localhost:8080/jboss-kitchensink/index.jsf


## Using Keycloak Docker Container

Open the demo app: http://localhost:8580/demo.html

1. Config
  * SSO Server URL: http://sso-rh-sso.cdk.vm/auth
  * Save
  * SSO Admin

2. Criar Realm
  * Config
   * SSO Realm: demo
   * Display Name: RH-SSO
   * HTML Display name: <strong>SAs Brasil</strong> RH-SSO Demo Tech Talk
   * Save

3. Criar App (client)
 * Client Id: demo-app
 * Root URL: http://localhost:8580
 * Config
   * App Client Id: demo-app
   * Save

4. Criar Roles
 * Role Name: realm-default-role
 * Save

 * Clients
    * demo-app
    * Roles
      * Role Name: app-default-role

5. Criar Usuário
 * Username: tuelho
 * Save

 * Tentar logar!
 * Update pass
 * Tentar novamente!
  * Update Pass

6. Habilitar Registro

7. Habilitar Social Login com GitHub

 * https://github.com/settings/profile
 * https://github.com/settings/applications
 * https://github.com/settings/developers