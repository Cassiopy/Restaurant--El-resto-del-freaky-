
--
-- datos para la tabla mozos
--
INSERT INTO mozos VALUES (2000, 'Frodo Baggins', 'Laprida 112', 18921118);
INSERT INTO mozos VALUES (2005, 'Bilbo Baggins', 'Chacabuco 1980', 17498107);
INSERT INTO mozos VALUES (2110, 'Sam Gamgee', 'Maipu 345', 25152018);
INSERT INTO mozos VALUES (2115, 'Gandalf el Gris', 'Av Sucre 1324', 17247118);
INSERT INTO mozos VALUES (2120, 'Saruman el Blanco', 'Av Illia 247', 16901678);
INSERT INTO mozos VALUES (2125, 'Merry BrandybucK', 'Av Sucre 2724', 29276295);
INSERT INTO mozos VALUES (2130, 'Pippin Took', 'Don Bosco 1514', 29645938);
INSERT INTO mozos VALUES (2135, 'Aragorn, hijo de Arathorn', 'Balcarce 890', 29535884);
INSERT INTO mozos VALUES (2140, 'Legolas, hijo de Thranduil', 'Mitre 1674', 17528160);
INSERT INTO mozos VALUES (2145, 'Gimli, hijo de Glóin', 'Junin 1120', 16592570);
INSERT INTO mozos VALUES (2150, 'Sméagol', 'Av Sucre 1687', 29498143);
INSERT INTO mozos VALUES (2155, 'Tyrael', 'Maipu 254', 28095858);
INSERT INTO mozos VALUES (2160, 'Solid Snake', 'Mitre 324', 16823905);
INSERT INTO mozos VALUES (2165, 'Thomas A. Anderson', 'Ayacucho 490', 26476718);
INSERT INTO mozos VALUES (2170, 'Walter White', 'Belgrano 970', 25509663);
INSERT INTO mozos VALUES (2175, 'Jesse Pinkman', 'Pringles 352', 28382362);
INSERT INTO mozos VALUES (2180, 'Himura Kenshin', 'Pedernera 1324', 22371137);
INSERT INTO mozos VALUES (2185, 'Sanosuke Sagara', 'Ej de los Andes 1714', 25108623);
INSERT INTO mozos VALUES (2190, 'Saito Hajime', 'Rivadavia 567', 19636776);
INSERT INTO mozos VALUES (2195, 'Robert Garcia', 'Quintana 300', 27677382);
INSERT INTO mozos VALUES (2200, 'Deckard Cain', 'Belgrano 1324', 16454121);
INSERT INTO mozos VALUES (2205, 'Alan Turing', 'Ayacucho 2389', 19524908);

-- --------------------------------------------------------

--
-- datos para la tabla platos
--
INSERT INTO platos VALUES (80, 'Lasaña', 'Tan rica como la lasaña de la abuela', 'Plato Principal', 280, 405, 360);
INSERT INTO platos VALUES (90, 'Milanesas', 'Con carne de ternera de primera', 'Plato Principal', 315, 540, 345);
INSERT INTO platos VALUES (100, 'Tiramisú', 'Hecho con buen queso mascarpone', 'Postre', 150, 240, 210);
INSERT INTO platos VALUES (110, 'Canelones', 'Los canelones de toda la vida! con salsa blanca', 'Plato Principal', 330, 420, 390);
INSERT INTO platos VALUES (120, 'Kare Raisu', 'Arroz con curry, verduras y carne', 'Plato Principal', 180, 270, 255);
INSERT INTO platos VALUES (130, 'Lembas', 'Pan de Elfo', 'Plato Principal', 90, 900, 750);
INSERT INTO platos VALUES (140, 'Ramen', 'Sopa japonesa, con los mejores Narutomakis', 'Plato Principal', 180, 360, 300);
INSERT INTO platos VALUES (150, 'Manjuu', 'Bollos al vapor rellenas con anko (dulce)', 'Postre', 90, 150, 120);
INSERT INTO platos VALUES (160, 'Semillas Senzu', 'Semillas del Ermitaño, ideal para recargar pilas', 'Plato Principal', 30, 630, 570);
INSERT INTO platos VALUES (170, 'Chipá', 'Bollitos hechas con harina de mandioca y queso', 'Entrada', 15, 60, 45);
INSERT INTO platos VALUES (180, 'Okonomiyaki', 'Tortilla japonesa, hecha con repollo y fideos soba', 'Plato Principal', 270, 330, 315);
INSERT INTO platos VALUES (190, 'Ravioles', 'Con salsa boloñesa (receta italiana)', 'Plato Principal', 255, 480, 420);

-- --------------------------------------------------------

--
-- datos para la tabla mesas
--
INSERT INTO mesas VALUES (1, 'niños', 2000);
INSERT INTO mesas VALUES (2, 'niños', 2005);
INSERT INTO mesas VALUES (3, 'niños', 2110);
INSERT INTO mesas VALUES (4, 'karaoke', 2135);
INSERT INTO mesas VALUES (5, 'karaoke', 2170);
INSERT INTO mesas VALUES (6, 'karaoke', 2115);
INSERT INTO mesas VALUES (7, 'fumadores', 2115);
INSERT INTO mesas VALUES (8, 'fumadores', 2205);
INSERT INTO mesas VALUES (9, 'fumadores', 2205);
INSERT INTO mesas VALUES (10, 'fumadores', 2150);
INSERT INTO mesas VALUES (11, 'no fumadores', 2115);
INSERT INTO mesas VALUES (12, 'no fumadores', 2155);
INSERT INTO mesas VALUES (13, 'no fumadores', 2135);
INSERT INTO mesas VALUES (14, 'no fumadores', 2140);
INSERT INTO mesas VALUES (15, 'no fumadores', 2135);
INSERT INTO mesas VALUES (16, 'no fumadores', 2115);

-- --------------------------------------------------------

--
-- datos para la tabla consumos
--
INSERT INTO consumos VALUES (1, '2021/04/22', '21:00', 15);
INSERT INTO consumos VALUES (2, '2021/04/22', '20:30', 13);
INSERT INTO consumos VALUES (3, '2021/04/23', '22:00', 11);
INSERT INTO consumos VALUES (4, '2021/04/23', '21:00', 9);
INSERT INTO consumos VALUES (5, '2021/04/23', '22:15', 7);
INSERT INTO consumos VALUES (6, '2021/04/24', '23:00', 5);
INSERT INTO consumos VALUES (7, '2021/04/24', '22:30', 3);
INSERT INTO consumos VALUES (8, '2021/04/24', '21:00', 1);
INSERT INTO consumos VALUES (9, '2021/04/24', '23:30', 2);
INSERT INTO consumos VALUES (10, '2021/04/26', '22:00', 4);
INSERT INTO consumos VALUES (11, '2021/04/26', '20:30', 6);
INSERT INTO consumos VALUES (12, '2021/04/26', '22:15', 8);
INSERT INTO consumos VALUES (13, '2021/04/26', '23:00', 10);
INSERT INTO consumos VALUES (14, '2021/04/26', '20:30', 12);
INSERT INTO consumos VALUES (15, '2021/04/26', '23:00', 14);
INSERT INTO consumos VALUES (16, '2021/04/26', '23:30', 16);
INSERT INTO consumos VALUES (17, '2021/04/26', '23:30', 13);
INSERT INTO consumos VALUES (18, '2021/04/28', '21:00', 13);
INSERT INTO consumos VALUES (19, '2021/04/28', '23:00', 6);
INSERT INTO consumos VALUES (20, '2021/04/28', '22:15', 8);
INSERT INTO consumos VALUES (21, '2021/05/01', '20:30', 8);
INSERT INTO consumos VALUES (22, '2021/05/01', '22:30', 3);
INSERT INTO consumos VALUES (23, '2021/05/01', '21:00', 3);
INSERT INTO consumos VALUES (24, '2021/05/01', '22:00', 12);

-- --------------------------------------------------------

--
-- datos para la tabla se_consume
--
INSERT INTO se_consume VALUES (1, 100);
INSERT INTO se_consume VALUES (2, 90);
INSERT INTO se_consume VALUES (3, 120);
INSERT INTO se_consume VALUES (4, 160);
INSERT INTO se_consume VALUES (5, 90);
INSERT INTO se_consume VALUES (6, 110);
INSERT INTO se_consume VALUES (7, 80);
INSERT INTO se_consume VALUES (8, 130);
INSERT INTO se_consume VALUES (9, 80);
INSERT INTO se_consume VALUES (10, 170);
INSERT INTO se_consume VALUES (11, 140);
INSERT INTO se_consume VALUES (12, 160);
INSERT INTO se_consume VALUES (13, 130);
INSERT INTO se_consume VALUES (13, 150);
INSERT INTO se_consume VALUES (14, 130);
INSERT INTO se_consume VALUES (15, 160);
INSERT INTO se_consume VALUES (16, 130);
INSERT INTO se_consume VALUES (16, 100);
INSERT INTO se_consume VALUES (17, 140);
INSERT INTO se_consume VALUES (18, 100);
INSERT INTO se_consume VALUES (19, 190);
INSERT INTO se_consume VALUES (20, 90);
INSERT INTO se_consume VALUES (21, 180);
INSERT INTO se_consume VALUES (22, 80);
INSERT INTO se_consume VALUES (23, 180);
INSERT INTO se_consume VALUES (24, 80);

-- --------------------------------------------------------