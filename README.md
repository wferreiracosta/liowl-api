# Liowl-API

[![Build Status](https://www.travis-ci.com/wferreiracosta/liowl-api.svg?branch=master)](https://www.travis-ci.com/wferreiracosta/liowl-api)
[![codecov](https://codecov.io/gh/wferreiracosta/liowl-api/branch/master/graph/badge.svg?token=QOS2K7TO82)](https://codecov.io/gh/wferreiracosta/liowl-api)

A API Liowl foi desenvolvida para realizar o controle de aluguel de livros. Ela realiza diversas atividades que seriam necessárias para o estabelecimento como cadastrar livros e pegar livros emprestados.

## About

Este projeto faz parte do meu portfólio pessoal, ficarei feliz se você pudesse me fornecer algum feedback sobre o projeto, código e estrutura.

## Getting Started
### Installing

**Cloning the Repository**

```
$ git clone https://github.com/wferreiracosta/liowl-api.git

$ cd liowl-api
```
### Routes

**Base URL**
```
http://localhost:8080/api/
```

**Swagger**

```
http://localhost:8080/swagger-ui.html
```

**Book Routes**

| Método |                                       | Content                                                | Success          | Error | Observação |
|--------|---------------------------------------|--------------------------------------------------------|------------------|-------|------------|
| POST   | /api/books                            | {"title":"string", "author":"string", "isbn":"string"} | CREATED (201)    |       |            |
| PUT    | /api/books/{id}                       | {"title":"string", "author":"string", "isbn":"string"} | OK (200)         |       |            |
| GET    | /api/books/{id}                       |                                                        | OK (200)         |       |            |
| GET    | /api/books?title=''&author=''&isbn='' |                                                        | OK (200)         |       |            |
| DELETE | /api/books/{id}                       |                                                        | NO CONTENT (204) |       |            |