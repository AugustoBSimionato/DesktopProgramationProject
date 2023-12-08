--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

-- Started on 2023-11-08 09:42:52 -03

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 16445)
-- Name: employees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employees (
    id integer,
    nomecompleto character varying(100),
    cpf character varying(300),
    endereco character varying(300),
    cep character varying(300),
    telefone character varying(300),
    contratacao character varying(100),
    demissao character varying(100)
);


ALTER TABLE public.employees OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16435)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    id integer,
    nomecompleto character varying(100),
    endereco character varying(300),
    cep character varying(300),
    descricaopedido character varying(300),
    tipopedido character varying(300)
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16432)
-- Name: pizzas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pizzas (
    id integer,
    nomepizza character varying(100),
    descricao character varying(300)
);


ALTER TABLE public.pizzas OWNER TO postgres;

--
-- TOC entry 3595 (class 0 OID 16445)
-- Dependencies: 217
-- Data for Name: employees; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employees (id, nomecompleto, cpf, endereco, cep, telefone, contratacao, demissao) FROM stdin;
2	Maria Silva	987.654.321-00	Avenida dos Trabalhadores, 456	98765-432	(987) 555-3210	2022-11-20	2023-02-28
3	João Oliveira	111.222.333-00	Praça dos Colaboradores, 789	54321-876	(111) 555-3333	2022-09-10	
4	Ana Ferreira	444.555.666-00	Rua do Emprego, 101	11111-111	(444) 555-6666	2023-03-01	
5	Pedro Almeida	777.888.999-00	Avenida do Temporário, 222	22222-222	(777) 555-9999	2023-04-15	2023-05-31
6	Laura Pereira	333.444.555-00	Rua dos Colaboradores, 333	33333-333	(333) 555-4444	2022-08-05	2023-01-15
7	Gustavo Oliveira	555.666.777-00	Avenida do Recrutamento, 444	44444-444	(555) 555-7777	2022-06-20	
8	Camila Silva	777.888.999-00	Rua da Equipe, 555	55555-555	(777) 555-8888	2023-02-10	
9	Miguel Santos	999.000.111-00	Avenida da Contratação, 666	66666-666	(999) 555-1111	2023-05-01	
10	Isabel Ferreira	111.222.333-00	Praça do Meio Período, 777	77777-777	(111) 555-3333	2023-03-10	2023-06-30
1	Carlos Santos	123.456.789-00	Rua dos Funcionários, 123	12345-678	(123) 555-7890	2023-01-15	
\.


--
-- TOC entry 3594 (class 0 OID 16435)
-- Dependencies: 216
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (id, nomecompleto, endereco, cep, descricaopedido, tipopedido) FROM stdin;
2	Maria Santos	Avenida da Margherita, 456	98765-432	Pizza de Margherita, tamanho grande, sem azeitonas.	Delivery
3	Carlos Oliveira	Travessa da Calabresa, 789	54321-876	Pizza de Calabresa e Pizza de Frango com Catupiry, tamanho médio.	Retirada no local
4	Ana Ferreira	Beco da Pizza Vegetariana, 101	11111-111	Pizza Vegetariana, tamanho médio, com molho picante.	Delivery
5	Pedro Almeida	Rua das Especiarias, 222	22222-222	Pizza personalizada: Molho de tomate, queijo mussarela, pepperoni, cogumelos.	Delivery
6	Isabel Santos	Rua das Delícias, 333	33333-333	Pizza de Quatro Queijos, tamanho médio, com borda recheada de catupiry.	Delivery
7	Miguel Oliveira	Avenida Gourmet, 444	44444-444	Pizza Trufada, tamanho grande, com vinho tinto.	Delivery
8	Laura Pereira	Rua das Pimentas, 555	55555-555	Pizza de Jalapeño, tamanho médio, com molho picante extra.	Retirada no local
9	Gustavo Silva	Rua da Festa, 666	66666-666	10 Pizzas de Calabresa e 5 Pizzas de Frango com Catupiry para festa.	Entrega em lote
10	Camila Ferreira	Rua das Plantas, 777	77777-777	Pizza de Abóbora e Pizza Vegetariana, tamanho médio.	Delivery
1	João Silva	Rua das Pizzas, 123	12345-678	Pizza de Pepperoni, tamanho médio, extra queijo.	Delivery
\.


--
-- TOC entry 3593 (class 0 OID 16432)
-- Dependencies: 215
-- Data for Name: pizzas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pizzas (id, nomepizza, descricao) FROM stdin;
2	Pizza de Margherita	Uma clássica pizza com molho de tomate, mussarela, manjericão e azeite de oliva.
3	Pizza de Frango com Catupiry	Uma pizza cremosa com frango desfiado e Catupiry.
4	Pizza de Calabresa	Uma pizza com calabresa picante e cebolas fatiadas.
5	Pizza Vegetariana	Uma pizza repleta de legumes frescos e queijo vegano.
6	Pizza de Chocolate	Uma pizza de sobremesa coberta com chocolate derretido e frutas.
7	Pizza de Quatro Queijos	Uma pizza com uma mistura de quatro queijos diferentes.
8	Pizza Trufada	Uma pizza gourmet com trufas, cogumelos e queijo ricota.
9	Pizza de Jalapeño	Uma pizza com jalapeños picantes e queijo cheddar.
10	Pizza de Carne Moída	Uma pizza com carne moída temperada e pimentões.
11	Pizza de Camarão com Abacaxi	Uma pizza com camarões suculentos e abacaxi doce.
12	Pizza Sem Glúten	Uma pizza sem glúten com ingredientes saudáveis.
13	Pizza de Frutos do Mar	Uma pizza com uma variedade de frutos do mar e molho branco.
14	Pizza de Presunto e Cogumelos	Uma pizza clássica com presunto e cogumelos fatiados.
15	Pizza de Churrasco	Uma pizza com carne de churrasco desfiada e cebolas caramelizadas.
16	Pizza de Abóbora	Uma pizza vegetariana com abóbora assada e queijo de cabra.
1	Pizza de Pepperoni	Uma deliciosa pizza com pepperoni e queijo derretido.
\.


-- Completed on 2023-11-08 09:42:53 -03

--
-- PostgreSQL database dump complete
--

