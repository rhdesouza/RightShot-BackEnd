# RightShot-BackEnd
Api Right Shot Club

Spring Boot 2.5.0-SNAPSHOT
MAVEN -> Gerenciador de Dependências
JDK: 11
Software: Eclipse Luna
Lombok

##Outras
Git -> Versionador de código
Software: GitKraken

##Publicação Heroku
HEROKU CLI -> Para publicação no Heroku
Maiores instruções acesse o site do heroku para aplicação.
Deverá ser criado na raiz do projeto back-end, o arquivo system.properties contendo o apontamento da versão java para o heroku (java.runtime.version=11).
____________________________________________________________________
#BUILD FRONT-END
$ ng build --prod
Copiar o conteudo da pasta guit para pasta src/main/resources/static do BACK-END
____________________________________________________________________
#BUILD BACK-END
Alterar as confirações de conexão com o banco de dados em applications.properties (Contém o código comentado para publicação no heroku).
Para cosultar as novas configurações acesse o heroku->rightshot->Overview->Add-ons(Banco)->Connection Info

Após o Build do FRONT-END, executar o comando:
$ maven install

Executar o arquivo .jar gerado na pasta target para teste.
____________________________________________________________________
#VARIAVEIS PARA PARAMETRIZAÇÃO (Environment)
#Configuração para Banco de Dados:
datasource.url
datasource.username
datasource.password

#Configuração para envio de e-mail:
mailSender.host
mailSender.port
mailSender.userName
mailSender.password
mailSender.encoding

____________________________________________________________________
#DEPLOY HEROKU (Necessário Heroku CLI)

Se você ainda não o fez, faça login na sua conta Heroku e siga as instruções para criar uma nova chave pública SSH.
$ heroku login

Clonar o repositório (Somente a primeira vez)
Use Git para clonar o código-fonte do rocky-spire-84672 na sua máquina local.
$ heroku git: clone -a rocky-spire-84672 
$ cd rocky-spire-84672

Implante suas alterações
Faça algumas alterações no código que você acabou de clonar e implante-o no Heroku usando o Git.
$ git add.
$ git commit -am "melhora"
$ git push heroku master

#Deverá ser criado na raiz do projeto back-end, o arquivo system.properties contendo o apontamento da versão java para o heroku (java.runtime.version=11).
____________________________________________________________________
#SWAGGER
ENDERECO_APLICACAO/swagger-ui.html
http://localhost:8080/swagger-ui.html#/
____________________________________________________________________
#Comandos
## DEPLOY DO WAR SEM TESTES
$ mvn clean package -DskipTests

## LOGS HEROKU
$ heroku logs -t

____________________________________________________________________


