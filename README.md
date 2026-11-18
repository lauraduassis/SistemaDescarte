# Sistema de Descarte

Aplicação web para controlar o estoque e o descarte de equipamentos e materiais de TI de um almoxarifado.

## Funcionalidades

- Cadastro e edição de produtos por patrimônio, com quantidade de entrada, código de barras e etiquetas em PDF.
- Solicitação de descarte, confirmação da coleta por empresa responsável e baixa da quantidade coletada.
- Histórico de entradas, retiradas e descartes e relatório de movimentação de estoque em PDF.
- Cadastro de empresas coletoras com máscara de telefone e validação de telefone e e-mail.
- Login, gestão de usuários e permissões e edição do perfil.

O saldo físico é calculado pelas entradas menos as retiradas. As solicitações de descarte podem ser consultadas no histórico.

## Requisitos

- JDK 21 e Maven.
- Node.js compatível com Vite 8 (20.19+ na linha 20 ou 22.12+ a partir da linha 22) e npm.
- Microsoft SQL Server com a base e o esquema do Sistema de Descarte já preparados.

## Executar localmente

Configure as credenciais do SQL Server na sessão do terminal. Exemplo para PowerShell:

```powershell
$env:SPRING_DATASOURCE_URL = 'jdbc:sqlserver://localhost:1433;databaseName=sistema_descarte;encrypt=true;trustServerCertificate=true'
$env:SPRING_DATASOURCE_USERNAME = 'seu_usuario'
$env:SPRING_DATASOURCE_PASSWORD = 'sua_senha'
```

Use o nome real da sua base. A opção de confiar no certificado do servidor no exemplo é para desenvolvimento local. Por padrão, a aplicação usa o esquema existente, com a geração automática de DDL desativada.

```powershell
npm ci
npm run build
mvn spring-boot:run
```

Abra [http://localhost:8080](http://localhost:8080/) e entre com um usuário já cadastrado na base.

## Verificações e empacotamento

```powershell
npm run build
mvn test
mvn package
```

O Maven gera o arquivo WAR em `target/`.

## Estrutura

| Caminho | Conteúdo |
| --- | --- |
| `src/main/java` | Aplicação Spring Boot, controladores, serviços e repositórios |
| `src/main/resources/templates` | Páginas e componentes Thymeleaf |
| `src/main/resources/js` | Componentes Alpine.js |
| `src/main/resources/css` | Estilos de origem |
| `src/main/resources/static` | Recursos estáticos, incluindo os arquivos gerados pelo Vite |
