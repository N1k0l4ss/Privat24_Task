-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Время создания: Сен 30 2021 г., 11:40
-- Версия сервера: 10.3.22-MariaDB
-- Версия PHP: 7.1.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `company`
--

-- --------------------------------------------------------

--
-- Структура таблицы `company`
--

CREATE TABLE `company` (
  `ID` int(11) NOT NULL,
  `TITLE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `company`
--

INSERT INTO `company` (`ID`, `TITLE`) VALUES
(92, 'Kihn - Klein'),
(93, 'Hintz and Sons');

-- --------------------------------------------------------

--
-- Структура таблицы `department`
--

CREATE TABLE `department` (
  `ID` int(11) NOT NULL,
  `FREEWORK` tinyint(1) DEFAULT 0,
  `STARTTIME` time DEFAULT NULL,
  `TITLE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `COMPANY_ID` int(11) DEFAULT NULL,
  `WORKMODE_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `department`
--

INSERT INTO `department` (`ID`, `FREEWORK`, `STARTTIME`, `TITLE`, `COMPANY_ID`, `WORKMODE_ID`) VALUES
(194, 0, '08:52:00', 'Industrial', 92, 1),
(195, 0, '08:00:00', 'Beauty', 92, 2),
(196, 0, '10:33:00', 'Electronics', 93, 1),
(197, 1, NULL, 'Movies', 93, NULL);

-- --------------------------------------------------------

--
-- Структура таблицы `employee`
--

CREATE TABLE `employee` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `STARTTIME` time DEFAULT NULL,
  `COMPANY_ID` int(11) DEFAULT NULL,
  `DEPARTMENT_ID` int(11) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  `PREFERENCE_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `employee`
--

INSERT INTO `employee` (`ID`, `NAME`, `STARTTIME`, `COMPANY_ID`, `DEPARTMENT_ID`, `ROLE_ID`, `PREFERENCE_ID`) VALUES
(9040, 'George Altenwerth', '08:00:00', 92, 194, 571, 2),
(9041, 'Carlotta Jacobi', '01:36:00', 92, 194, 570, 1),
(9042, 'Rosalind Funk', '14:41:00', 92, 194, 570, 1),
(9043, 'Jason Kovacek', '08:00:00', 92, 195, 572, 2),
(9044, 'Abdiel Hettinger', '12:55:00', 92, 195, 570, 1),
(9045, 'Dan Pollich', '01:59:00', 93, 197, 573, 1),
(9046, 'Dorthy Schaefer', '08:00:00', 93, 196, 575, 2),
(9047, 'Kennedy Boehm', '08:00:00', 93, 197, 573, 2),
(9048, 'Brad Lehner', NULL, 93, 197, 575, 3),
(9049, 'Jamey Kuhic', '08:00:00', 93, 197, 575, 2);

-- --------------------------------------------------------

--
-- Структура таблицы `preference`
--

CREATE TABLE `preference` (
  `ID` int(11) NOT NULL,
  `COEFFICIENT` double DEFAULT NULL,
  `PREFERENCENAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `preference`
--

INSERT INTO `preference` (`ID`, `COEFFICIENT`, `PREFERENCENAME`) VALUES
(1, 0.2, 'Work another time'),
(2, 0, 'Work default time'),
(3, 0.1, 'Work at home');

-- --------------------------------------------------------

--
-- Структура таблицы `role`
--

CREATE TABLE `role` (
  `ID` int(11) NOT NULL,
  `FREEWORK` tinyint(1) DEFAULT 0,
  `TITLE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `COMPANY_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `role`
--

INSERT INTO `role` (`ID`, `FREEWORK`, `TITLE`, `COMPANY_ID`) VALUES
(569, 1, 'National Infrastructure Orchestrator', 92),
(570, 1, 'Chief Web Officer', 92),
(571, 0, 'Legacy Research Representative', 92),
(572, 1, 'Legacy Mobility Orchestrator', 92),
(573, 1, 'Forward Intranet Orchestrator', 93),
(574, 1, 'Product Identity Manager', 93),
(575, 0, 'Principal Mobility Specialist', 93),
(576, 1, 'Chief Implementation Supervisor', 93);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `company`
--
ALTER TABLE `company`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FK_DEPARTMENT_WORKMODE_ID` (`WORKMODE_ID`),
  ADD KEY `FK_DEPARTMENT_COMPANY_ID` (`COMPANY_ID`);

--
-- Индексы таблицы `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FK_EMPLOYEE_COMPANY_ID` (`COMPANY_ID`),
  ADD KEY `FK_EMPLOYEE_PREFERENCE_ID` (`PREFERENCE_ID`),
  ADD KEY `FK_EMPLOYEE_DEPARTMENT_ID` (`DEPARTMENT_ID`),
  ADD KEY `FK_EMPLOYEE_ROLE_ID` (`ROLE_ID`);

--
-- Индексы таблицы `preference`
--
ALTER TABLE `preference`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FK_ROLE_COMPANY_ID` (`COMPANY_ID`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `company`
--
ALTER TABLE `company`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=98;

--
-- AUTO_INCREMENT для таблицы `department`
--
ALTER TABLE `department`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=207;

--
-- AUTO_INCREMENT для таблицы `employee`
--
ALTER TABLE `employee`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9078;

--
-- AUTO_INCREMENT для таблицы `preference`
--
ALTER TABLE `preference`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT для таблицы `role`
--
ALTER TABLE `role`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=588;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `department`
--
ALTER TABLE `department`
  ADD CONSTRAINT `FK_DEPARTMENT_COMPANY_ID` FOREIGN KEY (`COMPANY_ID`) REFERENCES `company` (`ID`),
  ADD CONSTRAINT `FK_DEPARTMENT_WORKMODE_ID` FOREIGN KEY (`WORKMODE_ID`) REFERENCES `preference` (`ID`);

--
-- Ограничения внешнего ключа таблицы `employee`
--
ALTER TABLE `employee`
  ADD CONSTRAINT `FK_EMPLOYEE_COMPANY_ID` FOREIGN KEY (`COMPANY_ID`) REFERENCES `company` (`ID`),
  ADD CONSTRAINT `FK_EMPLOYEE_DEPARTMENT_ID` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `department` (`ID`),
  ADD CONSTRAINT `FK_EMPLOYEE_PREFERENCE_ID` FOREIGN KEY (`PREFERENCE_ID`) REFERENCES `preference` (`ID`),
  ADD CONSTRAINT `FK_EMPLOYEE_ROLE_ID` FOREIGN KEY (`ROLE_ID`) REFERENCES `role` (`ID`);

--
-- Ограничения внешнего ключа таблицы `role`
--
ALTER TABLE `role`
  ADD CONSTRAINT `FK_ROLE_COMPANY_ID` FOREIGN KEY (`COMPANY_ID`) REFERENCES `company` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
