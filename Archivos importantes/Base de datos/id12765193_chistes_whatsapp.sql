-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 14-04-2020 a las 00:29:39
-- Versión del servidor: 10.3.16-MariaDB
-- Versión de PHP: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `id12765193_chistes_whatsapp`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id_categoria` int(11) NOT NULL,
  `categoria` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `fecha` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id_categoria`, `categoria`, `fecha`) VALUES
(1, 'Niños', '2020-03-02 22:56:28'),
(2, 'Animales', '2020-03-02 22:56:28'),
(3, 'Bebés', '2020-03-02 22:56:28'),
(4, 'Borrachos', '2020-03-02 22:56:28'),
(5, 'Actos', '2020-03-13 18:04:22'),
(6, 'Amigos', '2020-03-13 18:04:22'),
(7, 'Colmos', '2020-03-13 18:04:22'),
(8, 'Comidas', '2020-03-13 18:04:22'),
(9, 'Computación', '2020-03-13 18:04:22'),
(10, 'Crueles', '2020-03-13 18:04:22'),
(11, 'Deportes', '2020-03-13 18:04:22'),
(12, 'Doctores', '2020-03-13 18:04:22'),
(13, 'En qué se parece... ?', '2020-03-13 18:04:22'),
(14, 'Jaimito', '2020-03-13 18:04:22'),
(15, 'Mamá..! Papá...!', '2020-03-13 18:04:22'),
(16, 'Hombres', '2020-03-13 18:04:22'),
(17, 'Mujeres', '2020-03-13 18:04:22'),
(18, 'Matrinonios', '2020-03-13 18:04:22'),
(19, 'Navidad', '2020-03-13 18:04:22'),
(20, 'No es lo mismo', '2020-03-13 18:04:22'),
(21, 'Novios', '2020-03-13 18:04:22'),
(22, 'Pepito', '2020-03-13 18:04:22'),
(23, 'Profesiones', '2020-03-13 18:04:22'),
(24, 'Profesores', '2020-03-13 18:04:22'),
(25, 'Qué le dice...?', '2020-03-13 18:04:22'),
(26, 'Restaurantes', '2020-03-13 18:04:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `chistes`
--

CREATE TABLE `chistes` (
  `id_chiste` int(10) UNSIGNED NOT NULL,
  `chiste` varchar(3000) COLLATE utf8_unicode_ci NOT NULL,
  `id_categoria` int(11) NOT NULL,
  `fecha` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `chistes`
--

INSERT INTO `chistes` (`id_chiste`, `chiste`, `id_categoria`, `fecha`) VALUES
(1, 'Papá quiero una mascota... Ya tuviste piojos el año pasado y los mataste', 1, '2020-03-02 22:58:57'),
(2, 'Entra una persona a comprar a la tienda: – Hola, quiero un desodorante. – OK, enseguida se lo traigo, ¿lo quiere de spray? – No, lo quiero de sobacos.', 1, '2020-03-02 23:01:14'),
(3, 'Tengo una cocinera que es como un sol, lo quema todo.', 1, '2020-03-02 23:02:53'),
(4, '¿Sabes que mi primo anda en bicicleta desde los 5 años? Asu apoco ?, ya debe estar bien lejos', 1, '2020-03-02 23:10:25'),
(5, '¿A qué no sabes lo que le dice un pollito a otro pollito cuando se enoja? ¡Caldito seas!', 2, '2020-03-02 23:10:25'),
(6, '¿Cómo se llama el hermano vegetariano de Bruce Lee? Broco Lee', 1, '2020-03-02 23:10:25'),
(7, 'Dios por favor permite que este calor derrita la grasa que hay en mi cuerpo', 1, '2020-03-02 23:10:25'),
(8, 'Una Maestra le pregunta a su alumno: – como se dice “perro” en inglés. – Esta muy facil, se dice “dog”. – ¡Excelente! ¿Y cómo se dice veterinario? – se dice “dog-tor”', 1, '2020-03-02 23:10:49'),
(9, 'Dios por favor permite que este calor derrita la grasa que hay en mi cuerpo', 1, '2020-03-02 23:11:29'),
(10, '¿Cómo se llama al elefante que viste de traje? \"Elegante\"', 1, '2020-03-02 23:11:30'),
(11, 'Mi hijo en su nuevo trabajo se siente como pez en el agua ¿Qué hace? Nada!!', 1, '2020-03-02 23:11:30'),
(12, 'Un niño le dijo a su papá papá papá vinieron a preguntar si ¿Aquí vendian un burro ? y ¿qué les dijiste ? que no estabas', 1, '2020-03-02 23:11:30'),
(13, 'Si mi perro me dice lava la ropa de todos modos no lo hare porque?  porque los perros no hablan', 2, '2020-03-02 23:11:30'),
(14, 'Un señor llega al doctor con su bebe en brazos. Doctor, doctor, mi hijo tiene 6 meses y no abre los ojos. El doctor le hace un chequeo al bebe y le dice al padre: Señor, el que debe abrir los ojos es usted, este bebe es chino.', 3, '2020-03-02 23:14:23'),
(15, 'Lamentamos tener que decirle Señor que su esposa ha tenido un parto muy difícil y al bebé le tuvimos que poner oxigeno. -Que pena porque yo quería ponerle Manuel!', 3, '2020-03-02 23:18:16'),
(16, 'Un borracho va a Alcohólicos Anónimos y le preguntan:\r\n- ”¿Vino solo?”.- No…con hielo por favor', 4, '2020-03-02 23:20:24'),
(17, 'Un borracho entra a su casa a las 3 de la mañana, y su esposa, que lo está esperando le dice:- ¡que no ves a la hora que vienes llegando!- ¿Y quién te dijo que vengo llegando?, vine a buscar mi guitarra.', 4, '2020-03-02 23:23:28'),
(18, '¿Tiene pastillas para la flojera? Sí ¿Me pone una en la boca por favor?', 1, '2020-03-06 20:20:59'),
(19, '¿Quién es la persona que más se ríe? El barrendero, porque siempre va riendo.', 1, '2020-03-06 20:43:04'),
(20, 'Buenos días. Busco trabajo. ¿Le interesa de jardinero? ¿Dejar dinero? ¡Si lo que busco es trabajo!', 1, '2020-03-08 03:09:20'),
(21, '¿Cuál es el único animal que muere entre aplausos?\nEl mosquito.', 2, '2020-03-23 06:09:08'),
(22, '¿Por qué el perro mueve la cola? \nPorque la cola no mueve al perro.', 2, '2020-03-23 06:19:12'),
(23, '¿Qué le dice un ganso a una gansa?\n- ¡Vengansa!', 2, '2020-03-23 06:23:46'),
(24, '¿Cuál es el animal que salta más que un árbol?\n¡Cualquier animal porque los árboles no saltan!', 2, '2020-03-23 06:30:18'),
(25, 'Dos patos hacen una carrera. ¿Cómo acaban?\n¡EMPATADOS!', 2, '2020-03-23 06:35:25'),
(26, '¿Qué hace un perro con un taladro?\nTaladrando.', 2, '2020-03-23 06:38:06'),
(27, '¿Qué haces?\n- Matando moscas. Por ahora van dos machos y dos hembras.\n- ¿Cómo sabes de qué sexo son?\n- Porque dos estaban en el vaso de cerveza y las otras dos en el espejo.', 2, '2020-03-23 06:43:50'),
(28, 'Qué bonito tu perro, ¿cómo se llama?\n- Le he puesto Wifi ¡porque se lo he robado al vecino!', 2, '2020-03-23 07:03:35'),
(29, 'Un granjero le dice a otro:\n- Oye, Paco, ¿tus vacas fuman?.\n- No, ¿por qué?\n- Entonces se te está quemando el establo.', 2, '2020-03-23 07:10:08'),
(30, '¿Cuál es el pez que usa corbata?\nEl pezcueso.', 2, '2020-03-23 07:15:08'),
(31, '¿Crees que la leche engorda?\n- ¡Hombre!, ¿no ves cómo están las vacas?', 2, '2020-03-23 07:22:15'),
(32, '¿Cuál es el pájaro que hace siempre sus nidos en las iglesias?\n- El Ave María.', 2, '2020-03-23 07:25:18'),
(33, '¿Qué le dijo una chinche a otra?\n- Te quiero chincheramente.', 2, '2020-03-23 07:28:50'),
(34, '¿Qué hace una abeja en el gimnasio?\nZumba.', 2, '2020-03-23 07:31:49'),
(35, 'Iban dos cerditos por la calle y ven un cartel en una carnicería que dice:\n¡HOY, DÍA DEL CERDO¡\n¡¡Y los dos cerditos se dieron un abrazo!!', 2, '2020-03-23 07:33:15'),
(36, 'Si tienes 5 peces y 3 se ahogan, ¿cuántos te quedan?\n- Mmmmmm…\n- Anda, deja de contar, ¡los peces no se ahogan!', 2, '2020-03-23 07:35:10'),
(37, '¿En qué idioma se hablan las tortugas?\nEn Tortugués.', 2, '2020-03-23 07:37:29'),
(38, '¿Cuál es el animal más antiguo?\n- La vaca.\n- ¿Por qué?\n- Porque está en blanco y negro.', 2, '2020-03-23 07:39:09'),
(39, 'Papá, ¿cuántos años tiene el gato?\n- 2 años.\n- ¿Y cuántos tengo yo?\n- 5 años.\n¿Y por qué el gato tiene bigote y yo no?', 2, '2020-03-24 03:30:40'),
(40, '¿Cómo se convierte un burro en burra?\nSe mete el burro en una habitación oscura y se espera hasta que se aburra.', 2, '2020-03-24 04:30:20'),
(41, 'Mamá, mamá ¿qué tienes en la barriga? ---Un bebé que me regaló tu papa. -¡Papá no le regales más bebes a mamá porque se los come!', 3, '2020-03-26 04:08:12'),
(42, '¿Qué le dice una taza a otra?\n¿Qué tazaciendo?', 25, '2020-03-26 04:19:53'),
(43, 'un niño le pregunta a su mama mama porque me llamo claudio y la mama dice:porque yo me llamo claudia y el niño le dice: ah pero que bueno que no te llamas ana.', 1, '2020-03-27 03:08:01'),
(44, 'Tía Teresa, ¿para qué te pintas?\n- Para estar más guapa.\n- ¿Y tarda mucho en hacer efecto?', 17, '2020-03-28 03:21:05'),
(45, 'Llega el niño de jugar al fútbol y le dice a su padre: – Hoy el entrenador me ha dicho que soy garantía de gol.\n– ¡Qué bien! Siempre quise tener un hijo delantero.\n– No, papá, jugué de portero.', 11, '2020-03-28 04:00:09'),
(46, 'La esposa de dice al marido:\n-Cariño, dame al bebé\n-Espera a que llore.\n-¿A que llore?. ¿Por qué?\n- Porque no lo encuentro', 3, '2020-04-05 02:51:57'),
(47, '- ¿Qué pesa más, bebé de 3 kilos o un pájaro de 3 kilos?\n- El pájaro. Porque pesa 3 kilos y pico…', 3, '2020-04-05 03:02:43');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `favoritos`
--

CREATE TABLE `favoritos` (
  `fecha_id_favorito` datetime NOT NULL,
  `id_chiste` int(11) UNSIGNED NOT NULL,
  `id_usuario` int(11) UNSIGNED NOT NULL,
  `id_boton_favorito_rojo` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `id_boton_favorito_normal` varchar(100) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `favoritos`
--

INSERT INTO `favoritos` (`fecha_id_favorito`, `id_chiste`, `id_usuario`, `id_boton_favorito_rojo`, `id_boton_favorito_normal`) VALUES
('2020-03-24 04:22:22', 39, 37, '1000039', '2000039'),
('2020-03-24 04:22:26', 39, 37, '1000039', '2000039'),
('2020-03-24 05:03:26', 32, 8, '1000032', '2000032'),
('2020-03-25 20:03:32', 40, 8, '1000040', '2000040'),
('2020-03-27 02:54:37', 42, 10, '1000042', '2000042'),
('2020-03-27 07:10:17', 39, 1, '1000039', '2000039'),
('2020-03-27 07:10:32', 43, 1, '1000043', '2000043'),
('2020-03-27 07:51:35', 42, 12, '1000042', '2000042'),
('2020-04-05 02:25:07', 45, 18, '1000045', '2000045'),
('2020-04-05 02:25:14', 44, 18, '1000044', '2000044'),
('2020-04-05 02:26:15', 43, 18, '1000043', '2000043'),
('2020-04-11 06:15:37', 43, 23, '1000043', '2000043'),
('2020-04-11 06:15:38', 20, 23, '1000020', '2000020'),
('2020-04-11 06:15:39', 19, 23, '1000019', '2000019'),
('2020-04-11 06:15:41', 18, 23, '1000018', '2000018'),
('2020-04-11 06:15:43', 12, 23, '1000012', '2000012'),
('2020-04-13 22:05:07', 28, 19, '1000028', '2000028'),
('2020-04-13 22:31:57', 40, 19, '1000040', '2000040');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int(10) UNSIGNED NOT NULL,
  `nombre` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `apellidos` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fecha` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `nombre`, `apellidos`, `fecha`) VALUES
(1, NULL, NULL, '2020-03-21 19:12:52'),
(2, NULL, NULL, '2020-03-21 19:22:56'),
(3, NULL, NULL, '2020-03-21 20:15:57'),
(4, NULL, NULL, '2020-03-21 20:20:17'),
(5, NULL, NULL, '2020-03-21 20:21:52'),
(6, NULL, NULL, '2020-03-21 20:53:00'),
(7, NULL, NULL, '2020-03-21 21:00:54'),
(8, NULL, NULL, '2020-03-23 06:15:50'),
(9, NULL, NULL, '2020-03-24 04:22:59'),
(10, NULL, NULL, '2020-03-27 02:52:14'),
(11, NULL, NULL, '2020-03-27 06:58:27'),
(12, NULL, NULL, '2020-03-27 07:50:33'),
(13, NULL, NULL, '2020-03-28 17:47:27'),
(14, NULL, NULL, '2020-03-30 03:32:11'),
(15, NULL, NULL, '2020-03-30 03:43:45'),
(16, NULL, NULL, '2020-03-30 04:14:11'),
(17, NULL, NULL, '2020-04-05 00:49:20'),
(18, NULL, NULL, '2020-04-05 00:52:21'),
(19, NULL, NULL, '2020-04-10 06:32:59'),
(20, NULL, NULL, '2020-04-10 06:38:56'),
(21, NULL, NULL, '2020-04-10 06:41:48'),
(22, NULL, NULL, '2020-04-10 06:54:10'),
(23, NULL, NULL, '2020-04-10 07:02:06'),
(24, NULL, NULL, '2020-04-11 22:36:51'),
(25, NULL, NULL, '2020-04-14 00:20:41');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id_categoria`);

--
-- Indices de la tabla `chistes`
--
ALTER TABLE `chistes`
  ADD PRIMARY KEY (`id_chiste`),
  ADD KEY `FK_chistes_categorias` (`id_categoria`);

--
-- Indices de la tabla `favoritos`
--
ALTER TABLE `favoritos`
  ADD PRIMARY KEY (`fecha_id_favorito`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id_categoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT de la tabla `chistes`
--
ALTER TABLE `chistes`
  MODIFY `id_chiste` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `chistes`
--
ALTER TABLE `chistes`
  ADD CONSTRAINT `FK_chistes_categorias` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
