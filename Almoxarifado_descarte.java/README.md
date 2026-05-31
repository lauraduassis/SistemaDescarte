# Sistema de Descarte

![Java](https://img.shields.io/badge/Java-17-orange)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-green)
![FATEC](https://img.shields.io/badge/FATEC-Rio%20Preto-blue)
![Licença](https://img.shields.io/badge/Licença-Acadêmica-lightgrey)

<p align="center">
  <img width="380" height="380" alt="SistemaDescarte" src="https://github.com/user-attachments/assets/a009bf6d-30e0-49bb-9a66-e4e2365ce454" />
</p>

## Sumário

- [Descrição do Projeto](#descrição-do-projeto)
- [Equipe de Desenvolvimento](#equipe-de-desenvolvimento)
- [Paradigma de Desenvolvimento](#paradigma-de-desenvolvimento)
- [Problema Solucionado](#problema-solucionado)
- [Fluxo de Funcionamento](#fluxo-de-funcionamento)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Objetivo Acadêmico](#objetivo-acadêmico)
- [Diagrama de Classe](#Diagrama-de-Classe)
- [Protótipo](#Protótipo)

## 📊Descrição do Projeto
<p align="justify">
    O Sistema de Descarte foi desenvolvido com o objetivo de otimizar o controle e o acompanhamento de produtos com defeitos ou que necessitam de descarte, garantindo rastreabilidade e eficiência em todas as etapas do processo.
    O sistema permite o gerenciamento completo desde a identificação do item no almoxarifado até sua coleta pela empresa responsável pelo descarte, mantendo registros detalhados de todas as movimentações realizadas.
</p>

## 👥Equipe de Desenvolvimento
<h3><strong>Grupo: Digital Solutions</strong></h3>
<img width="1251" height="452" alt="image" src="https://github.com/user-attachments/assets/1003c9ad-cb8e-41a8-b296-d7bf9b70288c" />

## 🔄Paradigma de Desenvolvimento
O projeto foi desenvolvido utilizando o modelo de ciclo de vida Cascata (Waterfall).
<h3><strong>As etapas seguidas foram:</strong></h3>

1. Levantamento e análise dos requisitos; 
2. Projeto e modelagem do sistema;
3. Implementação (codificação);
4. Testes e validação;
5. Manutenção e melhorias.
6. Implementação (codificação);

Essa abordagem foi escolhida por proporcionar uma estrutura organizada e adequada ao escopo do sistema.

## 🚨Problema Solucionado
O sistema busca resolver a falta de um controle eficiente, organizado e seguro no processo de descarte de equipamentos do setor de tecnologia.
<h3><strong>Principais problemas identificados:</strong></h3>

.  Falta de rastreabilidade dos equipamentos descartados;

.  Perda de informações durante o processo;

.  Erros em registros manuais;

.  Ausência de histórico confiável;

.  Dificuldade na emissão de relatórios de acompanhamento.

## 🔄Fluxo de Funcionamento
<h3><strong>Recebimento do Produto:</strong></h3>
Ao chegar ao almoxarifado, o produto é avaliado pelo funcionário responsável. Caso seja destinado ao descarte, ele é registrado no sistema.
<h3><strong>Registro para Descarte:</strong></h3>
O funcionário realiza:

.  Identificação do patrimônio;

.  Geração do código de barras;

.  Leitura do código através de leitor apropriado;

.  Importação automática das informações do sistema da Empro;

.  Registro do produto com status Pendente.

<h3><strong>Solicitação de Coleta:</strong></h3>

Quando a quantidade necessária de itens é atingida, o almoxarifado solicita a retirada para a empresa coletora responsável.
<h3><strong>Atualização de Status:</strong></h3>

Após a coleta:

.  O status do produto é atualizado;

.  A movimentação é registrada;

.  O histórico permanece disponível para consulta.

<h3><strong>Emissão de Relatórios:</strong></h3>

O sistema disponibiliza relatórios contendo:

.  Quantidade de produtos descartados;

.  Número de patrimônio;

.  Datas de movimentação;

.  Status dos itens;

.  Histórico de descarte.

## 📦Funcionalidades

<table>
    <tr>
        <th>Nº</th>
        <th>Funcionalidade</th>
        <th>Descrição</th>
    </tr>
    <tr>
        <td align="center">01</td>
        <td><strong>Cadastrar Produto</strong></td>
        <td>Permite incluir, alterar e consultar produtos.</td>
    </tr>
    <tr>
        <td align="center">02</td>
        <td><strong>Efetuar Descarte</strong></td>
        <td>Permite registrar solicitações de descarte dos produtos.</td>
    </tr>
    <tr>
        <td align="center">03</td>
        <td><strong>Emitir Relatório do Descarte</strong></td>
        <td>Gera relatórios dos descartes realizados em um período informado.</td>
    </tr>
</table>

## 🚀Tecnologias Utilizadas

.  Java

.  Banco de dados SQL Server 

.  Visual Studio Code

.  Git  

.  GitHub

## 📁Estrutura do Projeto
<ul>
    <li>
        <strong>Almoxarifado_descarte.java/</strong> - Diretório principal do projeto
        <ul>
            <li>
                <strong>.vscode/</strong> - Configurações do Visual Studio Code
                <ul>
                    <li><strong>settings.json</strong> - Configurações do ambiente</li>
                </ul>
            </li>
            <li>
                <strong>bin/</strong> - Arquivos compilados da aplicação
                <ul>
                    <li><strong>entities/</strong> - Classes compiladas das entidades</li>
                    <li><strong>App.class</strong> - Classe principal compilada</li>
                </ul>
            </li>
            <li>
                <strong>src/</strong> - Código-fonte do projeto
                <ul>
                    <li><strong>entities/</strong> - Classes de entidades do sistema</li>
                    <li><strong>App.java</strong> - Classe principal da aplicação</li>
                </ul>
            </li>
            <li>
                <strong>README.md</strong> - Documentação do projeto
            </li>
        </ul>
    </li>
</ul>

## 🎓Objetivo Acadêmico
<p align="justify">
Projeto desenvolvido para a disciplina de Engenharia de Software, aplicando conceitos de análise de requisitos, modelagem de sistemas, desenvolvimento de software e documentação técnica.
</p>
<h3><strong>FATEC</strong> São José do Rio Preto</h3>

## 🧩Diagrama de Classe
<img width="700" height="500" alt="image" src="https://github.com/user-attachments/assets/7dd809e1-b7cd-498e-849f-0b94f5b5bbb3" />

## 🎓 Instituição

Desenvolvido por **Digital Solutions** para a disciplina de Engenharia de Software.

**Faculdade de Tecnologia de São José do Rio Preto – FATEC Rio Preto**

2026
