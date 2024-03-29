# Simples Dental - Profissionais
Projeto para o teste técnico de Desenvolvedor Back-End na Simples Dental.

## Descrição
Este projeto é uma aplicação back-end desenvolvida em Java com Spring Boot e PostgreSQL. A aplicação gerencia profissionais e seus contatos, permitindo operações CRUD (Create, Read, Update, Delete) por meio de uma API RESTful.

## Autor
- [@rafaelbraf](https://github.com/rafaelbraf)

## Pré-Requisitos
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

## Rodando localmente
Clone o projeto
```bash
  git clone https://github.com/rafaelbraf/simples_dental.git
```

Entre no diretório do projeto
```bash
  cd simples_dental
```

Execute o docker-compose
```bash
  docker-compose up -d
```
- Aguarde até que todos os serviços estejam em execução.
- Acesse a API em http://localhost:8080/
- Acesse o Swagger-UI em http://localhost:8080/swagger-ui/index.html


## Documentação da API

### Profissional

#### Lista de profissionais com base nos critérios definidos nos parâmetros
```http
  GET /profissionais
```
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `q` | `string` | **Obrigatório** Filtro para buscar profissionais que contenham o texto em qualquer um de seus atributos |
| `fields` | `list<string>` | **Opcional** Quando presente apenas os campos listados em fields deverão ser retornados |

#### Retorna profissional por id
```http
  GET /profissionais/{id}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `id`      | `long` | **Obrigatório** O ID do Profissional que você quer |

#### Insere no banco de dados os dados do profissional enviados via body
```http
  POST /profissionais
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `nome`      | `string` | **Obrigatório** Nome do Profissional |
| `cargo`     | `enum` | **Obrigatório** Cargo do Profissional |
| `nascimento`     | `date` | **Obrigatório** Data de Nascimento do Profissional |

#### Atualiza os dados do profissional que possui o ID passado via URL com os dados enviados
```http
  PUT /profissionais/{id}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `id`      | `long` | **Obrigatório** ID do Profissional |
| `nome`      | `string` | **Obrigatório** Nome do Profissional |
| `cargo`     | `enum` | **Obrigatório** Cargo do Profissional |
| `nascimento`     | `date` | **Obrigatório** Data de Nascimento do Profissional |

#### Deleta o profissional de forma lógica
```http
  DELETE /profissionais/{id}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `id`      | `long` | **Obrigatório** ID do Profissional |

### Contato

#### Lista de contatos com base nos critérios definidos em Params
```http
  GET /contatos
```
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `q` | `string` | **Obrigatório** Filtro para buscar contatos que contenham o texto em qualquer um de seus atributos |
| `fields` | `list<string>` | **Opcional** Quando presente apenas os campos listados em fields deverão ser retornados |

#### Retorna contato por id
```http
  GET /contatos/{id}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `id`      | `long` | **Obrigatório** O ID do Contato que você quer |

#### Insere no banco de dados os dados do contato enviados via body
```http
  POST /contatos
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `nome`      | `string` | **Obrigatório** Nome do Contato |
| `profissional`     | `profissional` | **Obrigatório** Profissional pai do Contato |

#### Atualiza os dados do contato que possui o ID passado via URL com os dados enviados
```http
  PUT /contatos/{id}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `nome`      | `string` | **Obrigatório** Nome do Contato |

#### Deleta o contato
```http
  DELETE /contatos/{id}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `id`      | `long` | **Obrigatório** ID do Contato |