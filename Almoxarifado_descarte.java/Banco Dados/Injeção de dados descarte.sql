create database descarte
go

use descarte 
go 

create table notas
(
	codigo		int				not null primary key identity,
	obs			varchar(100)	not null, 
	data_hora	datetime			null 
)
go

select *from notas
insert into notas (obs, data_hora) values
('Nota de descarte 001', '20260110'),
('Nota de descarte 002', '20260115'),
('Nota de descarte 003', '20260120'),
('Nota de descarte 004', '20260125'),
('Nota de descarte 005', '20260130');


create table empresas 
(
	codigo	int			not null primary key identity,
	nome	varchar(50) not null,
	cnpj	varchar(15) not null, 
	fone	varchar(15) not null,
	email	varchar(50) not null
)
go
select * from empresas
insert into empresas (nome, cnpj, fone, email) values
('EcoRecicla'		, '11111111111111', '17999990001', 'contato@ecorecicla.com'),
('Verde Ambiental'	, '22222222222222', '17999990002', 'contato@verde.com'),
('Reciclagem Brasil', '33333333333333', '17999990003', 'contato@recbr.com'),
('SustentaTech'     , '44444444444444', '17999990004', 'contato@sustentatech.com'),
('EcoLog'           , '55555555555555', '17999990005', 'contato@ecolog.com');


create table cargos
(
	codigo		int			not null primary key identity,
	descricao	varchar(50) not null
)
go
select*from cargos
insert into cargos (descricao) values
('Gerente'),
('Supervisor'),
('Operador'),
('Analista'),
('Auxiliar');


create table pessoas
(
	id		int			not null primary key identity,
	nome	varchar(50) not null,
	cpf		varchar(15) not null, 
	fone	varchar(15) not null,
	email	varchar(50) not null
)
go
select*from pessoas
insert into pessoas (nome, cpf, fone, email) values
('Ana Silva'	 , '11111111111', '17999991111', 'ana@email.com'),
('Carlos Souza'	 , '22222222222', '17999992222', 'carlos@email.com'),
('Mariana Lima'	 , '33333333333', '17999993333', 'mariana@email.com'),
('João Pereira'	 , '44444444444', '17999994444', 'joao@gmail.com'),
('Fernando Costa', '55555555555', '17999995555', 'fernando@gmail.com');


create table funsAlmo
(
	pessoa_id		int		not null	primary key references pessoas,
	cargo_codigo	int		not null				references cargos
)
go
select *from funsAlmo
insert into funsAlmo (pessoa_id, cargo_codigo) values
(1,1),
(2,2),
(3,3),
(4,4),
(5,5);


create table funsEmp
(
	pessoa_id		int	 not null primary key references pessoas,
	empresa_codigo	int	 not null			 references empresas
)
go
select * from funsEmp
insert into funsEmp (pessoa_id, empresa_codigo) values
(1,1),
(2,2),
(3,3),
(4,4),
(5,5);


create table retiradas 
(
	codigo		int			not null primary key identity,
	data_hota	datetime		null, 
	obs			varchar(50) not null,
	status		varchar(50) not null, --conferir depois
	funsAlmo_id int			not null references pessoas,
	funsEmp_id	int			not null references pessoas 
)
go
select * from retiradas
insert into retiradas (data_hota, obs, status, funsAlmo_id, funsEmp_id) values
('20260201', 'Retirada monitor'	  , 'Concluida', 1, 2),
('20260205', 'Retirada teclado'	  , 'Pendente' , 2, 3),
('20260210', 'Retirada impressora', 'Concluida', 3, 1),
('20260625', 'Retirada gabinete'  , 'Concluida', 4, 4),
('20260215', 'Retirada mouse'     , 'Pendente' , 5, 5);


create table entradas
(
	codigo		int			not null primary key identity,
	data_hora	datetime		null,
	obs			varchar(50) not null,
	status		varchar(50) not null, --conferir depois
	funsAlmo_id int			not null references pessoas,
)
go
select * from entradas
insert into entradas (data_hora, obs, status, funsAlmo_id) values
('20260125', 'Entrada monitor'	 , 'Recebido', 1),
('20260128', 'Entrada teclado'	 , 'Recebido', 2),
('20260130', 'Entrada impressora', 'Recebido', 3),
('20260212', 'Entrada gabinete'  , 'Recebido', 4),
('20260205', 'Entrada mouse'     , 'Recebido', 5);


create table produtos
(
	codigo			int			not null primary key identity,
	data_registro	datetime		null,
	descricao		varchar(50) not null,
	nr_patrimonio	int			not null
)
go
select * from produtos
insert into produtos (data_registro, descricao, nr_patrimonio) values
('20260125', 'Monitor LCD'	, 1001),
('20260128', 'Teclado USB'	, 1002),
('20260130', 'Impressora HP', 1003),
('20260205', 'Gabinete Dell', 1004),
('20260208', 'Mouse Óptico' , 1005);


create table produtos_notas
(
	produtos_codigo int not null references produtos, 
	notas_codigo	int not null references notas,
	qtd				int not null
	primary key (produtos_codigo, notas_codigo)
)
go
select * from produtos_notas
insert into produtos_notas (produtos_codigo, notas_codigo, qtd) values
(1,3,5),
(2,3,10),
(3,3,2),
(4,4,3),
(5,5,8);


create table produtos_retiradas
(
	produtos_codigo	 int not null references produtos,
	retiradas_codigo int not null references retiradas,
	qtd				 int not null 
	primary key (produtos_codigo, retiradas_codigo)
)
go
select * from produtos_retiradas
insert into produtos_retiradas (produtos_codigo, retiradas_codigo, qtd) values
(1,1,1),
(2,2,2),
(3,3,1),
(4,4,1),
(5,5,2);


create table produtos_entrada
(
	produtos_codigo int not null references produtos,
	entradas_codigo int not null references entradas
	primary key (produtos_codigo, entradas_codigo)
)
go
select * from produtos_entrada
insert into produtos_entrada (produtos_codigo, entradas_codigo) values
(1,1),
(2,2),
(3,3),
(4,4),
(5,5);


-- JOIN --

-- Produtos e Notas
select
    p.descricao Descrição,
    n.obs Observações,
    pn.qtd Quantidade
from produtos p
inner join produtos_notas pn
    on p.codigo = pn.produtos_codigo
inner join notas n
    on n.codigo = pn.notas_codigo;

-- Funcionários e Cargos
select
    pe.nome,
    c.descricao as cargo
from pessoas pe
inner join funsAlmo fa
    on pe.id = fa.pessoa_id
inner join cargos c
    on c.codigo = fa.cargo_codigo;

-- Funcionários e Empresas
select
    pe.nome,
    e.nome as empresa
from pessoas pe
inner join funsEmp fe
    on pe.id = fe.pessoa_id
inner join empresas e
    on e.codigo = fe.empresa_codigo;

-- Retiradas com os Responsáveis
select
    r.codigo Código,
    r.obs Observações,
    r.status Status,
    p1.nome as funcionario_almoxarifado,
    p2.nome as funcionario_empresa
from retiradas r
inner join pessoas p1
    on p1.id = r.funsAlmo_id
inner join pessoas p2
    on p2.id = r.funsEmp_id;

-- Entradas e Funcionários
select
    e.codigo Código,
    e.obs Observações,
    e.status Status,
    p.nome Nome
from entradas e
inner join pessoas p
    on p.id = e.funsAlmo_id;
