# Portal de Notícias

Projeto full stack de um site de notícias, com back-end em **Spring Boot** (Java) e front-end em **React**. Usuários podem se cadastrar, publicar notícias, curtir notícias e comentários, e comentar nas notícias de outros usuários.

## Tecnologias utilizadas

**Back-end**
- Java + Spring Boot
- Spring JDBC (`JdbcTemplate`)
- Banco de dados **H2** (banco relacional em memória, já configurado no projeto — não precisa instalar nada separado)

**Front-end**
- React
- Vite
- Axios (requisições HTTP)

---

## Pré-requisitos

Antes de começar, verifique se você tem instalado:

- **IntelliJ IDEA** (Community ou Ultimate) — para rodar o back-end
- **JDK 17 ou superior** — necessário para compilar e rodar o projeto Java
- **Node.js** (versão 18 ou superior) e **npm** — para rodar o front-end

> O banco de dados é o **H2**, que roda em memória junto com a aplicação. Não é necessário instalar nem configurar nenhum banco externo — tudo já está pronto nos arquivos do projeto.

---

## 1. Configurando e rodando o Back-end (IntelliJ)

### Passo a passo

1. Abra o **IntelliJ IDEA**.
2. Clique em **File > Open** e selecione a pasta **NewsApp-Back** (a pasta raiz do projeto, onde você baixou/clonou ele — ela contém o arquivo `pom.xml`).
3. Aguarde o IntelliJ importar as dependências do Maven automaticamente (aparece uma barra de progresso no canto inferior direito). Se não importar sozinho, clique com o botão direito no `pom.xml` e selecione **Maven > Reload Project**.
4. Localize a classe principal do projeto (a que tem a anotação `@SpringBootApplication`, geralmente chamada `NoticiasIndividualApplication.java` ou similar).
5. Clique com o botão direito nela e selecione **Run** (ou clique no ícone de play verde ao lado do método `main`).

Não é necessário configurar nem instalar nenhum banco de dados separado — o **H2** já está configurado nos arquivos do projeto e sobe automaticamente junto com a aplicação, em memória.

> **Atenção:** por ser um banco em memória, os dados (usuários, notícias, comentários) são **apagados toda vez que o back-end é parado ou reiniciado**. Isso é esperado nesse ambiente de desenvolvimento.

6. Se tudo estiver certo, o console do IntelliJ vai mostrar algo como:

```
Tomcat started on port(s): 8080 (http)
Started NoticiasIndividualApplication in X seconds
```

O back-end estará rodando em **http://localhost:8080**.

### Testando se está no ar

Abra o navegador ou uma ferramenta como Postman/Insomnia e acesse:

```
GET http://localhost:8080/Noticias
```

Se retornar uma lista (mesmo que vazia, `[]`), o back-end está funcionando corretamente.

### Problemas comuns

| Erro | Causa provável |
|---|---|
| Porta 8080 já em uso | Outro processo já está usando a porta. Feche-o ou mude a porta em `application.properties` com `server.port=8081` (lembre de atualizar também a `baseURL` no front-end) |
| Lista de notícias sempre vazia após reiniciar o back-end | Comportamento esperado do H2: por ser um banco em memória, todos os dados são apagados a cada reinício da aplicação |
| Erro ao compilar / dependências não resolvidas | Rode **Maven > Reload Project** no IntelliJ, ou confirme que o JDK configurado no projeto (File > Project Structure) é a versão 17 ou superior |

---

## 2. Configurando e rodando o Front-end (npm)

### Passo a passo

1. Localize a pasta **NewsApp-Front** no seu computador (onde você baixou/clonou o projeto).
2. Abra um terminal **diretamente dentro dessa pasta**. Isso pode ser feito de algumas formas, dependendo do seu sistema:
   - **Windows**: abra a pasta `NewsApp-Front` no Explorador de Arquivos, clique com o botão direito em um espaço vazio dentro dela e selecione **Abrir no Terminal** (ou **Abrir janela do PowerShell aqui**, dependendo da versão do Windows).
   - **VS Code**: abra a pasta `NewsApp-Front` pelo menu **File > Open Folder**, depois abra o terminal integrado em **Terminal > New Terminal** (ele já abre na pasta certa automaticamente).
   - **Mac**: clique com o botão direito na pasta `NewsApp-Front` e, se tiver a opção habilitada, use **Novo Terminal na Pasta**.

   Não é necessário usar o comando `cd`, desde que o terminal já abra dentro da pasta `NewsApp-Front` — o que pode variar de local dependendo de onde você baixou o projeto.

3. Instale as dependências do projeto:

```bash
npm install
```

Esse comando vai ler o `package.json` e baixar todas as bibliotecas necessárias (React, Axios, React Router, lucide-react, etc.) para a pasta `node_modules`.

4. Confirme que a URL base da API está apontando para o back-end correto. Normalmente isso fica no arquivo `src/api/http.js`:

```javascript
const http = axios.create({
  baseURL: 'http://localhost:8080/Noticias',
})
```

Se o back-end estiver rodando em outra porta, ajuste esse valor.

5. Inicie o servidor de desenvolvimento:

```bash
npm run dev
```

6. O terminal vai mostrar algo como:

```
VITE vX.X.X  ready in XXX ms

➜  Local:   http://localhost:5173/
```

7. Abra o navegador em **http://localhost:5173** (ou a porta indicada no terminal) para acessar o site.

> **Importante:** o back-end precisa estar rodando (passo 1) **antes** de abrir o front-end, senão as notícias não vão carregar e vai aparecer mensagem de erro de conexão.

### Problemas comuns

| Erro | Causa provável / solução |
|---|---|
| `Uncaught SyntaxError: does not provide an export named ...` | Cache do Vite desatualizado. Rode `rm -rf node_modules/.vite` (ou apague a pasta manualmente no Windows) e depois `npm run dev` novamente |
| Página carrega mas nada aparece / erro de conexão | O back-end não está rodando, ou está em uma porta diferente da configurada em `http.js` |
| `npm: command not found` | O Node.js não está instalado ou não está no PATH do sistema. Baixe em [nodejs.org](https://nodejs.org) |
| Página em branco após `npm run dev` | Verifique o console do navegador (F12) para ver o erro específico |

---

## 3. Fluxo de uso da aplicação

1. **Cadastro**: crie uma conta informando nick, e-mail e senha.
2. **Login**: entre com o e-mail e senha cadastrados.
3. **Página inicial**: veja todas as notícias publicadas, curta e comente nelas.
4. **Adicionar notícia**: com uma conta logada, publique uma nova notícia (título, descrição e, opcionalmente, uma imagem de capa).
5. **Minhas notícias**: acompanhe as notícias que você publicou, o total de curtidas recebidas e edite ou exclua suas publicações.
6. **Curtidas e comentários**: clique no coração para curtir/descurtir uma notícia, e no ícone de comentário para abrir a notícia e ver/adicionar comentários.

---

## 4. Estrutura resumida do projeto

```
NewsApp-Back/               # Projeto Spring Boot (abrir no IntelliJ)
├── src/main/java/...       # Controllers, models
└── src/main/resources/
    └── application.properties

NewsApp-Front/               # Projeto React (rodar com npm)
├── src/
│   ├── api/                 # Funções de chamada à API (axios)
│   ├── components/          # Componentes reutilizáveis (cards, modal)
│   ├── context/              # Contexto de autenticação
│   ├── pages/                 # Páginas da aplicação
│   └── utils/                  # Funções auxiliares (formatação de data, etc.)
└── package.json
```

> As duas pastas (`NewsApp-Back` e `NewsApp-Front`) ficam lado a lado, cada uma com seu próprio projeto — não é necessário que estejam uma dentro da outra.

---

## 5. Ordem correta para rodar o projeto do zero

1. Abra o back-end no IntelliJ (pasta **NewsApp-Back**) e execute a classe principal (`Run`). O banco H2 sobe automaticamente, sem nenhuma configuração adicional.
2. Confirme que `http://localhost:8080/Noticias` responde (deve retornar `[]` ou uma lista de notícias).
3. Abra um terminal diretamente dentro da pasta **NewsApp-Front**.
4. Rode `npm install` (apenas na primeira vez ou quando novas dependências forem adicionadas).
5. Rode `npm run dev`.
6. Acesse `http://localhost:5173` no navegador.

> Lembre-se: como o H2 é em memória, toda vez que você reiniciar o back-end, vai precisar cadastrar usuário e notícias de novo para testar.
