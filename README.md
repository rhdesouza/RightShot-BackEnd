# RightShot - BackEnd
_Api Right Shot Club_

##_Back-End_
###Softwares
- Spring Boot 2.5.0-SNAPSHOT
- MAVEN -> Gerenciador de Dependências
- JDK: 11
- Software: Intellij (Recomendado)
- Lombok
- Docker;
- Git;
- GitKraken;

###DevOps (Docker)
O projeto contem um docker-compose, para executalo será necessario o comando ```docker-compose up``` na raiz projeto;
Ambiente de Desenvolvimento:
 
    - Mysql (Banco de dados da aplicação);
    - SonarQube (Sonar da aplicação);
    - Postgres (Banco para armazenar os dados do SonarQube);
    - K6 (Ferramenta de testes de carga da aplicação);
    - grafana (Ferramenta gráfica para os testes do K6);    
    - influxDb (Armazena os dados extraidos do k6 e informados no grafana);

####Comandos docker:
    Iniciar o container: docker-compose up;

####Dicas SonarQube:
    Primeiro acesso o usuário e senha inicial será: admin.

    Comando para aumento de memória:
    wsl -d docker-desktop;
    sysctl -w vm.max_map_count=262144

###VARIAVEIS PARA PARAMETRIZAÇÃO (Environment)
####Configuração para Banco de Dados:
    datasource.url
    datasource.username
    datasource.password

####Configuração para envio de e-mail:
    mailSender.host
    mailSender.port
    mailSender.userName
    mailSender.password
    mailSender.encoding

####Configuração para rodar os testes heroku
    MAVEN_CUSTOM_OPTS=true

###Build Heroku
Deverá ser criado na raiz do projeto back-end, o arquivo system.properties contendo o apontamento da versão java para o heroku (java.runtime.version=11).
As variaveis de ambinete deverão ser parametrizadas no Heroku, caso contrário ele ira buscar as informações do aplication.properties;
O heroku deverá ser conectado com o repositório(github) para iniciar o deploy.

###SWAGGER
Remoto: ```ENDERECO_APLICACAO/swagger-ui.html```

Local: ```http://localhost:8080/swagger-ui.html#/```
____________________________________________________________________
##_FRONT-END_
###Softwares
 - VsCode;
 - Angular 11;
 - Angular Cli;
 - Git;
 - GitKraken;

###VARIAVEIS PARA PARAMETRIZAÇÃO (Environment)
Configurar os arquivos enviroment.ts para cada ambiente.

###Build Heroku
O heroku deverá ser conectado com o repositório(github) para iniciar o deploy.
