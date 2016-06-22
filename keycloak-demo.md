# RH-SSO Demo

http://keycloak.example.test:8080/auth


http://localhost:8580/demo.html#/
http://localhost:8080/jboss-kitchensink/index.jsf


## Using Keycloak Docker Container

```
docker run \
 --name mynginx \
 -v /home/rsoares/dev/github/rafaeltuelho/rh-sso-keycloak-demo/projects/slides-demo-app:/usr/share/nginx/html:ro \
 -d -p 8580:80 \
 nginx


docker run \
 --privileged \
 --name keycloak-server \
 -h keycloak.example.test \
 -v /home/rsoares/var/docker-volumes/keycloak:/data \
 -e KEYCLOAK_USER=admin \
 -e KEYCLOAK_LOGLEVEL=DEBUG \
 -e KEYCLOAK_PASSWORD=jboss@123 \
 --link freeipa-server:ipa \
 jboss/keycloak
```

```

docker run \
 --privileged \
 --name sso-server \
 -h keycloak.example.test \
 -v /home/rsoares/var/docker-volumes/sso:/data \
 -e KEYCLOAK_USER=admin \
 -e KEYCLOAK_LOGLEVEL=DEBUG \
 -e KEYCLOAK_PASSWORD=jboss@123 \
 --link freeipa-server:ipa \
 rsoares/rh-sso:7.0

 docker run \
  --name eap-server \
  -h eap.example.test \
  -v /home/rsoares/var/docker-volumes/eap7/deployments:/opt/jboss/eap/standalone/deployments:rw \
  -e EAP_USER=admin \
  -e EAP_PASSWORD=jboss@123 \
  --link sso-server:sso-server \
  rsoares/jboss-eap:7.0.0
```

## Using OSE CDK
start vagrant box
```
cd $CDK_HOME
vagrant up
```

clone jboss ose templates
```
git clone https://github.com/jboss-openshift/application-templates.git

```

bootstrap your cdk to use RH-SSO
```
cd application-templates

oc create -f jboss-images-streams.json -n openshift

cd application-templates/sso

oc login --server=$OSE_MASTER

oc new-project rh-sso

oc create -n rh-sso -f ../secrets/sso-app-secret.json
secret "sso-app-secret" created

oc process -f sso70-basic.json | oc create -n rh-sso -f -

service "sso" created
service "secure-sso" created
service "sso-basic" created
route "sso" created
route "secure-sso" created
deploymentconfig "sso" created
```

start an web container to serve our JS client Demo App
```
docker run --name mynginx \
   -v /home/rsoares/Demos/Keycloak-RH-SSO/slides_rh-sso-keycloak/demo-app:/usr/share/nginx/html:ro \
   -d -p 8580:80 nginx
```

open the demo app: http://localhost:8580/demo.html

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


data-background="http://i.giphy.com/90F8aUepslB84.gif"

--

8. LDAP/Kerberos Integration
#For backward compatibility
ADMIN_USERNAME=${ADMIN_USERNAME:-${EAP_ADMIN_USERNAME:-eapadmin}}
ADMIN_PASSWORD=${ADMIN_PASSWORD:-$EAP_ADMIN_PASSWORD}
NODE_NAME=${NODE_NAME:-$EAP_NODE_NAME}
HTTPS_NAME=${HTTPS_NAME:-$EAP_HTTPS_NAME}
HTTPS_PASSWORD=${HTTPS_PASSWORD:-$EAP_HTTPS_PASSWORD}
HTTPS_KEYSTORE_DIR=${HTTPS_KEYSTORE_DIR:-$EAP_HTTPS_KEYSTORE_DIR}
HTTPS_KEYSTORE=${HTTPS_KEYSTORE:-$EAP_HTTPS_KEYSTORE}
SECDOMAIN_USERS_PROPERTIES=${SECDOMAIN_USERS_PROPERTIES:-${EAP_SECDOMAIN_USERS_PROPERTIES:-users.properties}}
SECDOMAIN_ROLES_PROPERTIES=${SECDOMAIN_ROLES_PROPERTIES:-${EAP_SECDOMAIN_ROLES_PROPERTIES:-roles.properties}}
SECDOMAIN_NAME=${SECDOMAIN_NAME:-$EAP_SECDOMAIN_NAME}
SECDOMAIN_PASSWORD_STACKING=${SECDOMAIN_PASSWORD_STACKING:-$EAP_SECDOMAIN_PASSWORD_STACKING}
```


docker run \
 --privileged \
 --name apacheds \
 -h ldap.example.com \
 -d \
 -v /home/rsoares/var/docker-volumes/apacheds:/var/lib/apacheds-2.0.0-M20:ro \
 h3nrik/apacheds

sudo docker run \
 --privileged \
 --name freeipa-server \
 -ti -h ipa.example.test \
 -e PASSWORD=Secret123 \
 -v /tmp/ipa-data:/data \
 -p 20088:88 -p 20088:88/udp \
 -p 29080:9080 -p 20389:389 \
 -p 28787:8787 -p 80:80 -p 443:443 \
 adelton/freeipa-server:centos-7

docker run \
 -ti --name eap7 -h eap7 \
 -e EAP_ADMIN_USERNAME=admin \
 -e EAP_ADMIN_PASSWORD=jboss@123 \
 -v /home/rsoares/var/docker-volumes/eap7/deployments:/deployments \
 registry.access.redhat.com/jboss-eap-7/eap70-openshift


Next steps:
	1. You must make sure these network ports are open:
		TCP Ports:
		  * 80, 443: HTTP/HTTPS
		  * 389, 636: LDAP/LDAPS
		  * 88, 464: kerberos
		  * 53: bind
		UDP Ports:
		  * 88, 464: kerberos
		  * 53: bind
		  * 123: ntp

	2. You can now obtain a kerberos ticket using the command: 'kinit admin'
	   This ticket will allow you to use the IPA tools (e.g., ipa user-add)
	   and the web user interface.
```
http://keycloak.example.test:8080/auth
	admin/jboss@123
http://keycloak.github.io/docs/userguide/keycloak-server/html/kerberos.html#d4e3009


https://github.com/adelton/docker-freeipa/tree/fedora-20
https://ipa.example.test/ipa/ui/
```
IPA Credentials
	admin/Secret123
criar registro 'A' (com reverso) no DNS do FreeIPA
	Network Services -> DNS -> DNS Zones -> example.test. -> Add
criar um Host no FreeIPDA
	Identity -> Hosts -> Actions -> Rebuild auto membership
		Refresh
		Add
criar o principal 'HTTP/keycloak.example.test@EXAMPLE.TEST'
	Identity > Services -> Add
		Service = HTTP
		Host = keycloak.example.test
kadmin.local
	addprinc -randkey HTTP/keycloak.example.test@EXAMPLE.TEST
	ktadd -k /tmp/http.keytab HTTP/keycloak.example.test@EXAMPLE.TEST

	copiar o keytab (/tmp/http.keytab) para o Keycloak Server
```


{ "AuthServerWhitelist": "*.example.org",
"AuthNegotiateDelegateWhitelist": "*.example.org" }

cat /etc/opt/chrome/policies/managed/redhat-corp.json

Liferay Portal 6.2
	http://localhost:8180/
		test@liferay.com/test

		1.1.1. Contrasting Identity Management with a Standard LDAP Directory

		https://access.redhat.com/documentation/en-US/Red_Hat_Enterprise_Linux/7/html/Linux_Domain_Identity_Authentication_and_Policy_Guide/introduction.html#comparing
