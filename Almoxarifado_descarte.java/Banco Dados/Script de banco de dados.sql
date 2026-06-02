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


create table cargos
(
	codigo		int			not null primary key identity,
	descricao	varchar(50) not null
)
go
select*from cargos


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


create table funsAlmo
(
	pessoa_id		int		not null	primary key references pessoas,
	cargo_codigo	int		not null				references cargos
)
go
select *from funsAlmo


create table funsEmp
(
	pessoa_id		int	 not null primary key references pessoas,
	empresa_codigo	int	 not null			 references empresas
)
go
select * from funsEmp


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
select * from pessoas


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


create table produtos
(
	codigo			int			not null primary key identity,
	data_registro	datetime		null,
	descricao		varchar(50) not null,
	nr_patrimonio	int			not null
)
go
select * from produtos


create table produtos_notas
(
	produtos_codigo int not null references produtos, 
	notas_codigo	int not null references notas,
	qtd				int not null
)
go
select * from produtos_notas


create table produtos_retiradas
(
	produtos_codigo	 int not null references produtos,
	retiradas_codigo int not null references retiradas,
	qtd				 int not null -- not null/null??
)
go
select * from produtos_retiradas


create table produtos_entrada
(
	produtos_codigo int not null references produtos,
	entradas_codigo int not null references entradas
)
go
select * from produtos_entrada

-- no trabalho precisa ter o Join
-- vamos esperar mais algumas aulas para fazer essa parte