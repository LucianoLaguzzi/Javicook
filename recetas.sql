-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-09-2024 a las 16:23:11
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

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `receta_ingrediente`
--

CREATE TABLE `receta_ingrediente` (
  `id_ingrediente` bigint(20) DEFAULT NULL,
  `id_receta` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
(1, 'Luciano', 'javito_junin_05@hotmail.com', '$2a$10$rbUCU767Avwcxqk7.IGMlueNvLjRfrd4gjYxYcwzaeIdSs/1.4DT2', 'A', NULL, '2024-08-29 11:34:05', '/img/luciano_profile.jpg', 'a5d2b8ee-1704-460b-b7ca-26a38442db1b', '2024-07-02 14:26:06'),
(20, 'test', 'javito_junin_05@hotmail.com', '$2a$10$pF5QVpeR2njNgjn9QSIwaOXsOCKdMnNa.nT2ZErtaBeAa95aCL15K', 'A', NULL, '2024-08-29 11:29:37', 'img/default-imagen-perfil.jpg', '3f9b3ec5-3e90-4ffd-9982-8e4b3a97f0da', '2024-07-02 14:31:54'),
(22, 'test2', 'javito_junin_05@hotmail.com', '$2a$10$1XGDktqA/weUPm.eEa03pOVlQ2cVMmLmtNGbR3IkTqymUH/PXQtR6', 'A', NULL, '2024-08-29 12:05:04', '/img/test2_profile.jpg', '93c120bb-6865-4c36-a6fe-9ed06f1584d1', '2024-07-02 14:32:35'),
(41, 'testpass', 'javito_junin_05@hotmail.com', '$2a$10$kabPwge1oqx/pQoHbPrqsuNU3qENpxS9bx/5FeALGnc3xP0HGAj66', 'A', NULL, '2024-09-03 17:24:48', '/img/testpass_profile.jpg', 'b083ab7f-ada9-49cb-8967-7e633a7108f4', '2024-07-04 15:35:17');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_favorito_receta`
--

CREATE TABLE `usuario_favorito_receta` (
  `usuario_id` bigint(20) NOT NULL,
  `receta_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=155;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT de la tabla `valoracion_usuario`
--
ALTER TABLE `valoracion_usuario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

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
