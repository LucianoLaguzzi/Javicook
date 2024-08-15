-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-08-2024 a las 16:23:40
-- Versión del servidor: 10.4.17-MariaDB
-- Versión de PHP: 8.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `recetas`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comentario`
--

CREATE TABLE `comentario` (
  `id` bigint(20) NOT NULL,
  `estado` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fecha_baja` datetime DEFAULT NULL,
  `fecha_estado` datetime DEFAULT NULL,
  `comentario` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FECHA` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receta_id` bigint(20) DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `comentario`
--

INSERT INTO `comentario` (`id`, `estado`, `fecha_baja`, `fecha_estado`, `comentario`, `FECHA`, `receta_id`, `usuario_id`) VALUES
(1, 'A', NULL, '2024-05-20 16:32:47', 'Hola! que buenos', '20/05/2024 16:32', 54, 1),
(2, 'A', NULL, '2024-05-21 17:30:03', 'Muy buena la voy a probar! Que harina me recomiendas?', '21/05/2024 17:30', 61, 1),
(3, 'A', NULL, '2024-06-03 15:22:44', 'Que buenos!!', '03/06/2024 15:22', 71, 1),
(4, 'A', NULL, '2024-06-03 16:31:30', 'Los voy a probar!!', '03/06/2024 16:31', 71, 22),
(7, 'A', NULL, '2024-06-25 20:29:19', 'Prueba comentario a borrar', '25/06/2024 20:29', 103, 22),
(8, 'A', NULL, '2024-06-26 16:26:21', 'Estoy probando el comentario!', '26/06/2024 16:26', 103, 1),
(9, 'A', NULL, '2024-06-26 16:28:59', 'Probando email en comentario!', '26/06/2024 16:28', 103, 20),
(10, 'A', NULL, '2024-06-26 16:36:55', 'prueba email numero 2', '26/06/2024 16:36', 103, 20),
(11, 'A', NULL, '2024-07-04 11:58:12', 'Que bueno que prueben!', '04/07/2024 11:58', 103, 22),
(12, 'A', NULL, '2024-07-15 12:54:41', 'Probando el cambio de email!', '15/07/2024 12:54', 103, 41),
(13, 'A', NULL, '2024-07-31 16:01:03', 'Si!!', '31/07/2024 16:01', 61, 41);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ingrediente`
--

CREATE TABLE `ingrediente` (
  `ID` int(11) NOT NULL,
  `NOMBRE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `estado` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fecha_baja` datetime DEFAULT NULL,
  `fecha_estado` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `ingrediente`
--

INSERT INTO `ingrediente` (`ID`, `NOMBRE`, `estado`, `fecha_baja`, `fecha_estado`) VALUES
(16, ' sal', 'A', NULL, '2024-04-16 15:13:18'),
(18, ' agua', 'A', NULL, '2024-04-16 15:52:09'),
(19, 'harina', 'A', NULL, '2024-04-17 12:07:08'),
(20, ' papa', 'A', NULL, '2024-04-17 12:07:18'),
(22, 'sal', 'A', NULL, '2024-04-18 20:35:37'),
(23, ' pimienta', 'A', NULL, '2024-04-18 20:59:30'),
(24, ' harina', 'A', NULL, '2024-05-21 15:18:46'),
(25, ' polvo de hornear', 'A', NULL, '2024-05-21 15:18:50'),
(26, 'azucar', 'A', NULL, '2024-06-11 16:53:43'),
(27, 'asd', 'A', NULL, '2024-06-13 16:37:20'),
(28, 'levadura', 'A', NULL, '2024-07-15 16:21:19'),
(29, 'agua', 'A', NULL, '2024-07-29 11:04:36'),
(30, 'avena', 'A', NULL, '2024-08-08 18:10:01');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pasos_receta`
--

CREATE TABLE `pasos_receta` (
  `RECETA_ID` bigint(20) DEFAULT NULL,
  `PASOS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id` bigint(20) DEFAULT NULL,
  `estado` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fecha_baja` datetime DEFAULT NULL,
  `fecha_estado` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `pasos_receta`
--

INSERT INTO `pasos_receta` (`RECETA_ID`, `PASOS`, `id`, `estado`, `fecha_baja`, `fecha_estado`) VALUES
(51, 'Hacer los ñoquis\r\nServirlos', 0, '', NULL, NULL),
(52, 'Cantidad 1\r\nCantidad 2', 0, '', NULL, NULL),
(54, 'ingrediente 1\r\ningrediente 2', 0, '', NULL, NULL),
(55, 'paso1\r\npaso2', 0, '', NULL, NULL),
(56, 'paso2', 0, '', NULL, NULL),
(57, 'paso 3', 0, '', NULL, NULL),
(58, 'paso 4', 0, '', NULL, NULL),
(59, 'paso 5', 0, '', NULL, NULL),
(60, 'paso 6', 0, '', NULL, NULL),
(61, 'paso lazy', 0, '', NULL, NULL),
(69, 'paso error\r\npaso error2', NULL, NULL, NULL, NULL),
(70, 'Colocar lo seco en un bowl\r\nMezclar y formar una pasta\r\nEnmantecar una bandeja y llevarlos al horno', NULL, NULL, NULL, NULL),
(71, 'Armar el panqueque ponerle dulce de leche y comerlo', NULL, NULL, NULL, NULL),
(103, 'Paso 1 edicion\r\nPaso 2 edicionn', NULL, NULL, NULL, NULL),
(104, 'Paso edit 5\nPaso edit 6\n', NULL, NULL, NULL, NULL),
(107, 'paso nombre imagen', NULL, NULL, NULL, NULL),
(108, 'Paso nombre imagen 2', NULL, NULL, NULL, NULL),
(111, 'asd\r\ndsa', NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `receta`
--

CREATE TABLE `receta` (
  `ID` int(11) NOT NULL,
  `TITULO` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PASOS` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IMAGEN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FECHA` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ID_USUARIO` int(11) NOT NULL,
  `CATEGORIA` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALORACION` int(11) DEFAULT NULL,
  `DIFICULTAD` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TIEMPO_PREPARACION` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `INGREDIENTE_CANTIDAD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `estado` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fecha_baja` datetime DEFAULT NULL,
  `fecha_estado` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `receta`
--

INSERT INTO `receta` (`ID`, `TITULO`, `PASOS`, `IMAGEN`, `FECHA`, `ID_USUARIO`, `CATEGORIA`, `VALORACION`, `DIFICULTAD`, `TIEMPO_PREPARACION`, `INGREDIENTE_CANTIDAD`, `estado`, `fecha_baja`, `fecha_estado`) VALUES
(51, 'Ñoquis', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/ñoquis_img.jpg', '17/04/2024 12:06', 1, 'Almuerzo/Cena', 0, 'Fácil', '60', '', 'A', NULL, '2024-04-17 12:07:24'),
(52, 'prueba cantidades', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/prueba-cantidades_img.jpg', '18/04/2024 20:35', 1, 'Desayuno/Merienda', 0, 'Fácil', '123', NULL, 'A', NULL, '2024-04-18 20:35:38'),
(54, 'Ingredientes repe', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/ingredientes-repe_img.jpg', '18/04/2024 20:58', 1, 'Almuerzo/Cena', 1, 'Fácil', '35', NULL, 'A', NULL, '2024-05-21 11:54:27'),
(55, 'Prueba cantidad 1', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/prueba-cantidad-1_img.jpg', '25/04/2024 11:24', 1, 'Desayuno/Merienda', 0, 'Fácil', '12', NULL, 'A', NULL, '2024-04-25 11:24:56'),
(56, 'Prueba cantidad 2', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/prueba-cantidad-2_img.jpg', '25/04/2024 12:29', 1, 'Bebida/trago', 0, 'Fácil', '1', NULL, 'A', NULL, '2024-04-25 12:29:21'),
(57, 'Prueba cantidad 3', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/prueba-cantidad-3_img.jpg', '25/04/2024 12:32', 1, 'Postre', 0, 'Intermedio', '12', NULL, 'A', NULL, '2024-04-25 12:32:19'),
(58, 'Prueba cantidad 4', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/prueba-cantidad-4_img.jpg', '25/04/2024 12:33', 1, 'Almuerzo/Cena', 0, 'Intermedio', '14', NULL, 'A', NULL, '2024-05-23 21:02:55'),
(59, 'Prueba cantidad 5', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/prueba-cantidad-5_img.jpg', '25/04/2024 18:38', 1, 'Guarnición', 2, 'Fácil', '12', NULL, 'A', NULL, '2024-06-19 12:39:15'),
(60, 'Prueba cantidad 6', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/prueba-cantidad-6_img.jpg', '25/04/2024 18:45', 1, 'Almuerzo/Cena', 3, 'Fácil', '14', NULL, 'A', NULL, '2024-07-04 16:17:18'),
(61, 'Prueba lazy', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/prueba-lazy_img.jpg', '29/04/2024 20:39', 1, 'Desayuno/Merienda', 2, 'Intermedio', '100', NULL, 'A', NULL, '2024-07-31 15:58:47'),
(69, 'error prueba', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/error-prueba_img.jpg', '20/05/2024 22:21', 20, 'Almuerzo/Cena', 0, 'Difícil', '46', NULL, 'A', NULL, '2024-05-20 22:22:29'),
(70, 'Macaron', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/macaron_img.jpg', '21/05/2024 15:18', 1, 'Desayuno/Merienda', 0, 'Intermedio', '25', NULL, 'A', NULL, '2024-05-21 15:19:05'),
(71, 'Panqueques con dulce de leche', NULL, 'C:\\Users\\javit\\Desktop\\Luciano\\Proyectos\\JaviCook/src/main/webapp/img/fotos/panqueques-con-dulce-de-leche_img.jpg', '21/05/2024 15:28', 20, 'Postre', 3, 'Fácil', '12', NULL, 'A', NULL, '2024-06-03 15:22:35'),
(103, 'Edicion prueba', NULL, '/img/fotos/edicion-prueba_img.jpg', '25/06/2024 12:27', 22, 'Desayuno/Merienda', 3, 'Intermedio', '100', NULL, 'A', NULL, '2024-07-31 15:57:37'),
(104, 'Nueva receta prueba', NULL, '/img/fotos/nueva-receta-prueba_img.jpg', '15/07/2024 16:21', 41, 'Guarnición', 1, 'Difícil', '233', NULL, 'A', NULL, '2024-07-31 14:53:00'),
(107, 'Titulo de receta editado 2', NULL, '/img/fotos/prueba-nombre-imagen_1250_img.jpg', '29/07/2024 12:50', 41, 'Desayuno/Merienda', 1, 'Fácil', '1', NULL, 'A', NULL, '2024-07-31 15:43:09'),
(108, 'Prueba nombre imagen', NULL, '/img/fotos/prueba-nombre-imagen_1252_img.jpg', '29/07/2024 12:52', 41, 'Bebida/trago', 2, 'Difícil', '2', NULL, 'A', NULL, '2024-08-01 18:39:13'),
(111, 'Prueba nuevo', NULL, '/img/fotos/prueba-nuevo_1810_img.jpg', '08/08/2024 18:10', 41, 'Brunch', 0, 'Intermedio', '12', NULL, 'A', NULL, '2024-08-08 18:10:01');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `receta_ingrediente`
--

CREATE TABLE `receta_ingrediente` (
  `id_ingrediente` bigint(20) DEFAULT NULL,
  `id_receta` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `receta_ingrediente`
--

INSERT INTO `receta_ingrediente` (`id_ingrediente`, `id_receta`) VALUES
(19, 51),
(16, 51),
(20, 51),
(18, 51),
(22, 52),
(22, 54),
(23, 54),
(22, 55),
(19, 55),
(22, 56),
(22, 57),
(22, 58),
(22, 59),
(22, 60),
(22, 61),
(22, 69),
(22, 70),
(24, 70),
(25, 70),
(22, 71),
(22, 103),
(24, 103),
(28, 104),
(23, 104),
(22, 107),
(22, 108),
(30, 111);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `receta_ingredientes_cantidades`
--

CREATE TABLE `receta_ingredientes_cantidades` (
  `RECETA_ID` bigint(20) DEFAULT NULL,
  `INGREDIENTE_CANTIDAD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id` bigint(20) DEFAULT NULL,
  `estado` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fecha_baja` datetime DEFAULT NULL,
  `fecha_estado` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `receta_ingredientes_cantidades`
--

INSERT INTO `receta_ingredientes_cantidades` (`RECETA_ID`, `INGREDIENTE_CANTIDAD`, `id`, `estado`, `fecha_baja`, `fecha_estado`) VALUES
(52, 'sal: 20gr\r\nagua: 300cc\r\nharina: 500gr', 0, '', NULL, NULL),
(54, 'sal: 5gr\r\npimienta: 6gr', 0, '', NULL, NULL),
(55, 'sal: 20gr\r\nharina: 100gr', 0, '', NULL, NULL),
(56, 'sal:5gr\r\nharina:101gr', 0, '', NULL, NULL),
(57, 'sal:5gr\r\nharina:102gr', 0, '', NULL, NULL),
(58, 'sal:5gr\r\nharina:101gr', 0, '', NULL, NULL),
(59, 'sal:5gr\r\nharina:101gr', 0, '', NULL, NULL),
(60, 'sal:5gr\r\nharina:106gr', 0, '', NULL, NULL),
(61, 'sal:20gr\r\nlevadura: 1gr', 0, '', NULL, NULL),
(69, 'sal: 25gr', NULL, NULL, NULL, NULL),
(70, 'sal: 5gr\r\nharina: 100gr\r\npolvo de hornean: 10gr', NULL, NULL, NULL, NULL),
(71, 'sal: 20gr', NULL, NULL, NULL, NULL),
(103, 'sal: 20gr\r\nharina:200gr', NULL, NULL, NULL, NULL),
(104, 'sal:20gr\r\nagua:100gr\r\nharina:400gr\r\nlevadura:5gr\r\npimienta: 6gr', NULL, NULL, NULL, NULL),
(107, 'sal:20gr', NULL, NULL, NULL, NULL),
(108, 'Sal: 1gr\r\nHarina: 200gr\r\nAceite: 1cda\r\nMiel: 2cda', NULL, NULL, NULL, NULL),
(111, 'pimienta: 5g', NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `ID` int(11) NOT NULL,
  `NOMBRE` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `EMAIL` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CONTRASENIA` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `estado` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fecha_baja` datetime DEFAULT NULL,
  `fecha_estado` datetime DEFAULT NULL,
  `imagenPerfil` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `token_recuperacion` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fecha_expiracion_token` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`ID`, `NOMBRE`, `EMAIL`, `CONTRASENIA`, `estado`, `fecha_baja`, `fecha_estado`, `imagenPerfil`, `token_recuperacion`, `fecha_expiracion_token`) VALUES
(1, 'Luciano', 'javito_junin_05@hotmail.com', '$2a$10$rbUCU767Avwcxqk7.IGMlueNvLjRfrd4gjYxYcwzaeIdSs/1.4DT2', 'A', NULL, '2024-07-02 10:26:47', '/img/luciano_profile.jpg', 'a5d2b8ee-1704-460b-b7ca-26a38442db1b', '2024-07-02 14:26:06'),
(20, 'test', 'javito_junin_05@hotmail.com', '$2a$10$pF5QVpeR2njNgjn9QSIwaOXsOCKdMnNa.nT2ZErtaBeAa95aCL15K', 'A', NULL, '2024-07-04 16:16:57', 'img/default-imagen-perfil.jpg', '3f9b3ec5-3e90-4ffd-9982-8e4b3a97f0da', '2024-07-02 14:31:54'),
(22, 'test2', 'javito_junin_05@hotmail.com', '$2a$10$1XGDktqA/weUPm.eEa03pOVlQ2cVMmLmtNGbR3IkTqymUH/PXQtR6', 'A', NULL, '2024-07-15 13:05:22', '/img/test2_profile.jpg', '93c120bb-6865-4c36-a6fe-9ed06f1584d1', '2024-07-02 14:32:35'),
(41, 'testpass', 'javito_junin_05@hotmail.com', '$2a$10$kabPwge1oqx/pQoHbPrqsuNU3qENpxS9bx/5FeALGnc3xP0HGAj66', 'A', NULL, '2024-08-07 16:33:53', '/img/testpass_profile.jpg', 'b083ab7f-ada9-49cb-8967-7e633a7108f4', '2024-07-04 15:35:17');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_favorito_receta`
--

CREATE TABLE `usuario_favorito_receta` (
  `usuario_id` bigint(20) NOT NULL,
  `receta_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `usuario_favorito_receta`
--

INSERT INTO `usuario_favorito_receta` (`usuario_id`, `receta_id`) VALUES
(20, 69),
(22, 61),
(41, 103);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `valoracion_usuario`
--

CREATE TABLE `valoracion_usuario` (
  `id` bigint(20) NOT NULL,
  `estado` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fecha_baja` datetime DEFAULT NULL,
  `fecha_estado` datetime DEFAULT NULL,
  `valoracion` int(11) DEFAULT NULL,
  `id_receta` bigint(20) DEFAULT NULL,
  `id_usuario` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `valoracion_usuario`
--

INSERT INTO `valoracion_usuario` (`id`, `estado`, `fecha_baja`, `fecha_estado`, `valoracion`, `id_receta`, `id_usuario`) VALUES
(2, 'A', NULL, '2024-05-21 11:54:27', 1, 54, 20),
(3, 'A', NULL, '2024-05-21 17:30:29', 4, 61, 1),
(5, 'A', NULL, '2024-06-03 15:22:35', 3, 71, 1),
(9, 'A', NULL, '2024-06-19 12:38:31', 1, 59, 20),
(10, 'A', NULL, '2024-06-19 12:38:50', 2, 59, 22),
(11, 'A', NULL, '2024-06-19 12:39:15', 3, 59, 1),
(12, 'A', NULL, '2024-07-04 16:17:18', 3, 60, 20),
(13, 'A', NULL, '2024-07-16 10:31:49', 1, 104, 41),
(15, 'A', NULL, '2024-07-31 14:57:09', 2, 108, 41),
(16, 'A', NULL, '2024-07-31 15:43:09', 1, 107, 41),
(17, 'A', NULL, '2024-07-31 15:57:37', 3, 103, 41),
(18, 'A', NULL, '2024-07-31 15:58:47', 1, 61, 41);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `comentario`
--
ALTER TABLE `comentario`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `ingrediente`
--
ALTER TABLE `ingrediente`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `receta`
--
ALTER TABLE `receta`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fk_receta_usuario` (`ID_USUARIO`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `valoracion_usuario`
--
ALTER TABLE `valoracion_usuario`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `comentario`
--
ALTER TABLE `comentario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `ingrediente`
--
ALTER TABLE `ingrediente`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `receta`
--
ALTER TABLE `receta`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=112;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT de la tabla `valoracion_usuario`
--
ALTER TABLE `valoracion_usuario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `receta`
--
ALTER TABLE `receta`
  ADD CONSTRAINT `fk_receta_usuario` FOREIGN KEY (`ID_USUARIO`) REFERENCES `usuario` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
